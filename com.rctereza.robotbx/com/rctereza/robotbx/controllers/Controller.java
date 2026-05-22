package com.rctereza.robotbx.controllers;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.models.ReceitaBx;
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

				if (i == 0) {
					sourceFolder = params.PASTA_ORIGEM_ARQUIVOS_BAIXADOS();
					targetFolder = params.PASTA_DESTINO_ARQUIVOS_BAIXADOS() + "\\" + params.PROCURADOR().CLIENTE();
					
					logger.info("Deleting all files/folders in the directory ({}) before the downloading starts....", sourceFolder);
					FileUtils.deleteDirectory(sourceFolder);
					
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

				logger.info("#{}/{} - {} / {} / {} [{}] [{}] After...", (i + 1), list.get().size(), updated.SISTEMA(),
						updated.TIPO_ARQUIVO(), updated.TIPO_PESQUISA(), updated.ULTIMO_PEDIDO_SOLICITADO(),
						updated.DATA_HORA_CONCLUSAO_PROCESSAMENTO());
			}
			
			logger.info("Moving all files/folders downloaded to this new location at ({})....", targetFolder);
			FileUtils.copyDirectory(sourceFolder,targetFolder);
		}

		logger.info("-------------------------------------------------------------------------------");
		logger.info("Done. It was processed [{}] item(s).", list.get().size());

	}

}
