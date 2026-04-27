package com.rctereza.robotbx;

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

public class ChekFiles {

	private static int startYear;
	private static int startMonth;

	private static int endYear;
	private static int endMonth;

	private static String fileNameBegins;
	private static int fileFolderIndex;

	public static void main(String[] args) {

		String[] fileFolder = { "C:\\Temp\\ReceitanetBX\\Escrituração",
				"C:\\Temp\\ReceitanetBX\\Escrituração Fiscal Digital", "C:\\Temp\\ReceitanetBX\\Escrituração" };

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
		startYear = 2014;
		startMonth = 1;
		endYear = 2025;
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
