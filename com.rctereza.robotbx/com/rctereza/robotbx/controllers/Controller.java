package com.rctereza.robotbx.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.threads.ProcessRobot;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.wrappers.Ref;

public class Controller {

	public Map<ReceitaBx, String> startRobot(Ref<List<ReceitaBx>> list, boolean reset) {

		Map<ReceitaBx, String> resultList = new HashMap<>();

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
					resultList.put(params.get(), result);

				}

				System.out.println("#" + (i + 1) + "/" + list.get().size() + " - " + params.get().SISTEMA() + " ["
						+ params.get().ULTIMO_PEDIDO_SOLICITADO() + "] ["
						+ params.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO() + "] After...");
				System.out.println("-------------------------------------------------------------------------------");
			}
		}

		return resultList;
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
