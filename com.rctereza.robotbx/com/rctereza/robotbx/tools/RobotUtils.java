package com.rctereza.robotbx.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.enums.Sped;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;
import com.rctereza.robotbx.models.RobotTexts;

public class RobotUtils {
	
	private static boolean robotCommandEnabled = true;

	public static RobotTexts getRobotTexts(ReceitaBx params) {
		return new RobotTexts(1,"Robot of Texts", true, getListOfTexts(params));
	}
	
	public static Map<Integer, String> getListOfTexts(ReceitaBx params) {
		Map<Integer, String> texts = new HashMap<>();
		texts.put(1, "Buscar Certificado");
		return texts;
	}
	
	public static Robot getRobotBasedOnScreenResolution(ReceitaBx params) {
		Robot result = new Robot();
		
		if (Constants.DEBUG) {
			robotCommandEnabled = false;
			System.out.println("*** DEBUG IS ON *** ");
		}
		
		List<Robot> list = getListOfPreConfigfuredRobots(params);
		for (Robot robot: list) {
			if (robot.NAME().contains(params.SCREEN()) ) {
				result = robot;
				break;
			}
		}
		
		return result;
	}

	public static List<Robot> getListOfPreConfigfuredRobots(ReceitaBx params) {
		ArrayList<Robot> result = new ArrayList<>();
		result.add(getRobot01(params));
		result.add(getRobot02(params));
		return result;
	}
	
	private static Robot getRobot01(ReceitaBx params) {
		Robot result = new Robot(1,"Robot for 1366x768 resolution", 768, 1366, 418, 616, true, getRobot01Actions(params));
		return result;
	}
	
