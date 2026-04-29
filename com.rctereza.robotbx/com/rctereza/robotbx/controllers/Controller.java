package com.rctereza.robotbx.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.threads.ProcessRobot;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.wrappers.Ref;

public class Controller {

	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	public void startRobot(Ref<List<ReceitaBx>> list) throws InterruptedException, ExecutionException {

		try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {

			logger.info("Starting...");

			cleanDirectory(); // delete all files/folders in the directory where the files will be downloaded.

			for (int i = 0; i < list.get().size(); i++) {

				if (i > 0) {
					logger.info("Waiting 5 seconds before starting to process the next item...");
					Thread.sleep(5000);
				}

				ReceitaBx params = list.get().get(i);

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
		}

		logger.info("-------------------------------------------------------------------------------");
		logger.info("Done. It was processed [{}] item(s).", list.get().size());

	}

	private void cleanDirectory() {
		Path folderPath = Paths.get(Constants.PROGRAM_DOWNLOADED_FOLDER); // Change to your folder path
		try {
			FileUtils.clearDirectory(folderPath);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
