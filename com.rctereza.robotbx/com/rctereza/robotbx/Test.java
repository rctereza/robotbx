package com.rctereza.robotbx;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;

import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;
import com.rctereza.robotbx.tools.Actions;
import com.rctereza.robotbx.tools.CryptoUtils;
import com.rctereza.robotbx.tools.RobotUtils;

public class Test {

	public static void main(String[] args) throws InvalidKeyException, ClassNotFoundException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, IOException, InterruptedException, AWTException {

		System.out.println("Starting...");

		// *******************************************************************************************
		// OPEN RECEITANETBX
		// *******************************************************************************************
		ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", Constants.PROGRAM_COMMAND);
		processBuilder.directory(new java.io.File(Constants.PROGRAM_PATH));
		Process process = processBuilder.start();
		readStream(process.getInputStream(), "OUT: ");

		// *******************************************************************************************
		// LOAD ROBOT PARAMENTERS
		// *******************************************************************************************
		ReceitaBx receitaBx = (ReceitaBx) CryptoUtils.loadEncryptedGCM(Constants.SOFTWARE_SECRET,
				Constants.SOFTWARE_SECURE_FILE);
		System.out.println("Paramenters........: " + receitaBx);

		Robot robot = RobotUtils.getRobotBasedOnScreenResolution(receitaBx);
		System.out.println("Running............: " + robot.NAME());

		// *******************************************************************************************
		// EXECUTE THE ACTIONS
		// *******************************************************************************************
		Actions actions = new Actions();
		
		actions.Wait();
		
		for (RobotAction ra : robot.ROBOT_ACTIONS()) {

			System.out.println("Action.............: " + ra.toString());

			for (RobotCommand rc : ra.ROBOT_COMMANDS()) {

				if (rc.ENABLED()) {

					System.out.println("Command............: " + rc.toString());
					
					switch (rc.COMMAND()) {

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

			// Get pointer info
			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
			Point point = pointerInfo.getLocation();

			int x = (int) point.getX();
			int y = (int) point.getY();

			System.out.println("Mouse position: X=" + x + " Y=" + y);

			Thread.sleep(3000); // pause for three seconds
		}

	}

	private static void readStream(InputStream stream, String prefix) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line = reader.readLine();
			System.out.println(prefix + line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
