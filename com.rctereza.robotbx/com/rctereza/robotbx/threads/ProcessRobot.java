package com.rctereza.robotbx.threads;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.enums.Message;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;
import com.rctereza.robotbx.models.RobotMessageBox;
import com.rctereza.robotbx.tools.Actions;
import com.rctereza.robotbx.tools.RobotUtils;
import com.rctereza.robotbx.wrappers.Ref;
import com.rctereza.robotocr.MessageBox2;

public class ProcessRobot implements Callable<String> {

	private Ref<ReceitaBx> receitaBx;

	public ProcessRobot(Ref<ReceitaBx> receitaBx, boolean reset) {
		this.receitaBx = receitaBx;
		if (reset) {
			reset();
		}
	}

	@Override
	public String call() {

		String result = "";

		System.out.println("Thread Starting...");

		ProcessBuilder processBuilder = new ProcessBuilder("java", "--add-exports",
				"jdk.crypto.mscapi/sun.security.mscapi=ALL-UNNAMED", "-jar", Constants.PROGRAM_COMMAND);
		processBuilder.directory(new java.io.File(Constants.PROGRAM_PATH));
		processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
		Process process = null;

		try {
			// *******************************************************************************************
			// START RECEITANETBX JAR
			// *******************************************************************************************
			process = processBuilder.start();

			// *******************************************************************************************
			// GET ROBOT PARAMETERS
			// *******************************************************************************************
			Robot robot = RobotUtils.getRobotBasedOnScreenResolution(receitaBx.get());

			// *******************************************************************************************
			// PERFORM THE ACTIONS
			// *******************************************************************************************
			result = performActions(robot, receitaBx);

		} catch (Exception e) {
			result = "ERROR: [" + e.getMessage() + "]";

		} finally {
			if (process != null) {
				process.descendants().forEach(p -> p.destroy());
				process.destroy();
			}
		}

		System.out.println("Thread Terminated!");

		return result;
	}

