package com.rctereza.robotbx.threads;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;
import com.rctereza.robotbx.tools.Actions;

public class ProcessExternalProgram implements Runnable {

	private static final String threadName = "[" + ProcessExternalProgram.class.getName() + "] ";

	private volatile boolean running = true;

	private final CountDownLatch finishLatch;
	private final CountDownLatch doneLatch;
	private final Actions actions;
    private final Robot robot;

	public ProcessExternalProgram(CountDownLatch finishLatch, CountDownLatch doneLatch, Robot robot)
			throws AWTException {
		this.finishLatch = finishLatch;
		this.doneLatch = doneLatch;
		this.robot = robot;
		actions = new Actions();
	}

	@Override
	public void run() {

		try {

			Thread.sleep(1000); // pause for one second

			// firstCode();
			secondCode();

			while (running && !Thread.interrupted()) {

				if (finishLatch.getCount() == 0) {
					break;
				}

				// Get pointer info
				PointerInfo pointerInfo = MouseInfo.getPointerInfo();
				Point point = pointerInfo.getLocation();

				int x = (int) point.getX();
				int y = (int) point.getY();

				System.out.println(threadName + "Mouse position: X=" + x + " Y=" + y);

				// Sleep to avoid flooding output
				Thread.sleep(2000); // pause for three seconds
			}

		} catch (InterruptedException e) {
			System.out.println(threadName + "Interrupted!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			doneLatch.countDown();
		}

		System.out.println(threadName + "Terminated!");

	}

	public void stop() {
		running = false;
	}

	//Action Result......: {Escolha=java.awt.Rectangle[x=1082,y=726,width=70,height=32]}
	
	private void secondCode() throws Exception {

		System.out.println("Running............: " + robot.NAME());

		for (RobotAction rta : robot.ROBOT_ACTIONS()) {

			System.out.println("Action.............: " + rta);

			if (rta.ENABLED()) {

				//wd.calculate(Constants.PROGRAM_NAME);
				//Map<String, Rectangle> result = ocr.extractTextFromImage(rta.SCALE(), rta.TEXT(), wd.getRectangle(), true);
				//System.out.println("Action Result......: " + result);

				for (RobotCommand rtc : rta.ROBOT_COMMANDS()) {

					System.out.println("Command............: " + rtc.ID().toString() + " - " + rtc.COMMAND().toString()
							+ " [Enabled: " + rtc.ENABLED() + "]");

					if (rtc.ENABLED()) {

						switch (rtc.COMMAND()) {

						case Command.WAIT:
							actions.Wait(1000);
							break;

						case Command.MOVE:
//							Map.Entry<String, Rectangle> entry = result.entrySet().iterator().next();
//							Rectangle rec = entry.getValue();
//							actions.Move((int) rec.getY(), (int) (rec.getY() - rec.getWidth()) + 10);
							break;

						case Command.CLICK:
							actions.Click();
							break;

						case Command.PASTE:
							actions.Paste(rtc.TEXT());
							break;

						case Command.TYPE:
							actions.Type(rtc.TEXT());
							break;

						case Command.TAB:
							actions.Tab();
							break;

						default:
							break;
						}
					}
				}
			}
		}
	}
	
//	private void getCursorCurrentLocation() {
//		Point point = MouseInfo.getPointerInfo().getLocation();
//        int x = point.x;
//        int y = point.y;
//        System.out.println("Mouse position: (" + x + ", " + y + ")");
//	}

	/*
	 * private void firstCode() {
	 * 
	 * System.out.println("Running............: " + robot.NAME());
	 * 
	 * List<RobotAction> listOfRobotActions = robot.ROBOT_ACTIONS();
	 * 
	 * for (RobotAction ra : listOfRobotActions) {
	 * 
	 * System.out.println("Action.............: " + ra.ID().toString() + " - " +
	 * ra.DESCRIPTION());
	 * 
	 * List<RobotCommand> listOfRobotCommands = ra.ROBOT_COMMANDS();
	 * 
	 * for (RobotCommand rc : listOfRobotCommands) {
	 * 
	 * if (rc.ENABLED()) {
	 * 
	 * System.out.println("Command............: " + rc.toString());
	 * 
	 * switch (rc.COMMAND()) {
	 * 
	 * case Command.WAIT: actions.Wait(); break;
	 * 
	 * case Command.MOVE: actions.Move(rc.VALUEX(), rc.VALUEY()); break;
	 * 
	 * case Command.CLICK: actions.Click(); break;
	 * 
	 * case Command.PASTE: actions.Paste(rc.TEXT()); break;
	 * 
	 * case Command.TYPE: actions.Type(rc.TEXT()); break;
	 * 
	 * case Command.TAB: actions.Tab(); break;
	 * 
	 * default: break; } } }
	 * 
	 * }
	 * 
	 * System.out.println("Stopping...........: " + robot.NAME()); }
	 */
}
