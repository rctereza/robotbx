package com.rctereza.robotbx.threads;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;
import com.rctereza.robotbx.models.RobotTexts;
import com.rctereza.robotbx.tools.Actions;

public class ProcessExternalProgram implements Runnable {

	private static final String threadName = "[" + ProcessExternalProgram.class.getName() + "] ";

	private volatile boolean running = true;

	private final CountDownLatch finishLatch;
	private final CountDownLatch doneLatch;
	private final Actions actions;
	private final RobotTexts robotTexts;
	private final Robot robot;

	public ProcessExternalProgram(CountDownLatch finishLatch, CountDownLatch doneLatch, RobotTexts robotTexts)
			throws AWTException {
		this.finishLatch = finishLatch;
		this.doneLatch = doneLatch;
		this.robotTexts = robotTexts;
		actions = new Actions();
		this.robot = new Robot();
	}

	public ProcessExternalProgram(CountDownLatch finishLatch, CountDownLatch doneLatch, Robot robot)
			throws AWTException {
		this.finishLatch = finishLatch;
		this.doneLatch = doneLatch;
		this.robot = robot;
		actions = new Actions();
		this.robotTexts = new RobotTexts();
	}

	@Override
	public void run() {

		try {

			Thread.sleep(1000); // pause for one second

			//firstCode();
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
		} finally {
			doneLatch.countDown();
		}

		System.out.println(threadName + "Terminated!");

	}

	public void stop() {
		running = false;
	}

	private void secondCode() {

		System.out.println("Running............: " + robotTexts.NAME());

		for (Integer i : robotTexts.TEXTS().keySet()) {
			System.out.println("key: " + i + " value: " + robotTexts.TEXTS().get(i));
			//chamar method que vai tirar print screen e localizar a posição do texto 
		}
	}

	private void firstCode() {

		System.out.println("Running............: " + robot.NAME());

		List<RobotAction> listOfRobotActions = robot.ROBOT_ACTIONS();

		for (RobotAction ra : listOfRobotActions) {

			System.out.println("Action.............: " + ra.ID().toString() + " - " + ra.DESCRIPTION());

			List<RobotCommand> listOfRobotCommands = ra.ROBOT_COMMANDS();

			for (RobotCommand rc : listOfRobotCommands) {

				if (rc.ENABLED()) {

					System.out.println("Command............: " + rc.toString());

					switch (rc.COMMAND()) {

					case Command.WAIT:
						actions.Wait();
						break;

					case Command.MOVE:
						actions.Move(rc.VALUEX(), rc.VALUEY());
						break;

					case Command.CLICK:
						actions.Click();
						break;

					case Command.PASTE:
						actions.Paste(rc.TEXT());
						break;

					case Command.TYPE:
						actions.Type(rc.TEXT());
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

		System.out.println("Stopping...........: " + robot.NAME());
	}
}
