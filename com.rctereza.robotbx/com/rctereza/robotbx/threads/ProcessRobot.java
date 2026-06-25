package com.rctereza.robotbx.threads;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.enums.Message;
import com.rctereza.robotbx.enums.Sped;
import com.rctereza.robotbx.enums.Status;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;
import com.rctereza.robotbx.models.RobotMessageBox;
import com.rctereza.robotbx.models.Setting;
import com.rctereza.robotbx.ocr.ExtractImageText;
import com.rctereza.robotbx.ocr.TessUtils;
import com.rctereza.robotbx.tools.Actions;
import com.rctereza.robotbx.tools.RobotUtils;
import com.rctereza.robotbx.tools.ScreenResolution;
import com.rctereza.robotbx.tools.SpedUtils;

public class ProcessRobot implements Callable<ReceitaBx> {

	private static final Logger logger = LoggerFactory.getLogger(ProcessRobot.class);

	private ReceitaBx original;
	private Dimension monitor;
	private Boolean updateSetting;
	private Boolean monitorMoved;

	private String ULTIMO_PEDIDO_SOLICITADO = "";
	private String DATA_HORA_CONCLUSAO_PROCESSAMENTO = "";
	private String MENSAGEM_CONCLUSAO_PROCESSAMENTO = "";
	private String PERIODOS_FALTANDO = "";
	private Integer TOTAL_PERIODOS_FALTANDO = 0;
	private Status STATUS = Status.PENDING;

	private Rectangle targetMonitorBounds;

	public ProcessRobot(ReceitaBx original, Boolean updateSetting) {
		this.original = original;
		this.updateSetting = updateSetting;
		monitorMoved = false;
	}

	@Override
	public ReceitaBx call() {

		logger.info("Thread Starting...");

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
			Robot robot = RobotUtils.getRobotBasedOnScreenResolution(original);

			// *******************************************************************************************
			// PERFORM THE ACTIONS
			// *******************************************************************************************
			MENSAGEM_CONCLUSAO_PROCESSAMENTO = performActions(robot);

			// *******************************************************************************************
			// SET THE STATUS
			// *******************************************************************************************
			if (DATA_HORA_CONCLUSAO_PROCESSAMENTO != null && !DATA_HORA_CONCLUSAO_PROCESSAMENTO.equals(""))
				STATUS = Status.SUCCESS;
			else
				STATUS = Status.WARNING;

			// *******************************************************************************************
			// GET THE MISSING PERIODS BY CHECKING THE FILES DOWNLOADED
			// *******************************************************************************************
			PERIODOS_FALTANDO = getMissingPeriods();
			if (PERIODOS_FALTANDO.length() > 0) {
				String[] periods = PERIODOS_FALTANDO.split(",");
				TOTAL_PERIODOS_FALTANDO = periods.length;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			MENSAGEM_CONCLUSAO_PROCESSAMENTO = "ERROR: [" + e.getMessage() + "]";
			STATUS = Status.ERROR;

		} finally {
			if (process != null) {
				process.descendants().forEach(p -> p.destroy());
				process.destroy();
			}
		}

		logger.info("Thread Terminated!");

		Setting CONFIGURACAO = original.CONFIGURACAO();

		CONFIGURACAO = new Setting(original.CONFIGURACAO().SOFTWARE_NAME(), original.CONFIGURACAO().SOFTWARE_PATH(),
				original.CONFIGURACAO().SOFTWARE_PROGRAM(), original.CONFIGURACAO().DOWNLOAD_FOLDER(),
				original.CONFIGURACAO().SAVE_FOLDER(), original.CONFIGURACAO().LOG_FOLDER(),
				original.CONFIGURACAO().SAVE_LOG(), original.CONFIGURACAO().MAKE_SUBFOLDER(),
				original.CONFIGURACAO().AUTO_DOWNLOAD(), original.CONFIGURACAO().NUMBER_DOWNLOAD_SIMULTANEOUS(),
				original.CONFIGURACAO().MINUTES_FOR_NEXT_ORDER_UPDATE(), original.CONFIGURACAO().KEEP_WHICH_FILES(),
				false);

		return new ReceitaBx(original.RESOLUCAO_TELA(), CONFIGURACAO, original.CERTIFICADO(), original.PROCURADOR(),
				original.PERFIL(), original.PERFIL_TYPE(), original.PERFIL_VALUE(), original.SISTEMA(),
				original.TIPO_ARQUIVO(), original.TIPO_PESQUISA(), original.DATA_INICIO(), original.DATA_FIM(),
				original.CNPJ_INCORPORADORA(), original.TIPO_EVENTO(), original.BAIXAR_ARQUIVO_ASSINADO(),
				original.CNPJ_ESTABELECIMENTO(), original.BUSCAR_TODOS_ESTABLECIMENTOS(), original.INSCRICAO_ESTADUAL(),
				original.ULTIMO_ARQUIVO_TRANSMITIDO(), ULTIMO_PEDIDO_SOLICITADO, DATA_HORA_CONCLUSAO_PROCESSAMENTO,
				MENSAGEM_CONCLUSAO_PROCESSAMENTO, PERIODOS_FALTANDO, TOTAL_PERIODOS_FALTANDO, STATUS);
	}

