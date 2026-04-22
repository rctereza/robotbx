package com.rctereza.robotbx;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.enums.Message;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;
import com.rctereza.robotbx.models.RobotMessageBox;
import com.rctereza.robotbx.mutables.Ref;
import com.rctereza.robotbx.tools.Actions;
import com.rctereza.robotbx.tools.AutoCloseMessageDialog;
import com.rctereza.robotbx.tools.CryptoUtils;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.RobotUtils;
import com.rctereza.robotocr.MessageBox2;

public class Test {

	private static boolean DEBUG_ON = false;

	private static boolean RUNNING = true;

	private static int START_ACTIONS_AT = 0;

	private static int NUMBER_OF_ATTEMPTS = 0;

	// private static boolean PROCESS_FULLY_CONCLUDED = false;

	public static void main(String[] args) throws Exception {

		System.out.println("Starting...");

		System.setProperty("sun.java2d.uiScale", "1.0");

		// *******************************************************************************************
		// LOAD RECEINATNETBX DATA TO BE PROCESS
		// *******************************************************************************************
		Ref<ReceitaBx> receitaBx = CryptoUtils.loadRef(Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE,
				ReceitaBx.class, ReceitaBx::new);
		System.out.println("Paramenters........: " + receitaBx);

		if (!receitaBx.get().ULTIMO_PEDIDO_SOLICITADO().equals("")
				&& !receitaBx.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO().equals("")) {
			reset(receitaBx);
			System.out.println("Paramenters reseted: " + receitaBx);
		}

		if (!DEBUG_ON) {

			while (true) {

				if ((receitaBx.get().ULTIMO_PEDIDO_SOLICITADO().equals(""))
						|| (!receitaBx.get().ULTIMO_PEDIDO_SOLICITADO().equals("")
								&& receitaBx.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO().equals(""))) {

					RUNNING = true;
					
					System.out.println("Starting Step1.....: ");
					startProcessStep1(receitaBx);
					System.out.println("Stopping Step1.....: [" + receitaBx.get().ULTIMO_PEDIDO_SOLICITADO() + "]");
					
					Thread.sleep(5000); // pause for five seconds

				} else if (!receitaBx.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO().equals("")) {

					System.out.println(
							"Starting Step2.....: [" + receitaBx.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO() + "]");
					startProcessStep2(receitaBx);
					System.out.println("Stopping Step2.....: ");
					break;
				}
			}

		} else {

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
	}

	private static void startProcessStep2(Ref<ReceitaBx> receitaBx) throws Exception {

		// *******************************************************************************************
		// OPEN RECEITANETBX EXE
		// *******************************************************************************************
		ProcessBuilder processBuilder = new ProcessBuilder(Constants.PROGRAM_COMMAND2);
		processBuilder.directory(new java.io.File(Constants.PROGRAM_PATH));
		Process process = processBuilder.start();
		readStream(process.getInputStream(), "OUT: ");

		// *******************************************************************************************
		// LOAD ROBOT PARAMENTERS
		// *******************************************************************************************
		Robot robot = RobotUtils.getRobotBasedOnScreenResolution2(receitaBx.get());
		System.out.println("Starting robot.....: " + robot.NAME());

		// *******************************************************************************************
		// EXECUTE THE ACTIONS
		// *******************************************************************************************
		performAction(robot, receitaBx);

		System.out.println("Stopping robot.....: " + robot.NAME());

		process.destroy();

		Thread.sleep(5000); // pause for three seconds
		
		if (process != null && process.isAlive()) {
			System.out.println("destroyForcibly");
			process.destroyForcibly();
		}
		
		// ******************************************************************************************
//		while (true) {
//			MessageBox2 mb = new MessageBox2(Constants.PROGRAM_NAME);
//			String text = mb.getText();
//			System.out.println("Message box text found..: [" + text + "]");
//
//			if (text.contains(receitaBx.get().ULTIMO_PEDIDO_SOLICITADO())) {
//				// It means the downloading has not concluded yet!
//				Thread.sleep(5000); // pause for five seconds
//			} else if (text.contains("Não ha arquivos na fila de download")) {
//				AutoCloseMessageDialog.show("A extração automática dos arquivos foi concluída com sucesso!",
//						"Informação", 3000);
//				Actions actions = new Actions();
//				actions.Move(1147, 297); // Botão Sair
//				actions.Click();
//			}
//		}
		// ******************************************************************************************

//		while (true) {
//			// Get pointer info
//			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
//			Point point = pointerInfo.getLocation();
//
//			int x = (int) point.getX();
//			int y = (int) point.getY();
//
//			System.out.println("Mouse position: X=" + x + " Y=" + y);
//
//			Thread.sleep(3000); // pause for three seconds
//		}
		
	}

	private static void startProcessStep1(Ref<ReceitaBx> receitaBx) throws Exception {

		// *******************************************************************************************
		// OPEN RECEITANETBX JAR
		// *******************************************************************************************
		ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", Constants.PROGRAM_COMMAND);
		processBuilder.directory(new java.io.File(Constants.PROGRAM_PATH));
		Process process = processBuilder.start();
		readStream(process.getInputStream(), "OUT: ");

		// *******************************************************************************************
		// LOAD ROBOT PARAMENTERS
		// *******************************************************************************************
		Robot robot = RobotUtils.getRobotBasedOnScreenResolution(receitaBx.get());
		System.out.println("Starting robot.....: " + robot.NAME());

		// *******************************************************************************************
		// EXECUTE THE ACTIONS
		// *******************************************************************************************
		performAction(robot, receitaBx);

		System.out.println("Stopping robot.....: " + robot.NAME());

		process.destroy();
		
	}

	private static void performAction(Robot robot, Ref<ReceitaBx> receitaBx) throws Exception {

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
							if ((START_ACTIONS_AT == 0) || (ra.CONFIRMATON())) {
								if (ra.CONFIRMATON() && START_ACTIONS_AT > 0) {
									START_ACTIONS_AT = 0;
								}
								actions.Move(rc.VALUEX(), rc.VALUEY());
							}
							break;

						case Command.CLICK:
							if ((START_ACTIONS_AT == 0) || (ra.CONFIRMATON())) {
								if (ra.CONFIRMATON() && START_ACTIONS_AT > 0) {
									START_ACTIONS_AT = 0;
								}
								actions.Click();
							}
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
					Message mtype = Message.NONE;

					for (RobotMessageBox rmg : ra.MESSAGEBOX_TEXTS()) {

						if (rmg.TYPE() == Message.VALIDATION) {

							mtype = Message.VALIDATION;

							if (text.trim().equals(rmg.MESSAGE())) {

								JOptionPane.showMessageDialog(null, rmg.RESPONSE(), "Atenção",
										JOptionPane.WARNING_MESSAGE);

								if (rmg.ABORT()) {
									RUNNING = false;
								}

								break;
							}
						} else if (rmg.TYPE() == Message.WARNING) {

							mtype = Message.WARNING;

							if (text.contains(rmg.MESSAGE())) {

								JOptionPane.showMessageDialog(null, rmg.RESPONSE(), "Atenção",
										JOptionPane.WARNING_MESSAGE);

								if (rmg.ABORT()) {
									RUNNING = false;
								}

								break;
							}
						} else if (rmg.TYPE() == Message.WAITING) {

							mtype = Message.WAITING;

							if (text.contains(rmg.MESSAGE())) {

								found = true;

								if (ra.WAIT()) {

									NUMBER_OF_ATTEMPTS++;

									if (NUMBER_OF_ATTEMPTS <= ra.NUMBER_OF_ATTEMPTS()) {

										String counter = "#" + String.valueOf(NUMBER_OF_ATTEMPTS) + "/"
												+ ra.NUMBER_OF_ATTEMPTS().toString();

										System.out.println(counter + " - Waiting for this action [" + ra.DESCRIPTION()
												+ "] to be checked again...");

										actions.Wait(ra.WAIT_MILLISECONDS());

										START_ACTIONS_AT = ra.ID();
										i = 0;
										continue;

									} else {
										
										AutoCloseMessageDialog.show(rmg.RESPONSE(), "Atenção", 5000);

										if (rmg.ABORT()) {
											START_ACTIONS_AT = 0;
											NUMBER_OF_ATTEMPTS = 0;
											RUNNING = false;
										}

									}
								} else {
									System.out.println("*** ATTENTION *** This action [" + ra.DESCRIPTION()
											+ "] is not setting to WAIT [" + ra.WAIT()
											+ "] and it's using a message set to WAIT");
								}

								break;
							}
						} else if (rmg.TYPE() == Message.CONFIRMATION) {

							mtype = Message.CONFIRMATION;

							if (text.contains(rmg.MESSAGE())) {

								int position = text.indexOf(rmg.MESSAGE()) + rmg.MESSAGE().length() + 1;

								String order = text.substring(position);

								order = order.substring(0, order.indexOf(" "));

								saveOrderNumber(receitaBx, order);

								RobotAction robotAction = new RobotAction(ra.ID(),
										ra.DESCRIPTION() + " - Confirmando pedido [" + order + "]", false, null, false,
										0, 0, true, false, true, rmg.COMMANDS());

								robot.ROBOT_ACTIONS().set(i, robotAction);

								START_ACTIONS_AT = ra.ID();
								NUMBER_OF_ATTEMPTS = 0;
								i = 0;
								continue;
							}
						}
					}

					if (!found && mtype == Message.WAITING) {
						NUMBER_OF_ATTEMPTS = 0;
						START_ACTIONS_AT = 0;
					}

					if (!RUNNING) {
						break;
					}
				}
			}

