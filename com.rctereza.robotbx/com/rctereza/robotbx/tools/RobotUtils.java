package com.rctereza.robotbx.tools;

import java.util.ArrayList;
import java.util.List;

import com.rctereza.robotbx.Constants;
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
		if (Constants.DEBUG) {
			robotCommandEnabled = false;
			System.out.println("*** DEBUG IS ON *** ");
		}
		
		Robot result = new Robot();
		
		List<Robot> list = getListOfPreConfigfuredRobots(params);
		for (Robot robot : list) {
			if (robot.NAME().contains(params.SCREEN())) {
				result = robot;
				break;
			}
		}
		
		return result;
	}

	public static List<Robot> getListOfPreConfigfuredRobots(ReceitaBx params) {
		ArrayList<Robot> result = new ArrayList<>();
		result.add(new Robot(1, "Robot for 1366x768 resolution", 768, 1366, 418, 616, true, getRobot01Actions(params)));
		result.add(new Robot(1, "Robot for 1920x1080 resolution", 1080, 1920, 418, 616, true, getRobot02Actions(params)));
		return result;
	}

	private static List<RobotAction> getRobot01Actions(ReceitaBx params) {

		ArrayList<RobotAction> actions = new ArrayList<>();
		/*
		ArrayList<RobotCommand> commands = new ArrayList<>();
		ArrayList<RobotMessageBox> messages = new ArrayList<>();
		int counter = 0;

//		commands.clear();
//		commands.add(new RobotCommand(1, Command.WAIT, null, null, null, robotCommandEnabled));
//		actions.add(new RobotAction(counter, "Esperar o programa abrir", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 451, 565, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Buscar Certificado'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 598, 542, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null,
				params.CERTIFICADO().FILE_PATH() + "\\" + params.CERTIFICADO().FILE_NAME(), robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Colar o caminho do certificado", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 923, 541, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Escolha'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 617, 386, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, params.SENHA(), robotCommandEnabled));
		commands.add(new RobotCommand(4, Command.TAB, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(5, Command.SPACEBAR, null, null, null, robotCommandEnabled));
		messages.clear();
		messages.add(new RobotMessageBox(1, "O certificado selecionado provavelmente já passou de sua data de validade","Este certificado esta vencido! O processo deve ser reiniciailizado com um certificado válido.", true));
		messages.add(new RobotMessageBox(1, "Não foi possivel accessar os certificados digitais","A senha deste certificado esta inválida! O processo deve ser reiniciailizado com uma senha válida.", true));
		actions.add(new RobotAction(counter += 10, "Informe a senha e pressione enter", false, new ArrayList<>(messages), true,
				new ArrayList<>(commands)));

//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 640, 414, null, robotCommandEnabled));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
//		messages.clear();
//		messages.add(new RobotMessageBox(1, "O certificado selecionado provavelmente já passou de sua data de validade","Este certificado esta vencido! O processo deve ser reiniciailizado com um certificado válido.", true));
//		messages.add(new RobotMessageBox(1, "Não foi possivel accessar os certificados digitais","A senha deste certificado esta inválida! O processo deve ser reiniciailizado com uma senha válida.", true));
//		actions.add(new RobotAction(counter += 10, "Clicar no botão 'OK'", true, new ArrayList<>(messages),
//				true, new ArrayList<>(commands)));

		if (params.PERFIL().equals("Procurador")) {

			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 691, 531, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar no campo 'selecionar o perfil'", false, null, true,
					new ArrayList<>(commands)));

			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 689, 531, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Selecionar o perfil 'Procurador'", false, null, true,
					new ArrayList<>(commands)));

			if (params.PERFIL_TYPE().equals("CNPJ")) {

				commands.clear();
				commands.add(new RobotCommand(1, Command.MOVE, 526, 531, null, robotCommandEnabled));
				commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Quem você representa?'", false, null, true,
						new ArrayList<>(commands)));

				commands.clear();
				commands.add(new RobotCommand(1, Command.MOVE, 523, 531, null, robotCommandEnabled));
				commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Selecionar a opção 'CNPJ'", false, null, true,
						new ArrayList<>(commands)));

			}

			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 569, 531, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(3, Command.PASTE, null, null, params.PERFIL_VALUE(), robotCommandEnabled));
			actions.add(
					new RobotAction(counter += 10, "Colar o '" + params.PERFIL_TYPE() + "' informado nos parâmetros",
							false, null, true, new ArrayList<>(commands)));

		}

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 875, 568, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Entrar'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 245, 124, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Pesquisa'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 555, 221, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione Sistema'", false, null, true,
				new ArrayList<>(commands)));

		if (params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 561, 240, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contribuições'", false, null, true,
					new ArrayList<>(commands)));
		} else if (params.SISTEMA().equals(Sped.CONTABIL.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 565, 254, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contabil'", false, null, true,
					new ArrayList<>(commands)));
		} else if (params.SISTEMA().equals(Sped.ECF.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 569, 269, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED ECF'", false, null, true,
					new ArrayList<>(commands)));
		} else if (params.SISTEMA().equals(Sped.EFD.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 572, 282, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED EFD-Reinf'", false, null, true,
					new ArrayList<>(commands)));
		} else if (params.SISTEMA().equals(Sped.FISCAL.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 579, 296, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Fiscal-EFD ICMS IPI'", false, null, true,
					new ArrayList<>(commands)));
		}

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 729, 252, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de arquivo'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 727, 261, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Escrituração'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 674, 279, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de pesquisa'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 597, 295, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Periodo de Entrega'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 519, 322, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(
				new RobotCommand(3, Command.PASTE, null, null, params.DATA_INICIO(), robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Inicio'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 497, 341, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, params.DATA_FIM(), robotCommandEnabled));
		commands.add(new RobotCommand(4, Command.TAB, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Fim'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 659, 594, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botao 'Pesquisar'", false, null, true,
				new ArrayList<>(commands)));

//				// Clica no botao "OK" da mensagem que não existe arquivos para o periodo informado.
//				actions.Wait();
//				actions.Move(720, 404);
//				actions.Click();

//				// Clica no botao "Sair"
//				actions.Wait();
//				actions.Move(485, 147);
//				actions.Click();		
*/
		return actions;
	}

	private static List<RobotAction> getRobot02Actions(ReceitaBx params) {
		int counter = 0;
		
		ArrayList<RobotAction> actions = new ArrayList<>();
		ArrayList<RobotCommand> commands = new ArrayList<>();
		ArrayList<RobotMessageBox> messages = new ArrayList<>();
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Buscar Certificado'", false, null, false, 0, 0, true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.PASTE, null, null, null, params.CERTIFICADO().FILE_PATH() + "\\" + params.CERTIFICADO().FILE_NAME(), robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Informar o caminho do certificado", false, null, false, 0, 0, true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.PASTE, null, null, null, params.SENHA(), robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		//----------------------------------
		messages.clear();
		messages.add(new RobotMessageBox(1, "O certificado selecionado provavelmente já passou de sua data de validade","Este certificado esta vencido! O processo deve ser reinicializado com um certificado válido.", true, Message.WARNING));
		messages.add(new RobotMessageBox(1, "Não foi possivel accessar os certificados digitais","A senha deste certificado esta inválida! O processo deve ser reinicializado com uma senha válida.", true, Message.WARNING));
		//----------------------------------
		actions.add(new RobotAction(counter += 10, "Informar a senha e pressionar enter", true, new ArrayList<>(messages), false, 0 , 0, true, new ArrayList<>(commands)));
		
		//For some reason, typing an wrong password, using the robot, does not show a pop-up saying that the password is not valid. 
		//That's why I added this extra step to check if the grid has a certificate on it. If the grid is empty, it means the password was not valid.
		commands.clear();
		commands.add(new RobotCommand(1, Command.NONE, null, null, null, null, robotCommandEnabled));
		//----------------------------------
		messages.clear();
		messages.add(new RobotMessageBox(1, "","A senha deste certificado esta inválida! O processo deve ser reinicializado com uma senha válida.", true, Message.VALIDATION));
		//----------------------------------
		actions.add(new RobotAction(counter += 10, "Verificar se a senha foi validada corretamente", true, new ArrayList<>(messages), false, 0, 0, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Entrar'", false, null, false, 0, 0, true, new ArrayList<>(commands)));
				
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
		commands.add(new RobotCommand(1, Command.ALTARROWDOWN, null, null, null, null, robotCommandEnabled));
		
		if (params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())) {
			
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			
		} else if (params.SISTEMA().equals(Sped.CONTABIL.getValue())) {
			
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			
		} else if (params.SISTEMA().equals(Sped.ECF.getValue())) {
			
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			
		} else if (params.SISTEMA().equals(Sped.EFD.getValue())) {
			
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			
		} else if (params.SISTEMA().equals(Sped.FISCAL.getValue())) {
			
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
			
		}
		
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione Sistema'", false, null, false, 0, 0, true,
				new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ALTARROWDOWN, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de arquivo'", false, null, false, 0, 0, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ALTARROWDOWN, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ARROWDOWN, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de pesquisa'", false, null, false, 0, 0, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(3, Command.TYPE, null, null, null, params.DATA_INICIO(), robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.TAB, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Inicio'", false, null, false, 0, 0, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.TYPE, null, null, null, params.DATA_FIM(), robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Fim'", false, null, false, 0, 0, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 933, 753, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		//----------------------------------
		messages.clear();
		messages.add(new RobotMessageBox(1, "Pesquisa em andamento,","Tempo de espera > 1 minuto. O processo deve ser reinicializado.", true, Message.WAITING));
		messages.add(new RobotMessageBox(1, "O pedido número","", false, Message.CONFIRMATION));
		//----------------------------------
		actions.add(new RobotAction(counter += 10, "Clicar no botao 'Pesquisar'", true, new ArrayList<>(messages), true, 2000, 30, true,
				new ArrayList<>(commands)));
		
		// 957,579 - Botão cancelar
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.MOVE, 687, 611, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Marque todos o resultado da pesquisa", false, null, false, 0, 0, true,
				new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 1052, 760, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		//----------------------------------
		messages.clear();
		messages.add(new RobotMessageBox(1, "Pesquisa em andamento, Favor aguardar alguns instantes.","Tempo de espera > 1 minuto. O processo deve ser reinicializado.", true, Message.WAITING));
		//----------------------------------
		actions.add(new RobotAction(counter += 10, "Clicar no botao 'Solicitar arquivos marcados acima'", true, new ArrayList<>(messages), true, 2000, 30, true,
				new ArrayList<>(commands)));
		
		// 957,579 - Botão cancelar
		
		/*
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, 2000, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.ENTER, null, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.MOVE, 573, 309, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(1, Command.CLICK, null, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no link 'Acompanhamento'", false, null, false, 0, 0, true,
				new ArrayList<>(commands)));
		*/
		
		return actions;
	}

/*
	private static List<RobotAction> getRobot02ActionsOld(ReceitaBx params) {

		ArrayList<RobotAction> actions = new ArrayList<>();
		ArrayList<RobotCommand> commands = new ArrayList<>();
		ArrayList<RobotMessageBox> messages = new ArrayList<>();
		int counter = 0;
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.WAIT, null, null, null, robotCommandEnabled));
//		actions.add(new RobotAction(counter, "Esperar o programa abrir", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 744, 722, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Buscar Certificado'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 861, 700, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null,
				params.CERTIFICADO().FILE_PATH() + "\\" + params.CERTIFICADO().FILE_NAME(), robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Colar o caminho do certificado", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 1201, 700, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Escolha'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		//commands.add(new RobotCommand(1, Command.MOVE, 881, 543, null, robotCommandEnabled));
		//commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.TYPE, null, null, params.SENHA(), robotCommandEnabled));
		//commands.add(new RobotCommand(4, Command.TAB, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(5, Command.ENTER, null, null, null, robotCommandEnabled));
		messages.clear();
		messages.add(new RobotMessageBox(1, "O certificado selecionado provavelmente já passou de sua data de validade","Este certificado esta vencido! O processo deve ser reiniciailizado com um certificado válido.", true));
		messages.add(new RobotMessageBox(1, "Não foi possivel accessar os certificados digitais","A senha deste certificado esta inválida! O processo deve ser reiniciailizado com uma senha válida.", true));
		actions.add(new RobotAction(counter += 10, "Informe a senha e pressione enter", false, new ArrayList<>(messages), true,
				new ArrayList<>(commands)));

//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 921, 573, null, robotCommandEnabled));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
//		messages.clear();
//		messages.add(new RobotMessageBox(1, "O certificado selecionado provavelmente já passou de sua data de validade","Este certificado esta vencido! O processo deve ser reiniciailizado com um certificado válido.", true));
//		messages.add(new RobotMessageBox(1, "Não foi possivel accessar os certificados digitais","A senha deste certificado esta inválida! O processo deve ser reiniciailizado com uma senha válida.", true));
//		actions.add(new RobotAction(counter += 10, "Clicar no botão 'OK'", true, new ArrayList<>(messages),
//				true, new ArrayList<>(commands)));

		if (params.PERFIL().equals("Procurador")) {

			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 953, 690, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar no campo 'selecionar o perfil'", false, null, true,
					new ArrayList<>(commands)));

			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 953, 721, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Selecionar o perfil 'Procurador'", false, null, true,
					new ArrayList<>(commands)));

			if (params.PERFIL_TYPE().equals("CNPJ")) {

				commands.clear();
				commands.add(new RobotCommand(1, Command.MOVE, 830, 687, null, robotCommandEnabled));
				commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Quem você representa?'", false, null, true,
						new ArrayList<>(commands)));

				commands.clear();
				commands.add(new RobotCommand(1, Command.MOVE, 825, 717, null, robotCommandEnabled));
				commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Selecionar a opção 'CNPJ'", false, null, true,
						new ArrayList<>(commands)));

			}

			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 847, 688, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(3, Command.PASTE, null, null, params.PERFIL_VALUE(), robotCommandEnabled));
			actions.add(
					new RobotAction(counter += 10, "Colar o '" + params.PERFIL_TYPE() + "' informado nos parâmetros",
							false, null, true, new ArrayList<>(commands)));

		}

		commands.clear();
		//commands.add(new RobotCommand(1, Command.MOVE, 1160, 720, null, robotCommandEnabled));
		//commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.TAB, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.TAB, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.ENTER, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Entrar'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 526, 276, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Pesquisa'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 830, 379, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione Sistema'", false, null, true,
				new ArrayList<>(commands)));

		if (params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 397, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contribuições'", false, null, true,
					new ArrayList<>(commands)));
		} else if (params.SISTEMA().equals(Sped.CONTABIL.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 412, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contabil'", false, null, true,
					new ArrayList<>(commands)));
		} else if (params.SISTEMA().equals(Sped.ECF.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 426, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED ECF'", false, null, true,
					new ArrayList<>(commands)));
		} else if (params.SISTEMA().equals(Sped.EFD.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 440, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED EFD-Reinf'", false, null, true,
					new ArrayList<>(commands)));
		} else if (params.SISTEMA().equals(Sped.FISCAL.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 454, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Fiscal-EFD ICMS IPI'", false, null, true,
					new ArrayList<>(commands)));
		}

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 876, 407, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de arquivo'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 872, 422, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Escrituração'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 857, 433, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de pesquisa'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 851, 451, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Periodo de Entrega'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 768, 480, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(
				new RobotCommand(3, Command.PASTE, null, null, params.DATA_INICIO(), robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Inicio'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 766, 500, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, params.DATA_FIM(), robotCommandEnabled));
		commands.add(new RobotCommand(4, Command.TAB, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Fim'", false, null, true,
				new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 933, 753, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botao 'Pesquisar'", false, null, true,
				new ArrayList<>(commands)));

//				// Clica no botao "OK" da mensagem que não existe arquivos para o periodo informado.
//				actions.Wait();
//				actions.Move(720, 404);
//				actions.Click();

//				// Clica no botao "Sair"
//				actions.Wait();
//				actions.Move(485, 147);
//				actions.Click();		
		return actions;
	}
*/

}
