package com.rctereza.robotbx.controllers;

import java.awt.AWTException;
import java.util.concurrent.CountDownLatch;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.exceptions.InvalidScreenResolution;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.RobotText;
import com.rctereza.robotbx.threads.OpenExternalProgram;
import com.rctereza.robotbx.threads.ProcessExternalProgram;
import com.rctereza.robotbx.tools.RobotUtils;
import com.rctereza.robotbx.tools.ScreenResolution;
import com.rctereza.robotbx.tools.WindowDimensions;

public class Controller {

	private Listenable listener;
	
	public void addObjectListener(Listenable listener) {
		this.listener = listener;
	}
	
	public void startThreads(ReceitaBx params) throws AWTException, InterruptedException, InvalidScreenResolution {

		System.out.println("Starting...");

//		Robot robot = RobotUtils.getRobotBasedOnScreenResolution(params);
		RobotText robotTexts = RobotUtils.getRobotTexts(params);

//		if (robot.NAME() == null) {
//			throw new InvalidScreenResolution("ERROR!!! There's no robot configured for the Screen resolution ["
//					+ ScreenResolution.getResolution() + "].");
//		}

		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch finishLatch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(2);

		OpenExternalProgram t1Runnable = new OpenExternalProgram(startLatch, finishLatch, doneLatch);
		//ProcessExternalProgram t2Runnable = new ProcessExternalProgram(finishLatch, doneLatch, robot);
		ProcessExternalProgram t2Runnable = new ProcessExternalProgram(finishLatch, doneLatch, robotTexts);

		Thread t1 = new Thread(t1Runnable, "T1-ProgramLauncher");
		Thread t2 = new Thread(t2Runnable, "T2-RobotLauncher");

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			//System.out.println("Shutdown hook triggered...");
			t1Runnable.stop();
			t2Runnable.stop();
			t1.interrupt();
			t2.interrupt();
		}));

		// t1.setDaemon(true);
		t1.start();

		startLatch.await();

		WindowDimensions wd = new WindowDimensions(Constants.PROGRAM_NAME);

		System.out.println(
				"--------------------------------------------------------------------------------------------------------------------");
		System.out.println("Processing record..: #" + params.CERTIFICADO().ID());
		System.out.println("Screen resolution..: Width:" + ScreenResolution.getWidth() + "px, Height:"
				+ ScreenResolution.getHeight() + "px");
		System.out.println("Window dimensions..: Width:" + wd.getDimension().getWidth() + "px, Height:" + wd.getDimension().getHeight() + "px");
		System.out.println("Processing cutomer.: " + params.CERTIFICADO().CUSTOMER());
		System.out.println("Parameters.........: " + params);

		// t2.setDaemon(true);
		t2.start();
		
		new Thread(() -> {
		    try {
		        doneLatch.await();
		        listener.value(Menu.DONE.getValue());
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		}).start();
	}
}