	private static List<RobotAction> getRobot01Actions(ReceitaBx params) {
		int counter = 10;
		
		ArrayList<RobotAction> actions = new ArrayList<>();
		ArrayList<RobotCommand> commands = new ArrayList<>();
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter, "Esperar o programa abrir", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 451, 565, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Buscar Certificado'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 598, 542, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, params.CERTIFICADO().FILE_PATH() + "\\" + params.CERTIFICADO().FILE_NAME(), robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Colar o caminho do certificado", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 923, 541, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Escolha'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 617, 386, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, params.CERTIFICADO().FILE_PASS(), robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Colar o password do certificado", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 640, 414, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'OK'", true, new ArrayList<>(commands)));
		
		if (params.PERFIL().equals("Procurador")) {
			
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 691, 531, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar no campo 'selecionar o perfil'", true, new ArrayList<>(commands)));
			
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 689, 531, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Selecionar o perfil 'Procurador'", true, new ArrayList<>(commands)));

			if (params.PERFIL_TYPE().equals("CNPJ")) {

				commands.clear();
				commands.add(new RobotCommand(1, Command.MOVE, 526, 531, null, robotCommandEnabled));
				commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Quem você representa?'", true, new ArrayList<>(commands)));
				
				commands.clear();
				commands.add(new RobotCommand(1, Command.MOVE, 523, 531, null, robotCommandEnabled));
				commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Selecionar a opção 'CNPJ'", true, new ArrayList<>(commands)));
				
			}
			
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 569, 531, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(3, Command.PASTE, null, null, params.PERFIL_VALUE(), robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Colar o '" + params.PERFIL_TYPE() + "' informado nos parâmetros", true, new ArrayList<>(commands)));
			
		}

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 875, 568, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Entrar'", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 245, 124, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Pesquisa'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 555, 221, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione Sistema'", true, new ArrayList<>(commands)));
				
		if (params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 561, 240, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contribuições'", true, new ArrayList<>(commands)));
		}
		else if (params.SISTEMA().equals(Sped.CONTABIL.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 565, 254, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contabil'", true, new ArrayList<>(commands)));
		}
		else if (params.SISTEMA().equals(Sped.ECF.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 569, 269, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED ECF'", true, new ArrayList<>(commands)));
		}
		else if (params.SISTEMA().equals(Sped.EFD.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 572, 282, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED EFD-Reinf'", true, new ArrayList<>(commands)));
		}
		else if (params.SISTEMA().equals(Sped.FISCAL.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 579, 296, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Fiscal-EFD ICMS IPI'", true, new ArrayList<>(commands)));
		}
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 729, 252, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de arquivo'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 727, 261, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Escrituração'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 674, 279, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de pesquisa'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 597, 295, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Periodo de Entrega'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 519, 322, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, Constants.PROGRAM_PERIOD_START, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Inicio'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 497, 341, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, Constants.PROGRAM_PERIOD_END, robotCommandEnabled));
		commands.add(new RobotCommand(4, Command.TAB, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Fim'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 659, 594, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botao 'Pesquisar'", true, new ArrayList<>(commands)));

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
	
	private static Robot getRobot02(ReceitaBx params) {
		Robot result = new Robot(1,"Robot for 1920x1080 resolution", 1080, 1920, 418, 616, true, getRobot02Actions(params));
		return result;
	}
	
	private static List<RobotAction> getRobot02Actions(ReceitaBx params) {
		int counter = 10;
		
		ArrayList<RobotAction> actions = new ArrayList<>();
		ArrayList<RobotCommand> commands = new ArrayList<>();
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter, "Esperar o programa abrir", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 744, 722, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Buscar Certificado'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 861, 700, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, params.CERTIFICADO().FILE_PATH() + "\\" + params.CERTIFICADO().FILE_NAME(), robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Colar o caminho do certificado", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 1201, 700, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Escolha'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 881, 543, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, params.CERTIFICADO().FILE_PASS(), robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Colar o password do certificado", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 921, 573, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'OK'", true, new ArrayList<>(commands)));

		if (params.PERFIL().equals("Procurador")) {
			
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 953, 690, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar no campo 'selecionar o perfil'", true, new ArrayList<>(commands)));
			
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 953, 721, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Selecionar o perfil 'Procurador'", true, new ArrayList<>(commands)));

			if (params.PERFIL_TYPE().equals("CNPJ")) {

				commands.clear();
				commands.add(new RobotCommand(1, Command.MOVE, 830, 687, null, robotCommandEnabled));
				commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Clicar no campo 'Quem você representa?'", true, new ArrayList<>(commands)));
				
				commands.clear();
				commands.add(new RobotCommand(1, Command.MOVE, 825, 717, null, robotCommandEnabled));
				commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
				actions.add(new RobotAction(counter += 10, "Selecionar a opção 'CNPJ'", true, new ArrayList<>(commands)));
				
			}
			
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 847, 688, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			commands.add(new RobotCommand(3, Command.PASTE, null, null, params.PERFIL_VALUE(), robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Colar o '" + params.PERFIL_TYPE() + "' informado nos parâmetros", true, new ArrayList<>(commands)));
			
		}

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 1160, 720, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Entrar'", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 526, 276, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Pesquisa'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 830, 379, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione Sistema'", true, new ArrayList<>(commands)));
		
		if (params.SISTEMA().equals(Sped.CONTRIBUICOES.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 397, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contribuições'", true, new ArrayList<>(commands)));
		}
		else if (params.SISTEMA().equals(Sped.CONTABIL.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 412, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contabil'", true, new ArrayList<>(commands)));
		}
		else if (params.SISTEMA().equals(Sped.ECF.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 426, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED ECF'", true, new ArrayList<>(commands)));
		}
		else if (params.SISTEMA().equals(Sped.EFD.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 440, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED EFD-Reinf'", true, new ArrayList<>(commands)));
		}
		else if (params.SISTEMA().equals(Sped.FISCAL.getValue())) {
			commands.clear();
			commands.add(new RobotCommand(1, Command.MOVE, 835, 454, null, robotCommandEnabled));
			commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
			actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Fiscal-EFD ICMS IPI'", true, new ArrayList<>(commands)));
		}
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 876, 407, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de arquivo'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 872, 422, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Escrituração'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 857, 433, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de pesquisa'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 851, 451, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Periodo de Entrega'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 768, 480, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, Constants.PROGRAM_PERIOD_START, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Inicio'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 766, 500, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, Constants.PROGRAM_PERIOD_END, robotCommandEnabled));
		commands.add(new RobotCommand(4, Command.TAB, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Fim'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 933, 753, null, robotCommandEnabled));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, robotCommandEnabled));
		actions.add(new RobotAction(counter += 10, "Clicar no botao 'Pesquisar'", true, new ArrayList<>(commands)));

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
	
	
}
