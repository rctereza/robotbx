package com.rctereza.robotbx.tools;

import java.util.ArrayList;
import java.util.List;

import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.enums.Message;
import com.rctereza.robotbx.enums.Sped;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;
import com.rctereza.robotbx.models.RobotMessageBox;

public class RobotUtils {

	private static boolean robotCommandEnabled = true;

	public static Robot getRobotBasedOnScreenResolution(ReceitaBx params) {
		return new Robot(1, "Robot for 1920x1080 screen resolution", 1080, 1920, true,
				getRobotActionsFor1920x1080ScreenResolution(params));
	}

	private static List<RobotAction> getRobotActionsFor1920x1080ScreenResolution(ReceitaBx params) {
		int counter = 0;

		ArrayList<RobotAction> actions = new ArrayList<>();
		ArrayList<RobotCommand> commands = new ArrayList<>();
		ArrayList<RobotMessageBox> messages = new ArrayList<>();
		ArrayList<RobotCommand> messageCommands = new ArrayList<>();

		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Buscar Certificado'", false, null, false, 0, 0,
				false, false, true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 1000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.PASTE, null, null, null, params.CERTIFICADO().getAbsolutePath(),
				robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Informar o caminho do certificado", false, null, false, 0, 0, false,
				false, true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 1000, null, robotCommandEnabled));
		commands.add(
				new RobotCommand(1, Command.PASTE, null, null, null, params.CERTIFICADO().PASS(), robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Informar a senha e pressionar enter", false, null, false, 0, 0,
				false, false, true, new ArrayList<>(commands)));

		// ************************************************************************************************************************
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 1000, null, robotCommandEnabled));
		if (params.PROCURADOR().VALIDADE() != null) {
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));

			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.PASTE, null, null, null, params.PROCURADOR().DOCUMENTO(),
					robotCommandEnabled));

			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		} else {
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		}
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 1000, null, robotCommandEnabled));
		
		messages.clear();
		messages.add(new RobotMessageBox(1, "CheckMonitorResolution", "", true, Message.FUNCTION, null));
		messages.add(new RobotMessageBox(1, "Houve um erro",
				"Serviço indisponível temporariamente. Por favor, tente mais tarde...", true, Message.WARNING,
				null));	
		
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Entrar'", true, new ArrayList<>(messages), false,
				0, 0, false, false, true, new ArrayList<>(commands)));
		// ************************************************************************************************************************
		
