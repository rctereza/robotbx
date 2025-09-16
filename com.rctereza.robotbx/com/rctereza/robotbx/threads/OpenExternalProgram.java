package com.rctereza.robotbx.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

import com.rctereza.robotbx.Constants;

public class OpenExternalProgram implements Runnable  {

	private static final String threadName = "[" + OpenExternalProgram.class.getName() + "] ";

	private final CountDownLatch startLatch;
	private final CountDownLatch finishLatch;
	
	public OpenExternalProgram(CountDownLatch startLatch, CountDownLatch finishLatch) {
		this.startLatch = startLatch;
		this.finishLatch = finishLatch;
	}

	@Override
	public void run() {

		System.out.println(threadName + "Starting...");

		ProcessBuilder processBuilder = new ProcessBuilder(Constants.PROGRAM_COMMAND);
		processBuilder.directory(new java.io.File(Constants.PROGRAM_PATH)); // set the working directory

		try {
			Process process = processBuilder.start();

			// Read output from the command
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s = "";
			System.out.print(threadName + "Standard Output: ");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
				startLatch.countDown();
			}

			// Read any errors from the command
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			System.out.print(threadName + "Standard Error (if any): ");
			while ((s = stdError.readLine()) != null) {
				System.out.println(threadName + s);
			}

			int exitCode = process.waitFor();

			System.out.println(threadName + "Program exited with code: " + exitCode);
			
			finishLatch.countDown();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
