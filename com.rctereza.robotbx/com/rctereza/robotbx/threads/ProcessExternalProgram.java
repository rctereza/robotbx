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
import com.rctereza.robotbx.tools.Actions;

public class ProcessExternalProgram implements Runnable {

	private static final String threadName = "[" + ProcessExternalProgram.class.getName() + "] ";

	private final CountDownLatch finishLatch;
	private final Actions actions;
	private final Robot robot;

	public ProcessExternalProgram(CountDownLatch finishLatch, Robot robot) throws AWTException {
		this.finishLatch = finishLatch;
		this.robot = robot;
		actions = new Actions();
	}

	@Override
	public void run() {

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
		
		while (true) {

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
			try {
				Thread.sleep(3000); // pause for three seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println(threadName + "Program terminated.");

	}

}
