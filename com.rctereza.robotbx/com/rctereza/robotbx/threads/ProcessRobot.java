package com.rctereza.robotbx.threads;

import java.io.IOException;
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
import com.rctereza.robotbx.tools.Actions;
import com.rctereza.robotbx.tools.RobotUtils;
import com.rctereza.robotbx.tools.SpedUtils;
import com.rctereza.robotocr.MessageBox2;

public class ProcessRobot implements Callable<ReceitaBx> {

	private static final Logger logger = LoggerFactory.getLogger(ProcessRobot.class);

	private ReceitaBx original;

	private String ULTIMO_PEDIDO_SOLICITADO = "";
	private String DATA_HORA_CONCLUSAO_PROCESSAMENTO = "";
	private String MENSAGEM_CONCLUSAO_PROCESSAMENTO = "";
	private String PERIODOS_FALTANDO = "";
	private Integer TOTAL_PERIODOS_FALTANDO = 0;
	private Status STATUS = Status.PENDING;

	public ProcessRobot(ReceitaBx original) {
		this.original = original;
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

		return new ReceitaBx(original.RESOLUCAO_TELA(), original.CERTIFICADO(), original.NOME_CLIENTE(),
				original.CNPJ_CLIENTE(), original.PASTA_ORIGEM_ARQUIVOS_BAIXADOS(),
				original.PASTA_DESTINO_ARQUIVOS_BAIXADOS(), original.PERFIL(), original.PERFIL_TYPE(),
				original.PERFIL_VALUE(), original.SISTEMA(), original.TIPO_ARQUIVO(), original.TIPO_PESQUISA(),
				original.DATA_INICIO(), original.DATA_FIM(), original.CNPJ_INCORPORADORA(), original.TIPO_EVENTO(),
				original.BAIXAR_ARQUIVO_ASSINADO(), original.CNPJ_ESTABELECIMENTO(),
				original.BUSCAR_TODOS_ESTABLECIMENTOS(), original.INSCRICAO_ESTADUAL(),
				original.ULTIMO_ARQUIVO_TRANSMITIDO(), ULTIMO_PEDIDO_SOLICITADO, DATA_HORA_CONCLUSAO_PROCESSAMENTO,
				MENSAGEM_CONCLUSAO_PROCESSAMENTO, PERIODOS_FALTANDO, TOTAL_PERIODOS_FALTANDO, STATUS);
	}

	private String performActions(Robot robot) throws Exception {

		String result = "";

		boolean RUNNING = true;

		int START_ACTIONS_AT = 0;

		int NUMBER_OF_ATTEMPTS = 0;

		Thread.sleep(6000); // pause for six seconds

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

					logger.info("Checking if there's a message box...");
					MessageBox2 mb = new MessageBox2(Constants.PROGRAM_NAME);
					String text = mb.getText();
					text = text.replace("\n", "");
					logger.info("Message box text found: [{}]", text);

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

		if (original.SISTEMA().equals(Sped.EFD.getValue())
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
			fileFolderPath = original.PASTA_ORIGEM_ARQUIVOS_BAIXADOS() + "\\Escrituração";
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

			fileNameBegins = original.CNPJ_CLIENTE(); // "07214419000195";
			fileFolderPath = original.PASTA_ORIGEM_ARQUIVOS_BAIXADOS() + "\\Escrituração Fiscal Digital";
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
			fileFolderPath = original.PASTA_ORIGEM_ARQUIVOS_BAIXADOS() + "\\Escrituração";
			fileFolderIndex = 2;

			result = getPeriods(startYear, startMonth, endYear, endMonth, fileNameBegins, fileFolderPath,
					fileFolderIndex);
		}

		return result;

	}

	private String getPeriods(int startYear, int startMonth, int endYear, int endMonth, String fileNameBegins,
			String fileFolder, int fileFolderIndex) {

		String result = "";

		Path folderPath = Paths.get(fileFolder);

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
				logger.info("Todos os meses foram encontrados para o periodo informado.");
			} else {
				// Print result
//				missingMonths.forEach(System.out::println);
//				System.out.println(missingMonths.size() + " months are missing...");
				logger.info("({}) meses não foram encontrados para o periodo informado.", missingMonths.size());
				result = missingMonths.toString();
			}

		} catch (IOException e) {
			e.printStackTrace();
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
