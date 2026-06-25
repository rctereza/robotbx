package com.rctereza.robotbx.ztest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Main;
import com.rctereza.robotbx.enums.Sped;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.threads.ProcessRobot;
import com.rctereza.robotbx.tools.SpedUtils;

public class CheckTheDownloadedFiles {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessRobot.class);

	public static void main(String[] args) {

		int count = 1;
		
		List<ReceitaBx> receitaBxList = Main.getAppData().getLastListAdded(ReceitaBx.class);

		for (ReceitaBx receitaBx : receitaBxList) {
			
			String sourceFolder = receitaBx.CONFIGURACAO().DOWNLOAD_FOLDER();
			String targetFolder = receitaBx.CONFIGURACAO().SAVE_FOLDER() + "\\" + receitaBx.PROCURADOR().CLIENTE();

			logger.debug("#{} - SISTEMA: {}, TIPO_ARQUIVO: {}, TIPO_PESQUISA: {}",count++,receitaBx.SISTEMA(),receitaBx.TIPO_ARQUIVO(),receitaBx.TIPO_PESQUISA());
			
			
			
//			Integer TOTAL_PERIODOS_FALTANDO = 0;
//			String PERIODOS_FALTANDO = getMissingPeriods(receitaBx);
//			
//			if (PERIODOS_FALTANDO.length() > 0) {
//				String[] periods = PERIODOS_FALTANDO.split(",");
//				TOTAL_PERIODOS_FALTANDO = periods.length;
//			}
//			
//			logger.debug("TOTAL_PERIODOS_FALTANDO: {}, PERIODOS_FALTANDO: {}",TOTAL_PERIODOS_FALTANDO,PERIODOS_FALTANDO);
		}
		

	}

	private static String getMissingPeriods(ReceitaBx original) {
		String result = "";

		int startYear;
		int startMonth;
		int endYear;
		int endMonth;
		String fileNameBegins;
		String fileFolderPath;
		int fileFolderIndex;

		if (original.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())
				&& original.TIPO_PESQUISA().equals(SpedUtils.contribuicoesSearchTypes[2])) {

			// SISTEMA.........: SPED Contribuições
			// TIPO DE ARQUIVO.: Escrituração
			// TIPO DE PESQUISA: Período da Escrituração
			// NOME DO ARQUIVO.:
			// PISCOFINS_20200101_20200131_07214419000195_Original_20200311170643_466E91B68B0768A77ED8E18073A1310E47D56CF1.txt

			String[] values = original.DATA_INICIO().split("/");
			startYear = Integer.parseInt(values[2]);
			startMonth = Integer.parseInt(values[1]);

			values = original.DATA_FIM().split("/");
			endYear = Integer.parseInt(values[2]);
			endMonth = Integer.parseInt(values[1]);

			fileNameBegins = "PISCOFINS";
			fileFolderPath = original.CONFIGURACAO().DOWNLOAD_FOLDER() + "\\Escrituração";
			fileFolderIndex = 0;

			result = getPeriods(startYear, startMonth, endYear, endMonth, fileNameBegins, fileFolderPath,
					fileFolderIndex);
		}

		else if (original.SISTEMA().equals(Sped.EFD.getValue())) {

			// SISTEMA.........: SPED Fiscal-EFD ICMS IPI
			// TIPO DE ARQUIVO.: Escrituração Fiscal Digital
			// TIPO DE PESQUISA: Por Período da Escrituração
			// NOME DO ARQUIVO.:
			// 07214419000195-293845298-20210401-20210430-0-9535ADC3A5F5892956F5ECDEDE1E6D397F8FE8B4-SPED-EFD.txt

			String[] values = original.DATA_INICIO().split("/");
			startYear = Integer.parseInt(values[2]);
			startMonth = Integer.parseInt(values[1]);

			values = original.DATA_FIM().split("/");
			endYear = Integer.parseInt(values[2]);
			endMonth = Integer.parseInt(values[1]);

			fileNameBegins = original.PROCURADOR().CLIENTE_DOC(); // "07214419000195";
			fileFolderPath = original.CONFIGURACAO().DOWNLOAD_FOLDER() + "\\Escrituração Fiscal Digital";
			fileFolderIndex = 1;

			result = getPeriods(startYear, startMonth, endYear, endMonth, fileNameBegins, fileFolderPath,
					fileFolderIndex);
		}

		else if (original.SISTEMA().equals(Sped.ECF.getValue())
				&& original.TIPO_PESQUISA().equals(SpedUtils.ecfSearchTypes[1])) {

			// SISTEMA.........: SPED ECF
			// TIPO DE ARQUIVO.: Escrituração
			// TIPO DE PESQUISA: Período da Escrituração
			// NOME DO ARQUIVO.: SPEDECF-07214419000195-20140101-20141231-20150924103745.txt

			String[] values = original.DATA_INICIO().split("/");
			startYear = Integer.parseInt(values[2]);
			startMonth = Integer.parseInt(values[1]);

			values = original.DATA_FIM().split("/");
			endYear = Integer.parseInt(values[2]);
			endMonth = Integer.parseInt(values[1]);

			fileNameBegins = "SPEDECF";
			fileFolderPath = original.CONFIGURACAO().DOWNLOAD_FOLDER() + "\\Escrituração";
			fileFolderIndex = 2;

			result = getPeriods(startYear, startMonth, endYear, endMonth, fileNameBegins, fileFolderPath,
					fileFolderIndex);
		}

		return result;

	}

	private static String getPeriods(int startYear, int startMonth, int endYear, int endMonth, String fileNameBegins,
			String fileFolder, int fileFolderIndex) {

//		logger.debug("{}/{}, {}/{}, {}, {}, {}", startYear, startMonth, endYear, endMonth, fileNameBegins, fileFolder,
//				fileFolderIndex);

		String result = "";

		Path folderPath = Paths.get(fileFolder);

		if (Files.exists(folderPath)) {

			List<String> periods = new ArrayList<>();

			try (Stream<Path> paths = Files.list(folderPath)) {

				paths.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".txt")) // filter by extension
						.forEach(p -> {
							String fileName = p.getFileName().toString();

							if (fileName.startsWith(fileNameBegins)) {

								String extracted = extractPart(fileName, fileFolderIndex);

//							System.out.println("File: " + fileName);
//							System.out.println("Extracted: " + extracted);

								periods.add(extracted);
							}

						});

				Set<YearMonth> existingMonths = new HashSet<>();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

				for (String period : periods) {
					String startDateStr = period.split("_")[0];
					LocalDate startDate = LocalDate.parse(startDateStr, formatter);
					existingMonths.add(YearMonth.from(startDate));
				}

				// Define full range
				YearMonth start = YearMonth.of(startYear, startMonth);
				YearMonth end = YearMonth.of(endYear, endMonth);

				List<YearMonth> missingMonths = new ArrayList<>();

				YearMonth current = start;
				while (!current.isAfter(end)) {
					if (!existingMonths.contains(current)) {
						missingMonths.add(current);
					}
					current = current.plusMonths(1);
				}

				if (missingMonths.size() == 0) {
					logger.info("Todos os meses existem para o periodo informado.");
				} else {

					logger.info("(Existem meses faltando no periodo informado. Quantidade: {}.", missingMonths.size());
					result = missingMonths.toString();
				}

			} catch (IOException e) {
				logger.error("getPeriods()-> {} ", e.getMessage(), e);
			}

		}

		return result;
	}

	private static String extractPart(String fileName, int fileFolderIndex) {
		String result = "";

		if (fileFolderIndex == 0) {
			int initialPosition = fileName.indexOf('_') + 1;
			result = fileName.substring(initialPosition, 27);
		} else if (fileFolderIndex == 1 || fileFolderIndex == 2) {
			String value = fileName.substring(fileName.indexOf("-") + 1);
			value = value.substring(value.indexOf("-") + 1);
			result = value.substring(0, 17).replace("-", "_");
		}

		return result;
	}
}
