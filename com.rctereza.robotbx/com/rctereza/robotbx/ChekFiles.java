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

	public static void main(String[] args) {
		// SISTEMA.........: SPED Contribuições
		// TIPO DE ARQUIVO.: Escrituração
		// TIPO DE PESQUISA: Período da Escrituração
		// NOME DO ARQUIVO.: PISCOFINS_20200101_20200131_07214419000195_Original_20200311170643_466E91B68B0768A77ED8E18073A1310E47D56CF1.txt
		
		Path folderPath = Paths.get("C:\\Temp\\ReceitanetBX\\Escrituração");

		List<String> periods = new ArrayList<>();

		try (Stream<Path> paths = Files.list(folderPath)) {
			
			paths.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".txt")) // filter by extension
					.forEach(p -> {
						String fileName = p.getFileName().toString();

						// Example: extract part of the name
						// Suppose filename is "report_20240101.txt"
						String extracted = extractPart(fileName);

//						System.out.println("File: " + fileName);
//						System.out.println("Extracted: " + extracted);
						
						periods.add(extracted);
						
					});
			
			Set<YearMonth> existingMonths = new HashSet<>();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	        for (String period : periods) {
	            String startDateStr = period.split("_")[0];
	            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
	            existingMonths.add(YearMonth.from(startDate));
	        }
	        
			// Define full range
			YearMonth start = YearMonth.of(2020, 1);
			YearMonth end = YearMonth.of(2025, 12);
	        
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
	        }
	        else {
		        // Print result
		        missingMonths.forEach(System.out::println);
	        }
	        

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String extractPart(String fileName) {
		// Remove extension first
		int initialPosition = fileName.indexOf('_') + 1;

		String firtPart = fileName.substring(initialPosition, 27);

		return firtPart;
	}
}
