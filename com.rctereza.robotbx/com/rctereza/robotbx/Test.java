package com.rctereza.robotbx;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.enums.Message;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;
import com.rctereza.robotbx.models.RobotMessageBox;
import com.rctereza.robotbx.tools.Actions;
import com.rctereza.robotbx.tools.CryptoUtils;
import com.rctereza.robotbx.tools.RobotUtils;
import com.rctereza.robotocr.MessageBox2;

public class Test {

	private static boolean running = true;

	private static int START_ACTIONS_AT = 0;
	private static int NUMBER_OF_ATTEMPTS = 0;

	public static void main(String[] args) throws Exception {

		System.out.println("Starting...");
		
		System.setProperty("sun.java2d.uiScale", "1.0");

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

		for (int i = 0; i < robot.ROBOT_ACTIONS().size(); i++) {

			RobotAction ra = robot.ROBOT_ACTIONS().get(i);

			System.out.println("Action.............: " + ra.toString());

			if ((ra.ENABLED()) && (ra.ID() >= START_ACTIONS_AT)) {

				for (RobotCommand rc : ra.ROBOT_COMMANDS()) {

					if (rc.ENABLED()) {

						System.out.println("Command............: " + rc.toString());
						
						switch (rc.COMMAND()) {

						case Command.WAIT:
							actions.Wait(rc.WAITMS());
							break;
							
						case Command.MOVE:
							if (START_ACTIONS_AT == 0) { actions.Move(rc.VALUEX(), rc.VALUEY()); }
							break;

						case Command.CLICK:
							if (START_ACTIONS_AT == 0) { actions.Click(); }
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

						case Command.ENTER:
							actions.Enter();
							break;

						case Command.SPACEBAR:
							actions.SpaceBar();
							break;

						case Command.ALTARROWDOWN:
							actions.AltArrowDown();
							break;

						case Command.ARROWDOWN:
							actions.ArrowDown();
							break;

						case Command.ARROWUP:
							actions.ArrowUp();
							break;

						default:
							break;
						}
					}
				}

				if (ra.MESSAGEBOX()) {
					
					System.out.println("Checking if there's a message box...");
					MessageBox2 mb = new MessageBox2(Constants.PROGRAM_NAME);
					String text = mb.getText();
					System.out.println("Message box text found..: [" + text + "]");
					
					Boolean found = false;
					
					for (RobotMessageBox rmg : ra.MESSAGEBOX_TEXTS()) {
						
						if (rmg.TYPE() == Message.VALIDATION) {
							
							if (text.trim().equals(rmg.MESSAGE())) {
								
								JOptionPane.showMessageDialog(null, rmg.RESPONSE(), "Atenção", JOptionPane.WARNING_MESSAGE);
								
								if (rmg.ABORT()) {
									running = false;
								}
								
								break;
							}
						}
						else if (rmg.TYPE() == Message.WARNING) {
							
							if (text.contains(rmg.MESSAGE())) {
								 
								JOptionPane.showMessageDialog(null, rmg.RESPONSE(), "Atenção", JOptionPane.WARNING_MESSAGE);
								
								if (rmg.ABORT()) {
									running = false;
								}
								
								break;
							}
						}
						else if (rmg.TYPE() == Message.WAITING) {
							
							if (text.contains(rmg.MESSAGE())) {
							
								found = true;
							
								if (ra.WAIT()) {
									
									NUMBER_OF_ATTEMPTS++;
									
									if (NUMBER_OF_ATTEMPTS <= ra.NUMBER_OF_ATTEMPTS()) {
										
										String counter = "#" + String.valueOf(NUMBER_OF_ATTEMPTS) + "/" + ra.NUMBER_OF_ATTEMPTS().toString();
										
										System.out.println(counter + " - Waiting for this action [" + ra.DESCRIPTION() + "] to be checked again...");
										
										actions.Wait(ra.WAIT_MILLISECONDS());
										
										START_ACTIONS_AT = ra.ID();
										i = 0;
										continue;
										
									} else {
										
										JOptionPane.showMessageDialog(null, rmg.RESPONSE(), "Atenção", JOptionPane.WARNING_MESSAGE);
										
										if (rmg.ABORT()) {
											running = false;
										}
										
									}
								}
								else {
									System.out.println("*** ATTENTION *** This action [" + ra.DESCRIPTION() + "] is not setting to WAIT [" + ra.WAIT() + "] and it's using a message set to WAIT");
								}
								
								break;
							}
						}
						else if (rmg.TYPE() == Message.CONFIRMATION) {
							
							if (text.contains(rmg.MESSAGE())) {
								
								
								
							}
						}
					}
					
					if (!found && ra.WAIT()) {
						NUMBER_OF_ATTEMPTS = 0;
						START_ACTIONS_AT = 0;
					}

					if (!running) {
						break;
					}
				}
			}
		}

		System.out.println("Stopping...........: " + robot.NAME());

		while (running) {

			// Get pointer info
			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
			Point point = pointerInfo.getLocation();

			int x = (int) point.getX();
			int y = (int) point.getY();

			System.out.println("Mouse position: X=" + x + " Y=" + y);

			Thread.sleep(3000); // pause for three seconds
		}

		process.destroy();

	}

	private static void readStream(InputStream stream, String prefix) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line = reader.readLine();
			System.out.println(prefix + line);
		} 
	}

}
