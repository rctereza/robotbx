package com.rctereza.robotbx.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

import com.rctereza.robotbx.Constants;

public class OpenExternalProgram implements Runnable {

	private static final String threadName = "[" + OpenExternalProgram.class.getName() + "] ";

	private volatile boolean running = true;

	private Process process;

	private final CountDownLatch startLatch;
	private final CountDownLatch finishLatch;
	private final CountDownLatch doneLatch;

	public OpenExternalProgram(CountDownLatch startLatch, CountDownLatch finishLatch, CountDownLatch doneLatch) {
		this.startLatch = startLatch;
		this.finishLatch = finishLatch;
		this.doneLatch = doneLatch;
	}

	@Override
	public void run() {

		System.out.println(threadName + "Starting...");

		//ProcessBuilder processBuilder = new ProcessBuilder("java", "--add-exports","jdk.crypto.mscapi/sun.security.mscapi=ALL-UNNAMED", "-jar", Constants.PROGRAM_COMMAND);
		// processBuilder.inheritIO();

		ProcessBuilder processBuilder = new ProcessBuilder("java","-jar",Constants.PROGRAM_COMMAND);
		processBuilder.directory(new java.io.File(Constants.PROGRAM_PATH)); // set the working directory

		try {
			process = processBuilder.start();

			readStream(process.getInputStream(), threadName + "OUT: ");
			//readStream(process.getErrorStream(), threadName + "ERR: ");

			startLatch.countDown();

			// ✅ THIS IS WHERE YOUR LOOP GOES
			while (process.isAlive()) {
				Thread.sleep(1000);

				if (!running) {
					System.out.println(threadName + "Stopping process...");
					process.destroy();
					break;
				}
			}

			int exitCode = process.waitFor();

			System.out.println(threadName + "Program exited with code: " + exitCode);

			finishLatch.countDown();

		} catch (InterruptedException e) {
			System.out.println(threadName + "Interrupted!");

			if (process != null && process.isAlive()) {
				process.destroy();
			}

			Thread.currentThread().interrupt(); // restore interrupt flag

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			doneLatch.countDown();
			if (process != null && process.isAlive()) {
				process.destroyForcibly();
			}
		}

		System.out.println(threadName + "Terminated!");
	}

	public void stop() {
		running = false;
	}

	private void readStream(InputStream stream, String prefix) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line = reader.readLine();
			System.out.println(prefix + line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
