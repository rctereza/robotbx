package com.rctereza.robotbx.controllers;

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
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.enums.Sped;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.threads.ProcessRobot;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.SpedUtils;
import com.rctereza.robotbx.wrappers.Ref;

public class Controller {

	public void startRobot(Ref<List<ReceitaBx>> list, boolean reset) {

		try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {

			System.out.println("Starting...");
			System.out.println("-------------------------------------------------------------------------------");

			cleanDirectory(); // delete all files/folders in the directory where the files will be downloaded.

			for (int i = 0; i < list.get().size(); i++) {

				if (i > 0) {
					System.out.println("Waiting 5 seconds before starting to process the next item...");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}

				Ref<ReceitaBx> params = new Ref<>(list.get().get(i));

				System.out.println("#" + (i + 1) + "/" + list.get().size() + " - " + params.get().SISTEMA() + " ["
						+ params.get().ULTIMO_PEDIDO_SOLICITADO() + "] ["
						+ params.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO() + "] Before...");

				var result = "";

				var future = executor.submit(new ProcessRobot(params, reset));

				try {
					result = future.get();

				} catch (ExecutionException e) {
					result = e.getMessage();

				} catch (InterruptedException e1) {
					result = e1.getMessage();
					Thread.currentThread().interrupt();

				} finally {
					System.out.println(result);
					setResult(params, result);

				}

				System.out.println("#" + (i + 1) + "/" + list.get().size() + " - " + params.get().SISTEMA() + " ["
						+ params.get().ULTIMO_PEDIDO_SOLICITADO() + "] ["
						+ params.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO() + "] After...");
				System.out.println("-------------------------------------------------------------------------------");
			}
		}
		
		System.out.println("Done. It was processed [" + list.get().size() + "] item(s).");
	}

	private void cleanDirectory() {
		Path folderPath = Paths.get(Constants.PROGRAM_DOWNLOADED_FOLDER); // Change to your folder path
		try {
			FileUtils.clearDirectory(folderPath);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	private void setResult(Ref<ReceitaBx> receitaBx, String message) {
		
		String PERIODOS_FALTANDO = "";
		Integer TOTAL_PERIODOS_FALTANDO = 0;
 		
		if (receitaBx.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO() != null && !receitaBx.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO().equals("")) {
			PERIODOS_FALTANDO = getMissingPeriods(receitaBx.get());
			if (PERIODOS_FALTANDO.length() > 0) {
				String[] periods = PERIODOS_FALTANDO.split(",");
				TOTAL_PERIODOS_FALTANDO = periods.length;
			}
		}

		receitaBx.set(new ReceitaBx(receitaBx.get().RESOLUCAO_TELA(), receitaBx.get().CERTIFICADO(), null, null, null,
				receitaBx.get().PERFIL(), receitaBx.get().PERFIL_TYPE(), receitaBx.get().PERFIL_VALUE(),
				receitaBx.get().SISTEMA(), receitaBx.get().TIPO_ARQUIVO(), receitaBx.get().TIPO_PESQUISA(),
				receitaBx.get().DATA_INICIO(), receitaBx.get().DATA_FIM(), receitaBx.get().CNPJ_INCORPORADORA(),
				receitaBx.get().TIPO_EVENTO(), receitaBx.get().BAIXAR_ARQUIVO_ASSINADO(),
				receitaBx.get().CNPJ_ESTABELECIMENTO(), receitaBx.get().BUSCAR_TODOS_ESTABLECIMENTOS(),
				receitaBx.get().INSCRICAO_ESTADUAL(), receitaBx.get().ULTIMO_ARQUIVO_TRANSMITIDO(),
				receitaBx.get().ULTIMO_PEDIDO_SOLICITADO(), receitaBx.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO(),
				message, PERIODOS_FALTANDO, TOTAL_PERIODOS_FALTANDO));

	}
	
	private String getMissingPeriods(ReceitaBx receitaBx) {
		String result = "";
		
		String[] fileFolder = { "C:\\Temp\\ReceitanetBX\\Escrituração",
				"C:\\Temp\\ReceitanetBX\\Escrituração Fiscal Digital", "C:\\Temp\\ReceitanetBX\\Escrituração" };

		int startYear;
		int startMonth;
		int endYear;
		int endMonth;
		String fileNameBegins;
		int fileFolderIndex;

		// SISTEMA.........: SPED Contribuições
		// TIPO DE ARQUIVO.: Escrituração
		// TIPO DE PESQUISA: Período da Escrituração
		// NOME DO ARQUIVO.:
		// PISCOFINS_20200101_20200131_07214419000195_Original_20200311170643_466E91B68B0768A77ED8E18073A1310E47D56CF1.txt
//		startYear = 2021;
//		startMonth = 4;
//		endYear = 2026;
//		endMonth = 3;
//		fileNameBegins = "PISCOFINS";
//		fileFolderIndex = 0;

		// SISTEMA.........: SPED Fiscal-EFD ICMS IPI
		// TIPO DE ARQUIVO.: Escrituração Fiscal Digital
		// TIPO DE PESQUISA: Por Período da Escrituração
		// NOME DO ARQUIVO.:
		// 07214419000195-293845298-20210401-20210430-0-9535ADC3A5F5892956F5ECDEDE1E6D397F8FE8B4-SPED-EFD.txt
//		startYear = 2021;
//		startMonth = 4;
//		endYear = 2026;
//		endMonth = 3;
//		fileNameBegins = "07214419000195";
//		fileFolderIndex = 1;

		// SISTEMA.........: SPED ECF
		// TIPO DE ARQUIVO.: Escrituração
		// TIPO DE PESQUISA: Período da Escrituração
		// NOME DO ARQUIVO.: SPEDECF-07214419000195-20140101-20141231-20150924103745.txt
		
	//	if (receitaBx.SISTEMA().equals(Sped.ECF.getValue()) && receitaBx.TIPO_ARQUIVO().equals(SpedUtils.)) {
			
		
		startYear = Integer.valueOf(receitaBx.DATA_INICIO().substring(3));
		startMonth = 1;
		endYear = Integer.valueOf(receitaBx.DATA_FIM().substring(3));
		endMonth = 12;
		fileNameBegins = "SPEDECF";
		fileFolderIndex = 2;

		Path folderPath = Paths.get(fileFolder[fileFolderIndex]);

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
				System.out.println("There's no missing period in this range...");
			} else {
				// Print result
				missingMonths.forEach(System.out::println);
				System.out.println(missingMonths.size() + " months are missing...");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String extractPart(String fileName, int fileFolderIndex) {
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