//		messages.clear();
//		messages.add(new RobotMessageBox(1, "CheckMenuOptions", "", false, Message.FUNCTION, null));
//
//		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Entrar'", true, new ArrayList<>(messages), false,
//				0, 0, false, false, true, new ArrayList<>(commands)));

		// **********************************************************************************************************************************************
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ALT_ARROW_DOWN, null, null, null, null, robotCommandEnabled));

		if (params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())) {

			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));

		} else if (params.SISTEMA().equals(Sped.CONTABIL.getValue())) {

			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));

		} else if (params.SISTEMA().equals(Sped.ECF.getValue())) {

			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));

		} else if (params.SISTEMA().equals(Sped.EFD.getValue())) {

			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));

		} else if (params.SISTEMA().equals(Sped.FISCAL.getValue())) {

			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));

		}

		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione Sistema'", false, null, false, 0, 0,
				false, false, true, new ArrayList<>(commands)));

		// **********************************************************************************************************************************************
		commands.clear();
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ALT_ARROW_DOWN, null, null, null, null, robotCommandEnabled));

		if (params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue()) || params.SISTEMA().equals(Sped.ECF.getValue())) {

			commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));

		} else {

			if ((params.SISTEMA().equals(Sped.CONTABIL.getValue())
					&& params.TIPO_ARQUIVO().equals("Escrituração Contábil Digital"))
					|| (params.SISTEMA().equals(Sped.EFD.getValue())
							&& params.TIPO_ARQUIVO().equals("Eventos de Tabelas"))
					|| (params.SISTEMA().equals(Sped.FISCAL.getValue())
							&& params.TIPO_ARQUIVO().equals("Escrituração Fiscal Digital"))) {

				commands.add(new RobotCommand(1, Command.ARROW_DOWN, 899, 422, null, null, robotCommandEnabled));

			} else if (params.SISTEMA().equals(Sped.CONTABIL.getValue())
					&& params.TIPO_ARQUIVO().equals(SpedUtils.contabilFileTypes[1])) {

				commands.add(new RobotCommand(1, Command.MOVE, 899, 436, null, null, robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));

			} else if (params.SISTEMA().equals(Sped.CONTABIL.getValue())
					&& params.TIPO_ARQUIVO().equals(SpedUtils.contabilFileTypes[2])) {

				commands.add(new RobotCommand(1, Command.MOVE, 899, 450, null, null, robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));

			}

		}
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de arquivo'", false, null, false,
				0, 0, false, false, true, new ArrayList<>(commands)));

		if (!params.SISTEMA().equals(Sped.EFD.getValue()) && !params.SISTEMA().equals(Sped.FISCAL.getValue())) {

			// **********************************************************************************************************************************************

			if ((params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())
					&& params.TIPO_PESQUISA().equals(SpedUtils.contribuicoesSearchTypes[0]))
					|| (params.SISTEMA().equals(Sped.ECF.getValue())
							&& params.TIPO_PESQUISA().equals("Período de Entrega"))) {

				commands.clear();
				commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ALT_ARROW_DOWN, null, null, null, null, robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ARROW_DOWN, null, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de pesquisa'", false,
						null, false, 0, 0, false, false, true, new ArrayList<>(commands)));

			} else {

				if (!params.SISTEMA().equals(Sped.CONTABIL.getValue())) {

					commands.clear();
					commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
					commands.add(
							new RobotCommand(1, Command.ALT_ARROW_DOWN, null, null, null, null, robotCommandEnabled));

					if ((params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())
							&& params.TIPO_PESQUISA().equals(SpedUtils.contribuicoesSearchTypes[1]))
							|| (params.SISTEMA().equals(Sped.ECF.getValue())
									&& params.TIPO_PESQUISA().equals("Período da Escrituração"))) {

						commands.add(new RobotCommand(1, Command.MOVE, 900, 465, null, null, robotCommandEnabled));
						commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));

					} else if ((params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())
							&& params.TIPO_PESQUISA().equals(SpedUtils.contribuicoesSearchTypes[2]))) {

						commands.add(new RobotCommand(1, Command.MOVE, 900, 476, null, null, robotCommandEnabled));
						commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));

					} else if ((params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())
							&& params.TIPO_PESQUISA().equals(SpedUtils.contribuicoesSearchTypes[3]))) {

						commands.add(new RobotCommand(1, Command.MOVE, 900, 490, null, null, robotCommandEnabled));
						commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
					}

					actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de pesquisa'", false,
							null, false, 0, 0, false, false, true, new ArrayList<>(commands)));
				}
			}

			// **********************************************************************************************************************************************
			commands.clear();
			commands.add(
					new RobotCommand(1, Command.TYPE_ONLY_NUMBERS, null, null, null, params.DATA_INICIO(), robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Inicio'", false, null, false, 0, 0,
					false, false, true, new ArrayList<>(commands)));

			// **********************************************************************************************************************************************
			commands.clear();
			commands.add(new RobotCommand(1, Command.TYPE_ONLY_NUMBERS, null, null, null, params.DATA_FIM(), robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Fim'", false, null, false, 0, 0, false,
					false, true, new ArrayList<>(commands)));

			if ((params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())
					&& params.TIPO_PESQUISA().equals(SpedUtils.contribuicoesSearchTypes[1]))
					|| (params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())
							&& params.TIPO_PESQUISA().equals(SpedUtils.contribuicoesSearchTypes[3]))) {

				// **********************************************************************************************************************************************
				commands.clear();
				commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.TYPE_ONLY_NUMBERS, null, null, null, params.CNPJ_INCORPORADORA(),
						robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'CNPJ Incorporadora'", false, null, false,
						0, 0, false, false, true, new ArrayList<>(commands)));
			}

			// **********************************************************************************************************************************************
			commands.clear();

			if (params.SISTEMA().equals(Sped.CONTABIL.getValue()))
				commands.add(new RobotCommand(1, Command.MOVE, 1053, 753, null, null, robotCommandEnabled));
			else
				commands.add(new RobotCommand(1, Command.MOVE, 933, 753, null, null, robotCommandEnabled));

			commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
			// ----------------------------------
			messages.clear();
			messages.add(new RobotMessageBox(1, "Nenhum arquivo foi encontrado",
					"Nenhum arquivo foi encontrado para o critério de pesquisa solicitado.", true, Message.WARNING,
					null));
			messages.add(new RobotMessageBox(1, "tente mais tarde",
					"Serviço indisponível temporariamente. Por favor, tente mais tarde.", true, Message.WARNING,
					null));
			messages.add(new RobotMessageBox(1, "Não existe procuração",
					"Não existe procuração eletrônica para o procurador do certificado digital apresentado.", true, Message.WARNING,
					null));
			messages.add(new RobotMessageBox(1, "Pesquisa em andamento,",
					"Tempo de espera > 1 minuto. O processo deve ser reinicializado.", true, Message.WAITING, null));
			// ----------------------------------
			actions.add(new RobotAction(counter += 10, "Clicar no botao 'Pesquisar'", true, new ArrayList<>(messages),
					true, 2000, 30, false, false, true, new ArrayList<>(commands)));

		} else {

			if (params.SISTEMA().equals(Sped.EFD.getValue())) {

				// **********************************************************************************************************************************************
				commands.clear();
				commands.add(new RobotCommand(1, Command.MOVE, 890, 475, null, null, robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));

				if (params.TIPO_EVENTO().equals(SpedUtils.efdEventTypes[0])) {
					commands.add(new RobotCommand(1, Command.MOVE, 890, 495, null, null, robotCommandEnabled));
				} else if (params.TIPO_EVENTO().equals(SpedUtils.efdEventTypes[1])) {
					commands.add(new RobotCommand(1, Command.MOVE, 890, 505, null, null, robotCommandEnabled));
				} else if (params.TIPO_EVENTO().equals(SpedUtils.efdEventTypes[2])) {
					commands.add(new RobotCommand(1, Command.MOVE, 890, 520, null, null, robotCommandEnabled));
				}

				commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Tipo de Evento'", false, null, false, 0, 0,
						false, false, true, new ArrayList<>(commands)));

				if (params.BAIXAR_ARQUIVO_ASSINADO().equals(SpedUtils.efdDownloadSignedFiles[1])) {

					// **********************************************************************************************************************************************
					commands.clear();
					commands.add(new RobotCommand(1, Command.MOVE, 890, 500, null, null, robotCommandEnabled));
					commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
					commands.add(new RobotCommand(1, Command.MOVE, 890, 530, null, null, robotCommandEnabled));
					commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
					actions.add(
							new RobotAction(counter += 10, "Clicar no campo 'Baixar arquivo com Assinatura Digital'",
									false, null, false, 0, 0, false, false, true, new ArrayList<>(commands)));
				}
			}

			else if (params.SISTEMA().equals(Sped.FISCAL.getValue())) {

				// **********************************************************************************************************************************************
				commands.clear();
				commands.add(new RobotCommand(1, Command.TYPE_ONLY_NUMBERS, null, null, null, params.CNPJ_ESTABELECIMENTO(),
						robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'CNPJ do Estabelecimento'", false, null,
						false, 0, 0, false, false, true, new ArrayList<>(commands)));

				commands.clear();
				if (params.BUSCAR_TODOS_ESTABLECIMENTOS()) {
					commands.add(new RobotCommand(1, Command.SPACEBAR, null, null, null, null, robotCommandEnabled));
				}
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				actions.add(
						new RobotAction(counter += 10, "Clicar no campo 'Buscar Arquivos de Todos os Estabelecimentos'",
								false, null, false, 0, 0, false, false, true, new ArrayList<>(commands)));

				commands.clear();
				commands.add(new RobotCommand(1, Command.TYPE, null, null, null, params.INSCRICAO_ESTADUAL(),
						robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Inscrição Estadual'", false, null, false,
						0, 0, false, false, true, new ArrayList<>(commands)));

				commands.clear();
				commands.add(
						new RobotCommand(1, Command.TYPE_ONLY_NUMBERS, null, null, null, params.DATA_INICIO(), robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data Inicio'", false, null, false, 0, 0,
						false, false, true, new ArrayList<>(commands)));

				commands.clear();
				commands.add(
						new RobotCommand(1, Command.TYPE_ONLY_NUMBERS, null, null, null, params.DATA_FIM(), robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data Fim'", false, null, false, 0, 0,
						false, false, true, new ArrayList<>(commands)));

				commands.clear();
				if (params.ULTIMO_ARQUIVO_TRANSMITIDO()) {
					commands.add(new RobotCommand(1, Command.SPACEBAR, null, null, null, null, robotCommandEnabled));
				}
				commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Ultimo arquivo transmitido'", false, null,
						false, 0, 0, false, false, true, new ArrayList<>(commands)));
			}

			// **********************************************************************************************************************************************
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 1053, 753, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
			// ----------------------------------
			messages.clear();
			messages.add(new RobotMessageBox(1, "Pesquisa em andamento,",
					"Tempo de espera > 1 minuto. O processo deve ser reinicializado.", true, Message.WAITING, null));
			messages.add(new RobotMessageBox(1, "Nenhum arquivo foi encontrado",
					"Nenhum arquivo foi encontrado para o critério de pesquisa solicitado.", true, Message.WARNING,
					null));
			// ----------------------------------
			actions.add(new RobotAction(counter += 10, "Clicar no botao 'Pesquisar'", true, new ArrayList<>(messages),
					true, 2000, 30, false, false, true, new ArrayList<>(commands)));

		}

		// **********************************************************************************************************************************************
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.MOVE, 687, 611, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Marque todos o resultado da pesquisa", false, null, false, 0, 0,
				false, false, true, new ArrayList<>(commands)));

		// *****************************************************************************************************************
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.MOVE, 1052, 760, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));

		// ----------------------------------
		messages.clear();
		messages.add(new RobotMessageBox(1, "Seus arquivos estão sendo solicitados",
				"Tempo de espera > 1 minuto. O processo deve ser reinicializado.", true, Message.WAITING, null));

		messageCommands.clear();
		messageCommands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		messageCommands.add(new RobotCommand(1, Command.MOVE, 957, 565, null, null, robotCommandEnabled));
		messageCommands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		messages.add(new RobotMessageBox(1, "O pedido número", "", false, Message.CONFIRMATION,
				new ArrayList<>(messageCommands)));
		// ----------------------------------

		actions.add(new RobotAction(counter += 10, "Clicar no botao 'Solicitar arquivos marcados acima'", true,
				new ArrayList<>(messages), true, 2000, 30, false, false, true, new ArrayList<>(commands)));
		// *****************************************************************************************************************

		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.MOVE, 562, 310, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no link 'Acompanhamento'", false, null, false, 0, 0, false,
				false, true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.MOVE, 790, 410, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Selecionar o pedido", false, null, false, 0, 0, false, false, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.MOVE, 694, 590, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Selecionar os arquivos do pedido", false, null, false, 0, 0, false,
				false, true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 1000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.MOVE, 690, 290, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		// ----------------------------------
		messageCommands.clear();
		messageCommands.add(new RobotCommand(1, Command.WAIT, null, null, 1000, null, robotCommandEnabled));
		messageCommands.add(new RobotCommand(1, Command.MOVE, 1147, 297, null, null, robotCommandEnabled));
		messageCommands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		// ----------------------------------
		messages.clear();
		messages.add(new RobotMessageBox(1, "Não ha arquivos na fila de download", "Clicar no Botão 'Sair'", true,
				Message.CONCLUSION, new ArrayList<>(messageCommands)));
		// ----------------------------------
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Baixar'", true, new ArrayList<>(messages), true,
				6000, 50, true, true, true, new ArrayList<>(commands)));

		return actions;
	}
}
