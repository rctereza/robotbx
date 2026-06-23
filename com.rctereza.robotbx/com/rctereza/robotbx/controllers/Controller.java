package com.rctereza.robotbx.controllers;

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
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.ReceitaFile;
import com.rctereza.robotbx.models.Setting;
import com.rctereza.robotbx.threads.ProcessRobot;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.wrappers.Ref;

public class Controller {

	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	private static String sourceFolder;
	private static String targetFolder;

	public void startRobot(Ref<List<ReceitaBx>> list) throws InterruptedException, ExecutionException, IOException {

		try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {

			logger.info("Starting...");

			for (int i = 0; i < list.get().size(); i++) {

				ReceitaBx params = list.get().get(i);
				
				logger.debug("ReceitaBX: {}",params);

				if (i == 0) {
					sourceFolder = params.CONFIGURACAO().DOWNLOAD_FOLDER();
					targetFolder = params.CONFIGURACAO().SAVE_FOLDER() + "\\" + params.PROCURADOR().CLIENTE();
					
					logger.info("Deleting all files/folders in the directory ({}) before the downloading starts....", sourceFolder);
					FileUtils.emptyDirectory(sourceFolder);
					
				} else {
					logger.info("Waiting 5 seconds before starting to process the next item...");
					Thread.sleep(5000);
				}

				logger.info("-------------------------------------------------------------------------------");

				logger.info("#{}/{} - {} / {} / {} [{}] [{}] Before...", (i + 1), list.get().size(), params.SISTEMA(),
						params.TIPO_ARQUIVO(), params.TIPO_PESQUISA(), params.ULTIMO_PEDIDO_SOLICITADO(),
						params.DATA_HORA_CONCLUSAO_PROCESSAMENTO());

				var future = executor.submit(new ProcessRobot(params));

				ReceitaBx updated = future.get();

				list.get().set(i, updated);
				
				logger.debug("ReceitaBX: {}",updated);
				
				logger.info("#{}/{} - {} / {} / {} [{}] [{}] After...", (i + 1), list.get().size(), updated.SISTEMA(),
						updated.TIPO_ARQUIVO(), updated.TIPO_PESQUISA(), updated.ULTIMO_PEDIDO_SOLICITADO(),
						updated.DATA_HORA_CONCLUSAO_PROCESSAMENTO());

				logger.info("-------------------------------------------------------------------------------");

				logger.info("Moving all files/folders downloaded to this new location ({})....", targetFolder);
				
				FileUtils.copyDirectory(sourceFolder,targetFolder);
				
				FileUtils.emptyDirectory(sourceFolder);
				
				if (list.get().getFirst().CONFIGURACAO().KEEP_WHICH_FILES().equals(Setting.KeepWhichFiles.ONLY_AMEND)) {
					logger.info("Keeping only the last file/amended on this new location ({})....", targetFolder);
					keepOnlyAmendedFiles(targetFolder);
				}
			}
		}

		logger.info("-------------------------------------------------------------------------------");
		logger.info("Done. It was processed [{}] item(s).", list.get().size());

	}
	
	private void keepOnlyAmendedFiles(String root) {
		
		List<String> files = new ArrayList<>();

		try (var paths = Files.walk(Paths.get(root))) {

			paths.filter(Files::isRegularFile).filter(path -> path.toString().toLowerCase().endsWith(".txt"))
					.map(Path::toString).forEach(files::add);

		} catch (IOException e) {
			e.printStackTrace();
		}

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

			objects.put(FILE_SHORT_NAME + DOCUMENT_DATETIME_SENT,
					new ReceitaFile(FILE_NAME, FILE_PATH, FILE_EXTENSION, FILE_SHORT_NAME + DOCUMENT_DATETIME_SENT,
							DOCUMENT_NAME, DOCUMENT_PERIOD_FROM, DOCUMENT_PERIOD_TO, DOCUMENT_CODE, DOCUMENT_TYPE,
							DOCUMENT_DATETIME_SENT, DOCUMENT_KEY));
		}
		
		
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
		ReceitaFile obj = null;
		Integer total = 0;
		Integer count = 0;
		
		for (ReceitaFile value : sortedMap.values()) {
			total++;
			String currentPeriod = value.DOCUMENT_PERIOD_FROM() + "_" + value.DOCUMENT_PERIOD_TO();
			Long currentDateTimeSent = Long.valueOf(value.DOCUMENT_DATETIME_SENT());
			
			if (!previousPeriod.isBlank() && !previousPeriod.equals(currentPeriod)) {
				count++;
				result.add(obj);
				mostRecentDaTeTimeSent = 0l;
			}
			
		    previousPeriod = currentPeriod;
		    
		    if (currentDateTimeSent > mostRecentDaTeTimeSent) {
		    	mostRecentDaTeTimeSent = currentDateTimeSent;
		    	obj = value;
		    }
		    
		}
		
		logger.info("Total files read ({}), and total files selected ({}).",total,count);
		
		Boolean filesNotMoved = true;
		
		for (ReceitaFile value : result) {
			if (filesNotMoved) {
				try {
					FileUtils.createDirectory(Constants.SOFTWARE_TEMP_FOLDER);
					FileUtils.emptyDirectory(Constants.SOFTWARE_TEMP_FOLDER);
					
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