			if (ra.LAST_ACTION()) {
				// PROCESS_FULLY_CONCLUDED = true;
				saveDateTimeOfConclusion(receitaBx);
			}
		}
	}

	private static void readStream(InputStream stream, String prefix) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line = reader.readLine();
			System.out.println(prefix + line);
		}
	}

	private static void saveOrderNumber(Ref<ReceitaBx> receitaBx, String orderNumber)
			throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException, IOException {

		receitaBx.set(new ReceitaBx(receitaBx.get().SCREEN(), receitaBx.get().CERTIFICADO(), 
				receitaBx.get().PERFIL(), receitaBx.get().PERFIL_TYPE(), receitaBx.get().PERFIL_VALUE(),
				receitaBx.get().SISTEMA(), receitaBx.get().TIPO_ARQUIVO(), receitaBx.get().TIPO_PESQUISA(),
				receitaBx.get().DATA_INICIO(), receitaBx.get().DATA_FIM(), receitaBx.get().CNPJ_INCORPORADORA(),
				receitaBx.get().TIPO_EVENTO(), receitaBx.get().BAIXAR_ARQUIVO_ASSINADO(),
				receitaBx.get().CNPJ_ESTABELECIMENTO(), receitaBx.get().BUSCAR_TODOS_ESTABLECIMENTOS(),
				receitaBx.get().INSCRICAO_ESTADUAL(), receitaBx.get().ULTIMO_ARQUIVO_TRANSMITIDO(), orderNumber, null));

		CryptoUtils.saveEncryptedGCM(receitaBx.get(), Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE);
	}

	private static void saveDateTimeOfConclusion(Ref<ReceitaBx> receitaBx)
			throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException, IOException {

		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String DATA_HORA_CONCLUSAO_PROCESSAMENTO = currentDateTime.format(formatter);

		receitaBx.set(new ReceitaBx(receitaBx.get().SCREEN(), receitaBx.get().CERTIFICADO(), 
				receitaBx.get().PERFIL(), receitaBx.get().PERFIL_TYPE(), receitaBx.get().PERFIL_VALUE(),
				receitaBx.get().SISTEMA(), receitaBx.get().TIPO_ARQUIVO(), receitaBx.get().TIPO_PESQUISA(),
				receitaBx.get().DATA_INICIO(), receitaBx.get().DATA_FIM(), receitaBx.get().CNPJ_INCORPORADORA(),
				receitaBx.get().TIPO_EVENTO(), receitaBx.get().BAIXAR_ARQUIVO_ASSINADO(),
				receitaBx.get().CNPJ_ESTABELECIMENTO(), receitaBx.get().BUSCAR_TODOS_ESTABLECIMENTOS(),
				receitaBx.get().INSCRICAO_ESTADUAL(), receitaBx.get().ULTIMO_ARQUIVO_TRANSMITIDO(),
				receitaBx.get().ULTIMO_PEDIDO_SOLICITADO(), DATA_HORA_CONCLUSAO_PROCESSAMENTO));

		CryptoUtils.saveEncryptedGCM(receitaBx.get(), Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE);
	}

	private static void reset(Ref<ReceitaBx> receitaBx) throws InvalidKeyException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IOException {

		receitaBx.set(new ReceitaBx(receitaBx.get().SCREEN(), receitaBx.get().CERTIFICADO(), 
				receitaBx.get().PERFIL(), receitaBx.get().PERFIL_TYPE(), receitaBx.get().PERFIL_VALUE(),
				receitaBx.get().SISTEMA(), receitaBx.get().TIPO_ARQUIVO(), receitaBx.get().TIPO_PESQUISA(),
				receitaBx.get().DATA_INICIO(), receitaBx.get().DATA_FIM(), receitaBx.get().CNPJ_INCORPORADORA(),
				receitaBx.get().TIPO_EVENTO(), receitaBx.get().BAIXAR_ARQUIVO_ASSINADO(),
				receitaBx.get().CNPJ_ESTABELECIMENTO(), receitaBx.get().BUSCAR_TODOS_ESTABLECIMENTOS(),
				receitaBx.get().INSCRICAO_ESTADUAL(), receitaBx.get().ULTIMO_ARQUIVO_TRANSMITIDO(), "", ""));

		CryptoUtils.saveEncryptedGCM(receitaBx.get(), Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE);
		
		clean();
	}
	
	private static void clean() throws IOException {
		Path folderPath = Paths.get("C:\\Temp\\ReceitanetBX"); // Change to your folder path
		FileUtils.clearDirectory(folderPath);
	}

}
