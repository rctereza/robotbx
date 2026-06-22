package com.rctereza.robotbx.ztest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.ReceitaFile;
import com.rctereza.robotbx.tools.FileUtils;

public class KeepingOnlyAmendedFiles {

	public static void main(String[] args) {
		String path = "C:\\Temp\\ReceitanetBX\\Resultado\\STANGHERLIN SUPERMERCADO LTDA\\";
		keepOnlyAmendedFiles(path);
		System.out.println("Done!");
	}

	private static void keepOnlyAmendedFiles(String root) {

		List<String> files = new ArrayList<>();

		try (var paths = Files.walk(Paths.get(root))) {

			paths.filter(Files::isRegularFile).filter(path -> path.toString().toLowerCase().endsWith(".txt"))
					.map(Path::toString).forEach(files::add);

		} catch (IOException e) {
			e.printStackTrace();
		}

//		files.forEach(System.out::println);
//		System.out.println("-----------------------------------------------------");
		
		Map<String, ReceitaFile> objects = new HashMap<>();

		for (String name : files) {

			String[] values = name.substring(name.lastIndexOf("\\") + 1).split("_");

			String FILE_NAME = name.substring(name.lastIndexOf("\\") + 1);
			String FILE_PATH = name.substring(0, name.lastIndexOf("\\") + 1);
			String FILE_EXTENSION = FILE_NAME.substring(FILE_NAME.lastIndexOf("."));
			String FILE_SHORT_NAME = FILE_NAME.indexOf("Retificadora") > 0
					? FILE_NAME.substring(0, FILE_NAME.indexOf("Retificadora"))
					: FILE_NAME.substring(0, FILE_NAME.indexOf("Original"));
			String DOCUMENT_NAME = values[0];
			String DOCUMENT_PERIOD_FROM = values[1];
			String DOCUMENT_PERIOD_TO = values[2];
			String DOCUMENT_CODE = values[3];
			String DOCUMENT_TYPE = values[4];
			String DOCUMENT_DATETIME_SENT = values[5];
			String DOCUMENT_KEY = values[6].substring(0, values[6].lastIndexOf("."));

//			System.out.println("FILE_NAME.............: " + FILE_NAME);
//			System.out.println("FILE_PATH.............: " + FILE_PATH);
//			System.out.println("FILE_EXTENSION........: " + FILE_EXTENSION);
//			System.out.println("FILE_SHORT_NAME.......: " + FILE_SHORT_NAME);
//			System.out.println("DOCUMENT_NAME.........: " + DOCUMENT_NAME);
//			System.out.println("DOCUMENT_PERIOD_FROM..: " + DOCUMENT_PERIOD_FROM);
//			System.out.println("DOCUMENT_PERIOD_TO....: " + DOCUMENT_PERIOD_TO);
//			System.out.println("DOCUMENT_CODE.........: " + DOCUMENT_CODE);
//			System.out.println("DOCUMENT_TYPE.........: " + DOCUMENT_TYPE);
//			System.out.println("DOCUMENT_DATETIME_SENT: " + DOCUMENT_DATETIME_SENT);
//			System.out.println("DOCUMENT_KEY..........: " + DOCUMENT_KEY);

			objects.put(FILE_SHORT_NAME + DOCUMENT_DATETIME_SENT,
					new ReceitaFile(FILE_NAME, FILE_PATH, FILE_EXTENSION, FILE_SHORT_NAME + DOCUMENT_DATETIME_SENT,
							DOCUMENT_NAME, DOCUMENT_PERIOD_FROM, DOCUMENT_PERIOD_TO, DOCUMENT_CODE, DOCUMENT_TYPE,
							DOCUMENT_DATETIME_SENT, DOCUMENT_KEY));

			// System.out.println(objects);
			// break;
		}

//		objects.keySet().stream().sorted().forEach(key -> System.out.println(key + " = " + objects.get(key)));
//		System.out.println("-----------------------------------------------------");

		Map<String, ReceitaFile> sortedMap =
				objects.entrySet()
			           .stream()
			           .sorted(Map.Entry.comparingByKey())
			           .collect(
			               java.util.stream.Collectors.toMap(
			                   Map.Entry::getKey,
			                   Map.Entry::getValue,
			                   (a, b) -> a,
			                   LinkedHashMap::new
			               )
			           );


		List<ReceitaFile> result = new ArrayList<>();
		Long mostRecentDaTeTimeSent = 0l;
		String previousPeriod = "";
		String fileKept = "";
		ReceitaFile obj = null;
		Integer total = 0;
		Integer count = 0;
		
		for (ReceitaFile value : sortedMap.values()) {
			total++;
			String currentKey = value.DOCUMENT_PERIOD_FROM() + "_" + value.DOCUMENT_PERIOD_TO() + "_" + value.DOCUMENT_DATETIME_SENT();
			String currentPeriod = value.DOCUMENT_PERIOD_FROM() + "_" + value.DOCUMENT_PERIOD_TO();
			Long currentDateTimeSent = Long.valueOf(value.DOCUMENT_DATETIME_SENT());
			String currentFileName = value.FILE_NAME();
			
			if (!previousPeriod.isBlank() && !previousPeriod.equals(currentPeriod)) {
				count++;
				result.add(obj);
				mostRecentDaTeTimeSent = 0l;
				System.out.println("*" + fileKept);
				System.out.println("-----------------------------------------------------");
			}
			
		    System.out.println(currentKey + " - " + currentFileName);
		    
		    previousPeriod = currentPeriod;
		    
		    if (currentDateTimeSent > mostRecentDaTeTimeSent) {
		    	mostRecentDaTeTimeSent = currentDateTimeSent;
		    	fileKept = currentKey + " - " + currentFileName;
		    	obj = value;
		    }
		    
		}
		
		System.out.println("*" + fileKept);
		System.out.println("-----------------------------------------------------");
		System.out.println("total files read....:" + total.toString());
		System.out.println("-----------------------------------------------------");
		
		result.forEach(System.out::println);
		System.out.println("-----------------------------------------------------");
		System.out.println("total files selected:" + count.toString());
		System.out.println("-----------------------------------------------------");
		
		Boolean filesNotMoved = true;
		
		for (ReceitaFile value : result) {
			System.out.println(value.FILE_PATH() + value.FILE_PATH());
			if (filesNotMoved) {
				try {
					FileUtils.createDirectory(Constants.SOFTWARE_TEMP_FOLDER);
					FileUtils.copyDirectory(value.FILE_PATH(), Constants.SOFTWARE_TEMP_FOLDER);
					FileUtils.emptyDirectory(value.FILE_PATH());
					filesNotMoved = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Path source = Paths.get(Constants.SOFTWARE_TEMP_FOLDER + value.FILE_NAME());
			Path target = Paths.get(value.FILE_PATH() + value.FILE_NAME());

			try {
				Files.copy(
					    source,
					    target,
					    StandardCopyOption.REPLACE_EXISTING,
					    StandardCopyOption.COPY_ATTRIBUTES
					);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
