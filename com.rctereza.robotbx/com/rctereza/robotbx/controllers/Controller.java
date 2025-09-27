package com.rctereza.robotbx.controllers;

import java.awt.AWTException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.exceptions.InvalidScreenResolution;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.threads.OpenExternalProgram;
import com.rctereza.robotbx.threads.ProcessExternalProgram;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.RobotUtils;
import com.rctereza.robotbx.tools.ScreenResolution;
import com.rctereza.robotbx.tools.WindowDimensions;

public class Controller {

	public void startThreads(ReceitaBx params) throws AWTException, InterruptedException, InvalidScreenResolution {

		System.out.println("Starting...");
		
		Robot robot = RobotUtils.getRobotBasedOnScreenResolution(params.CERTIFICADO(), params.SCREEN());
		
		if (robot.NAME() == null) {
			throw new InvalidScreenResolution("ERROR!!! There's no robot configured for the Screen resolution [" + ScreenResolution.getResolution() + "].");
		}
			
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch finishLatch = new CountDownLatch(1);
		
		Thread t1 = new Thread(new OpenExternalProgram(startLatch, finishLatch));
		Thread t2 = new Thread(new ProcessExternalProgram(finishLatch, params, robot));

		t1.start();

		startLatch.await();
		
		WindowDimensions wd = new WindowDimensions(Constants.PROGRAM_NAME);
		
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		System.out.println("Processing record..: #" + params.CERTIFICADO().ID());
		System.out.println("Screen resolution..: Width:" + ScreenResolution.getWidth() + "px, Height:" + ScreenResolution.getHeight() + "px");
		System.out.println("Window dimensions..: Width:" + wd.getWidth() + "px, Height:" + wd.getHeight() + "px");			
		System.out.println("Processing cutomer.: " + params.CERTIFICADO().CUSTOMER());
		
		t2.start();
		
		t2.join();
			
		System.out.println("Done!");

	}

	public void startThreads() throws AWTException, InterruptedException {

		System.out.println("Starting...");
		
		List<Certificate> list = FileUtils.getListOfCertificates();
		
		for (Certificate certificate : list) {
			
			CountDownLatch startLatch = new CountDownLatch(1);
			CountDownLatch finishLatch = new CountDownLatch(1);
			
			Robot robot = RobotUtils.getRobotBasedOnScreenResolution(certificate, ScreenResolution.getResolution());
			
			if (robot.NAME() == null) {
				System.out.println("ERROR!!! There's no robot configured for the Screen resolution [" + ScreenResolution.getResolution() + "].");
				break;
			}
			
			Thread t1 = new Thread(new OpenExternalProgram(startLatch, finishLatch));
			Thread t2 = new Thread(new ProcessExternalProgram(finishLatch, null, robot));

			t1.start();

			startLatch.await();
			
			WindowDimensions wd = new WindowDimensions(Constants.PROGRAM_NAME);
			
			System.out.println("--------------------------------------------------------------------------------------------------------------------");
			System.out.println("Processing record..: #" + certificate.ID() + "/" + list.size());
			System.out.println("Screen resolution..: Width:" + ScreenResolution.getWidth() + "px, Height:" + ScreenResolution.getHeight() + "px");
			System.out.println("Window dimensions..: Width:" + wd.getWidth() + "px, Height:" + wd.getHeight() + "px");			
			System.out.println("Processing cutomer.: " + certificate.CUSTOMER());
			
			t2.start();
			
			t2.join();
			
			break;
			
		}

		System.out.println("Done!");

	}
	
}