	private String performActions(Robot robot) throws Exception {

		String result = "";

		boolean RUNNING = true;

		int START_ACTIONS_AT = 0;

		int NUMBER_OF_ATTEMPTS = 0;

		Thread.sleep(10000); // pause for 10 seconds

		Actions actions = new Actions();

		for (int i = 0; i < robot.ROBOT_ACTIONS().size(); i++) {

			RobotAction ra = robot.ROBOT_ACTIONS().get(i);

			logger.info("Action.............: {}", ra.toString());

			if ((ra.ENABLED()) && (ra.ID() >= START_ACTIONS_AT)) {

				for (RobotCommand rc : ra.ROBOT_COMMANDS()) {

					if (rc.ENABLED()) {

						logger.info("Command............: {}", rc.toString());

						switch (rc.COMMAND()) {

						case Command.WAIT:
							actions.Wait(rc.WAITMS());
							break;

						case Command.MOVE:
							if ((START_ACTIONS_AT == 0) || (ra.CONFIRMATON())) {
								if (ra.CONFIRMATON() && START_ACTIONS_AT > 0) {
									START_ACTIONS_AT = 0;
								}
								int absoluteX = targetMonitorBounds.x + rc.VALUEX();
								int absoluteY = targetMonitorBounds.y + rc.VALUEY();
								logger.debug(
										"CALCULATION -> boundX {} + relativeX {} = absoluteY {}, boundY {} + relativeY {} = absoluteY {}",
										targetMonitorBounds.x, rc.VALUEX(), absoluteX, targetMonitorBounds.y,
										rc.VALUEY(), absoluteY);
								actions.Move(absoluteX, absoluteY);
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

						case Command.TYPE_ONLY_NUMBERS:
							actions.TypeOnlyNumbers(rc.TEXT());
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

						case Command.ALT_ARROW_DOWN:
							actions.AltArrowDown();
							break;

						case Command.ARROW_DOWN:
							actions.ArrowDown();
							break;

						case Command.ARROW_UP:
							actions.ArrowUp();
							break;

						default:
							break;
						}
					}
				}

				if (ra.MESSAGEBOX()) {

					logger.info("Checking if there's a window to capture...");
					ExtractImageText mb = new ExtractImageText(Constants.PROGRAM_NAME);
					String text = mb.getText();
					monitor = mb.getMonitorSize();
					logger.info("Window text found: [{}]\n{}", text, monitor);

					Boolean found = false;
					Message mtype = Message.NONE;

					for (RobotMessageBox rmg : ra.MESSAGEBOX_TEXTS()) {

						if (rmg.TYPE() == Message.VALIDATION) {

							mtype = Message.VALIDATION;

							if (text.trim().equals(rmg.MESSAGE())) {

								result = rmg.RESPONSE();
								logger.info(result);

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

								result = rmg.RESPONSE();
								logger.info(result);

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

										logger.info("{} - Waiting for this action [{}] to be checked again...", counter,
												ra.DESCRIPTION());

										actions.Wait(ra.WAIT_MILLISECONDS());

										START_ACTIONS_AT = ra.ID();
										i = 0;
										continue;

									} else {

										result = rmg.RESPONSE();
										logger.info(result);

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
									logger.info(result);
									RUNNING = false;
								}
								break;
							}
						} else if (rmg.TYPE() == Message.CONFIRMATION) {

							mtype = Message.CONFIRMATION;

							if (text.contains(rmg.MESSAGE())) {

								int position = text.indexOf(rmg.MESSAGE()) + rmg.MESSAGE().length() + 1;

								String value = text.substring(position);

								ULTIMO_PEDIDO_SOLICITADO = value.substring(0, value.indexOf(" "));

								RobotAction robotAction = new RobotAction(ra.ID(),
										ra.DESCRIPTION() + " - Confirmando pedido [" + ULTIMO_PEDIDO_SOLICITADO + "]",
										false, null, false, 0, 0, true, false, true, rmg.COMMANDS());

								robot.ROBOT_ACTIONS().set(i, robotAction);

								START_ACTIONS_AT = ra.ID();
								NUMBER_OF_ATTEMPTS = 0;
								i = 0;
								continue;
							}
						} else if (rmg.TYPE() == Message.CONCLUSION) {

							mtype = Message.CONCLUSION;

							if (text.contains(rmg.MESSAGE())) {

								DATA_HORA_CONCLUSAO_PROCESSAMENTO = getDateTimeOfConclusion();

								result = "SUCCESS: [" + DATA_HORA_CONCLUSAO_PROCESSAMENTO + "]";
								logger.info(result);

								RobotAction robotAction = new RobotAction(ra.ID(),
										rmg.RESPONSE() + " - Conclusão [" + DATA_HORA_CONCLUSAO_PROCESSAMENTO + "]",
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

										logger.info("{} - Waiting for this action [{}] to be checked again...", counter,
												rmg.MESSAGE());

										actions.Wait(ra.WAIT_MILLISECONDS());

										START_ACTIONS_AT = ra.ID();
										i = 0;
										continue;

									} else {

										result = "Este processamento foi cancelado após esperar 5 minutos pela sua conclusão.";
										logger.info(result);
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
									logger.info(result);
									RUNNING = false;
								}
							}
						} else if (rmg.TYPE() == Message.FUNCTION) {

							if (rmg.MESSAGE() != null && rmg.MESSAGE().equals("CheckMenuOptions")) {

								Class<ProcessRobot> clazz = ProcessRobot.class;

								Constructor<ProcessRobot> constructor = clazz.getConstructor(ReceitaBx.class, Boolean.class);

								ProcessRobot obj = constructor.newInstance(original, updateSetting);

								Method method = clazz.getDeclaredMethod(rmg.MESSAGE(), Robot.class, Actions.class);

								method.setAccessible(true);

								method.invoke(obj, robot, actions);

							} else {

								// Get method reference
								Method method = ProcessRobot.class.getDeclaredMethod("CheckMonitorResolution",
										Actions.class);

								// Allow access to private method
								method.setAccessible(true);

								// Invoke method
								Object valid = method.invoke(ProcessRobot.this, actions);

								// Convert returned value
//								boolean found2 = (boolean) valid;
								if (!((boolean) valid)) {
									result = "ATTENTION: This application cannot continue running because there's no 1920x1080 monitor resolution.";
									logger.info(result);
									RUNNING = false;
									break;
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

	@SuppressWarnings("unused")
	private boolean CheckMonitorResolution(Actions actions) {
		if (ScreenResolution.moveAppTo1920x1080Monitor()) {
			monitorMoved = true;
			targetMonitorBounds = ScreenResolution.getTargetMonitorBounds();
			actions.Wait(3000);
//			logger.debug("Monitor: {}",targetMonitorBounds);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private void CheckMenuOptions(Robot robot, Actions actions) throws Exception {

		if (updateSetting) {
			
			logger.info("Checking the parameters of the menu Tools / Options");

			String salvarOsArquivosEm = original.CONFIGURACAO().DOWNLOAD_FOLDER();
			String numeroDownloads = original.CONFIGURACAO().NUMBER_DOWNLOAD_SIMULTANEOUS().toString();
			String atualizacaoPedidos = original.CONFIGURACAO().MINUTES_FOR_NEXT_ORDER_UPDATE().toString();
			String salvarLogEm = original.CONFIGURACAO().LOG_FOLDER();
			String errorMsg = "Não foi possivel gerar o arquivo de log";

			boolean changed = false;

			actions.Alt_F();
			actions.Enter();
			actions.Wait(1000);

			if (monitorMoved) {
				ScreenResolution.moveAppTo1920x1080Monitor();
//				int absoluteX = targetMonitorBounds.x + rc.VALUEX();
//				int absoluteY = targetMonitorBounds.y + rc.VALUEY();
//				logger.debug(
//						"CALCULATION -> boundX {} + relativeX {} = absoluteY {}, boundY {} + relativeY {} = absoluteY {}",
//						targetMonitorBounds.x, rc.VALUEX(), absoluteX, targetMonitorBounds.y,
//						rc.VALUEY(), absoluteY);
//				actions.Move(absoluteX, absoluteY);
			}
			
			// ------------------------------------------------------------------------------
			Rectangle rect = new Rectangle(789, // x
					408, // y
					241, // width = 1030 - 789 = 241
					21 // height = 429 - 408 = 21
			);
			ExtractImageText mb = new ExtractImageText(Constants.PROGRAM_NAME, 2.0);
			String text = mb.getTextFrom(rect, TessUtils.FIELD_TYPE.TEXT);
			if (!text.contains(salvarOsArquivosEm)) {
				logger.info("01 - Changing 'Salvar os arquivos em' to [{}]", salvarOsArquivosEm);
				actions.Ctrl_A();
				actions.Paste(salvarOsArquivosEm);
				actions.Wait(1000);
				changed = true;
			}

			actions.Tab();
			actions.Tab();

			// ------------------------------------------------------------------------------
			rect = new Rectangle(784, // x
					451, // y
					10, // width = 794 - 784 = 10
					11 // height = 463 - 451 = 12
			);
//			mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
			int value = mb.countDarkPixels(rect, 100);
			if (original.CONFIGURACAO().MAKE_SUBFOLDER()) {
				if (value < 30) {
					logger.info("02 - Selecting 'Criar sub-diretório para cada tipo de arquivo'.");
					actions.SpaceBar();
					actions.Wait(1000);
					changed = true;

				}
			} else {
				if (value > 30) {
					logger.info("02 - Deselecting 'Criar sub-diretório para cada tipo de arquivo'.");
					actions.SpaceBar();
					actions.Wait(1000);
					changed = true;
				}
			}

			actions.Tab();
			actions.Tab();

			// ------------------------------------------------------------------------------
			rect = new Rectangle(963, // x
					507, // y
					12, // width = 974 - 963 = 11
					16 // height = 523 - 507 = 16
			);
//			mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
			text = mb.getTextFrom(rect, TessUtils.FIELD_TYPE.NONE);
			if (original.CONFIGURACAO().NUMBER_DOWNLOAD_SIMULTANEOUS() == 5) {
				if (!text.contains(numeroDownloads)) {
					logger.info("03 - Setting 'Número de downloads simultâneos:' to [5].");
					actions.Ctrl_A();
					actions.Paste("5");
					actions.Wait(1000);
					changed = true;
				}
			} else {
				if (text.contains(numeroDownloads)) {
					logger.info("03 - Setting 'Número de downloads simultâneos:' to [1].");
					actions.Ctrl_A();
					actions.Paste("1");
					actions.Wait(1000);
					changed = true;
				}
			}

			actions.Tab();

			// ------------------------------------------------------------------------------
			rect = new Rectangle(973, // x
					558, // y
					44, // width = 1017 - 973 = 11
					16 // height = 575 - 558 = 16
			);
//			mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
			text = mb.getTextFrom(rect, TessUtils.FIELD_TYPE.NONE);
			if (original.CONFIGURACAO().MINUTES_FOR_NEXT_ORDER_UPDATE() == 60) {
				if (!text.contains(atualizacaoPedidos)) {
					logger.info("04 - Setting 'Atualização de pedidos (em minutos)' to [60].");
					actions.Ctrl_A();
					actions.Paste("60");
					actions.Wait(1000);
					changed = true;
				}
			} else {
				if (text.contains(atualizacaoPedidos)) {
					logger.info("04 - Setting 'Atualização de pedidos (em minutos)' to [10].");
					actions.Ctrl_A();
					actions.Paste("10");
					actions.Wait(1000);
					changed = true;
				}
			}

			actions.Tab();

			// ------------------------------------------------------------------------------
			rect = new Rectangle(784, // x
					614, // y
					10, // width = 795 - 784 = 11
					11 // height = 626 - 614 = 12
			);
//			mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
			value = mb.countDarkPixels(rect, 100);
			if (original.CONFIGURACAO().SAVE_LOG()) {
				if (value < 30) {
					logger.info("05 - Selecting 'Salvar log para depuração.");
					actions.SpaceBar();
					actions.Wait(1000);
					actions.Tab();
					changed = true;
				}

				rect = new Rectangle(784, // x
						632, // y
						256, // width = 1040 - 783 = 257
						21 // height = 654 - 632 = 22
				);
//				mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
				text = mb.getTextFrom(rect, TessUtils.FIELD_TYPE.TEXT);
				if (!text.contains(salvarLogEm)) {
					logger.info("06 - Changing the path to [{}].", salvarLogEm);
					actions.Ctrl_A();
					actions.Paste(salvarLogEm);
					actions.Wait(1000);
					changed = true;
				}
				actions.Tab();
			} else {
				if (value > 30) {
					logger.info("05 - Deselecting 'Salvar log para depuração.");
					actions.SpaceBar();
					actions.Wait(1000);
					changed = true;
				}
				actions.Tab();
			}

			if (changed) {
				// System.out.println("Salvar");
				actions.Tab();
				actions.Tab();
				actions.Tab();
				actions.SpaceBar();
				actions.Wait(1000);

				mb = new ExtractImageText(Constants.PROGRAM_NAME, 2.0);
				text = mb.getText();
				if (text.contains(errorMsg)) {
					logger.warn("ATTENTION! " + errorMsg + "... [" + salvarLogEm + "]");
				} else {
					actions.SpaceBar();
					logger.info("Parameters fixed with success.");
				}
			} else {
				// System.out.println("Cancelar");
				actions.Tab();
				actions.Tab();
				actions.Tab();
				actions.Tab();
				actions.SpaceBar();
				logger.info("Parameters already fixed. No change needed.");
			}
		}
	}

	private String getDateTimeOfConclusion() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return currentDateTime.format(formatter);
	}

	private String getMissingPeriods() {
		String result = "";

		int startYear;
		int startMonth;
		int endYear;
		int endMonth;
		String fileNameBegins;
		String fileFolderPath;
		int fileFolderIndex;

		if (original.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())
				&& original.TIPO_PESQUISA().equals(SpedUtils.contribuicoesSearchTypes[2])) {

			// SISTEMA.........: SPED Contribuições
			// TIPO DE ARQUIVO.: Escrituração
			// TIPO DE PESQUISA: Período da Escrituração
			// NOME DO ARQUIVO.:
			// PISCOFINS_20200101_20200131_07214419000195_Original_20200311170643_466E91B68B0768A77ED8E18073A1310E47D56CF1.txt

			String[] values = original.DATA_INICIO().split("/");
			startYear = Integer.parseInt(values[2]);
			startMonth = Integer.parseInt(values[1]);

			values = original.DATA_FIM().split("/");
			endYear = Integer.parseInt(values[2]);
			endMonth = Integer.parseInt(values[1]);

			fileNameBegins = "PISCOFINS";
			fileFolderPath = original.CONFIGURACAO().DOWNLOAD_FOLDER() + "\\Escrituração";
			fileFolderIndex = 0;

			result = getPeriods(startYear, startMonth, endYear, endMonth, fileNameBegins, fileFolderPath,
					fileFolderIndex);
		}

		else if (original.SISTEMA().equals(Sped.EFD.getValue())) {

			// SISTEMA.........: SPED Fiscal-EFD ICMS IPI
			// TIPO DE ARQUIVO.: Escrituração Fiscal Digital
			// TIPO DE PESQUISA: Por Período da Escrituração
			// NOME DO ARQUIVO.:
			// 07214419000195-293845298-20210401-20210430-0-9535ADC3A5F5892956F5ECDEDE1E6D397F8FE8B4-SPED-EFD.txt

			String[] values = original.DATA_INICIO().split("/");
			startYear = Integer.parseInt(values[2]);
			startMonth = Integer.parseInt(values[1]);

			values = original.DATA_FIM().split("/");
			endYear = Integer.parseInt(values[2]);
			endMonth = Integer.parseInt(values[1]);

			fileNameBegins = original.PROCURADOR().CLIENTE_DOC(); // "07214419000195";
			fileFolderPath = original.CONFIGURACAO().DOWNLOAD_FOLDER() + "\\Escrituração Fiscal Digital";
			fileFolderIndex = 1;

			result = getPeriods(startYear, startMonth, endYear, endMonth, fileNameBegins, fileFolderPath,
					fileFolderIndex);
		}

		else if (original.SISTEMA().equals(Sped.ECF.getValue())
				&& original.TIPO_PESQUISA().equals(SpedUtils.ecfSearchTypes[1])) {

			// SISTEMA.........: SPED ECF
			// TIPO DE ARQUIVO.: Escrituração
			// TIPO DE PESQUISA: Período da Escrituração
			// NOME DO ARQUIVO.: SPEDECF-07214419000195-20140101-20141231-20150924103745.txt

			String[] values = original.DATA_INICIO().split("/");
			startYear = Integer.parseInt(values[2]);
			startMonth = Integer.parseInt(values[1]);

			values = original.DATA_FIM().split("/");
			endYear = Integer.parseInt(values[2]);
			endMonth = Integer.parseInt(values[1]);

			fileNameBegins = "SPEDECF";
			fileFolderPath = original.CONFIGURACAO().DOWNLOAD_FOLDER() + "\\Escrituração";
			fileFolderIndex = 2;

			result = getPeriods(startYear, startMonth, endYear, endMonth, fileNameBegins, fileFolderPath,
					fileFolderIndex);
		}

		return result;

	}

	private String getPeriods(int startYear, int startMonth, int endYear, int endMonth, String fileNameBegins,
			String fileFolder, int fileFolderIndex) {

//		logger.debug("{}/{}, {}/{}, {}, {}, {}", startYear, startMonth, endYear, endMonth, fileNameBegins, fileFolder,
//				fileFolderIndex);

		String result = "";

		Path folderPath = Paths.get(fileFolder);

		if (Files.exists(folderPath)) {

			List<String> periods = new ArrayList<>();

			try (Stream<Path> paths = Files.list(folderPath)) {

				paths.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".txt")) // filter by extension
						.forEach(p -> {
							String fileName = p.getFileName().toString();

							if (fileName.startsWith(fileNameBegins)) {

								String extracted = extractPart(fileName, fileFolderIndex);

//							System.out.println("File: " + fileName);
//							System.out.println("Extracted: " + extracted);

								periods.add(extracted);
							}

						});

				Set<YearMonth> existingMonths = new HashSet<>();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

				for (String period : periods) {
					String startDateStr = period.split("_")[0];
					LocalDate startDate = LocalDate.parse(startDateStr, formatter);
					existingMonths.add(YearMonth.from(startDate));
				}

				// Define full range
				YearMonth start = YearMonth.of(startYear, startMonth);
				YearMonth end = YearMonth.of(endYear, endMonth);

				List<YearMonth> missingMonths = new ArrayList<>();

				YearMonth current = start;
				while (!current.isAfter(end)) {
					if (!existingMonths.contains(current)) {
						missingMonths.add(current);
					}
					current = current.plusMonths(1);
				}

				if (missingMonths.size() == 0) {
					logger.info("Todos os meses existem para o periodo informado.");
				} else {

					logger.info("(Existem meses faltando no periodo informado. Quantidade: {}.", missingMonths.size());
					result = missingMonths.toString();
				}

			} catch (IOException e) {
				logger.error("getPeriods()-> {} ", e.getMessage(), e);
			}

		}

		return result;
	}

	private String extractPart(String fileName, int fileFolderIndex) {
		String result = "";

		if (fileFolderIndex == 0) {
			int initialPosition = fileName.indexOf('_') + 1;
			result = fileName.substring(initialPosition, 27);
		} else if (fileFolderIndex == 1 || fileFolderIndex == 2) {
			String value = fileName.substring(fileName.indexOf("-") + 1);
			value = value.substring(value.indexOf("-") + 1);
			result = value.substring(0, 17).replace("-", "_");
		}

		return result;
	}

}