	private String performActions(Robot robot, Ref<ReceitaBx> receitaBx) throws Exception {

		String result = "";

		boolean RUNNING = true;

		int START_ACTIONS_AT = 0;

		int NUMBER_OF_ATTEMPTS = 0;

		Thread.sleep(6000); // pause for six seconds

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

								result = "ATTENTION: " + rmg.RESPONSE();
								System.out.println(result);

								// JOptionPane.showMessageDialog(null, rmg.RESPONSE(), "Atenção",
								// JOptionPane.WARNING_MESSAGE);

								if (rmg.ABORT()) {
									RUNNING = false;
								}

								break;
							}
						} else if (rmg.TYPE() == Message.WARNING) {

							mtype = Message.WARNING;

							if (text.contains(rmg.MESSAGE())) {

								result = "ATTENTION: " + rmg.RESPONSE();
								System.out.println(result);

								// JOptionPane.showMessageDialog(null, rmg.RESPONSE(), "Atenção",
								// JOptionPane.WARNING_MESSAGE);

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

										result = "ATTENTION: " + rmg.RESPONSE();
										System.out.println(result);
										
										// AutoCloseMessageDialog.show(rmg.RESPONSE(), "Atenção", 5000);

										if (rmg.ABORT()) {
											START_ACTIONS_AT = 0;
											NUMBER_OF_ATTEMPTS = 0;
											RUNNING = false;
										}

									}
								} else {
									result = "ATTENTION: This action [" + ra.DESCRIPTION()
											+ "] is not setting to WAIT [" + ra.WAIT()
											+ "] and it's using a message set to WAIT";
									System.out.println(result);
									RUNNING=false;
								}
								break;
							}
						} else if (rmg.TYPE() == Message.CONFIRMATION) {

							mtype = Message.CONFIRMATION;

							if (text.contains(rmg.MESSAGE())) {

								int position = text.indexOf(rmg.MESSAGE()) + rmg.MESSAGE().length() + 1;

								String order = text.substring(position);

								order = order.substring(0, order.indexOf(" "));

								saveOrderNumber(order);

								RobotAction robotAction = new RobotAction(ra.ID(),
										ra.DESCRIPTION() + " - Confirmando pedido [" + order + "]", false, null, false,
										0, 0, true, false, true, rmg.COMMANDS());

								robot.ROBOT_ACTIONS().set(i, robotAction);

								START_ACTIONS_AT = ra.ID();
								NUMBER_OF_ATTEMPTS = 0;
								i = 0;
								continue;
							}
						} else if (rmg.TYPE() == Message.CONCLUSION) {

							mtype = Message.CONCLUSION;

							if (text.contains(rmg.MESSAGE())) {

								saveDateTimeOfConclusion();

								result = "SUCCESS: [" + receitaBx.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO() + "]";
								System.out.println(result);

								RobotAction robotAction = new RobotAction(ra.ID(),
										rmg.RESPONSE() + " - Conclusão ["
												+ receitaBx.get().DATA_HORA_CONCLUSAO_PROCESSAMENTO() + "]",
										false, null, false, 0, 0, true, false, true, rmg.COMMANDS());

								robot.ROBOT_ACTIONS().set(i, robotAction);

								START_ACTIONS_AT = ra.ID();
								NUMBER_OF_ATTEMPTS = 0;
								i = 0;
								continue;
							} else {
								if (ra.WAIT()) {

									NUMBER_OF_ATTEMPTS++;

									if (NUMBER_OF_ATTEMPTS <= ra.NUMBER_OF_ATTEMPTS()) {

										String counter = "#" + String.valueOf(NUMBER_OF_ATTEMPTS) + "/"
												+ ra.NUMBER_OF_ATTEMPTS().toString();

										System.out.println(counter + " - Waiting for this action [" + rmg.MESSAGE()
												+ "] to be checked again...");

										actions.Wait(ra.WAIT_MILLISECONDS());

										START_ACTIONS_AT = ra.ID();
										i = 0;
										continue;

									} else {

										result = "ATTENTION: This process was ended after waiting 5 minutes for its conclusion.";
										System.out.println(result);
										// AutoCloseMessageDialog.show(rmg.RESPONSE(), "Atenção", 5000);

										if (rmg.ABORT()) {
											START_ACTIONS_AT = 0;
											NUMBER_OF_ATTEMPTS = 0;
											RUNNING = false;
										}

									}

								} else {
									result = "ATTENTION: This action [" + ra.DESCRIPTION()
											+ "] is not setting to WAIT [" + ra.WAIT()
											+ "] and it's using a message set to WAIT";
									System.out.println(result);
									RUNNING = false;
								}
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
		}

		return result;
	}

	private void saveOrderNumber(String orderNumber) {

		receitaBx.set(new ReceitaBx(receitaBx.get().RESOLUCAO_TELA(), receitaBx.get().CERTIFICADO(), null, null, null, receitaBx.get().PERFIL(),
				receitaBx.get().PERFIL_TYPE(), receitaBx.get().PERFIL_VALUE(), receitaBx.get().SISTEMA(),
				receitaBx.get().TIPO_ARQUIVO(), receitaBx.get().TIPO_PESQUISA(), receitaBx.get().DATA_INICIO(),
				receitaBx.get().DATA_FIM(), receitaBx.get().CNPJ_INCORPORADORA(), receitaBx.get().TIPO_EVENTO(),
				receitaBx.get().BAIXAR_ARQUIVO_ASSINADO(), receitaBx.get().CNPJ_ESTABELECIMENTO(),
				receitaBx.get().BUSCAR_TODOS_ESTABLECIMENTOS(), receitaBx.get().INSCRICAO_ESTADUAL(),
				receitaBx.get().ULTIMO_ARQUIVO_TRANSMITIDO(), orderNumber, null));

//		CryptoUtils.saveEncryptedGCM(receitaBx.get(), Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE);
	}

	private void saveDateTimeOfConclusion() {

		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String DATA_HORA_CONCLUSAO_PROCESSAMENTO = currentDateTime.format(formatter);

		receitaBx.set(new ReceitaBx(receitaBx.get().RESOLUCAO_TELA(), receitaBx.get().CERTIFICADO(), null, null, null, receitaBx.get().PERFIL(),
				receitaBx.get().PERFIL_TYPE(), receitaBx.get().PERFIL_VALUE(), receitaBx.get().SISTEMA(),
				receitaBx.get().TIPO_ARQUIVO(), receitaBx.get().TIPO_PESQUISA(), receitaBx.get().DATA_INICIO(),
				receitaBx.get().DATA_FIM(), receitaBx.get().CNPJ_INCORPORADORA(), receitaBx.get().TIPO_EVENTO(),
				receitaBx.get().BAIXAR_ARQUIVO_ASSINADO(), receitaBx.get().CNPJ_ESTABELECIMENTO(),
				receitaBx.get().BUSCAR_TODOS_ESTABLECIMENTOS(), receitaBx.get().INSCRICAO_ESTADUAL(),
				receitaBx.get().ULTIMO_ARQUIVO_TRANSMITIDO(), receitaBx.get().ULTIMO_PEDIDO_SOLICITADO(),
				DATA_HORA_CONCLUSAO_PROCESSAMENTO));

//		CryptoUtils.saveEncryptedGCM(receitaBx.get(), Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE);
	}

	private void reset() {

		receitaBx.set(new ReceitaBx(receitaBx.get().RESOLUCAO_TELA(), receitaBx.get().CERTIFICADO(), null, null, null, receitaBx.get().PERFIL(),
				receitaBx.get().PERFIL_TYPE(), receitaBx.get().PERFIL_VALUE(), receitaBx.get().SISTEMA(),
				receitaBx.get().TIPO_ARQUIVO(), receitaBx.get().TIPO_PESQUISA(), receitaBx.get().DATA_INICIO(),
				receitaBx.get().DATA_FIM(), receitaBx.get().CNPJ_INCORPORADORA(), receitaBx.get().TIPO_EVENTO(),
				receitaBx.get().BAIXAR_ARQUIVO_ASSINADO(), receitaBx.get().CNPJ_ESTABELECIMENTO(),
				receitaBx.get().BUSCAR_TODOS_ESTABLECIMENTOS(), receitaBx.get().INSCRICAO_ESTADUAL(),
				receitaBx.get().ULTIMO_ARQUIVO_TRANSMITIDO(), "", ""));

//		CryptoUtils.saveEncryptedGCM(receitaBx.get(), Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE);

	}
}
