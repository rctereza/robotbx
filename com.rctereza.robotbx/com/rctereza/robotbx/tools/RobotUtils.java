package com.rctereza.robotbx.tools;

import java.util.ArrayList;
import java.util.List;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.enums.Command;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Robot;
import com.rctereza.robotbx.models.RobotAction;
import com.rctereza.robotbx.models.RobotCommand;

public class RobotUtils {

	public static Robot getRobot(ReceitaBx params) {
//		List<Robot> list = getPreConfigfuredRobot(params.CERTIFICADO());
//		return list.get(0);
		return null;
	}
	
	public static Robot getRobotBasedOnScreenResolution(Certificate certificate, String resolution) {
		Robot result = new Robot();
			
		List<Robot> list = getListOfPreConfigfuredRobots(certificate);
		for (Robot robot: list) {
			if (robot.NAME().contains(resolution) ) {
				result = robot;
				break;
			}
		}
		
		return result;
	}

//	public static List<Robot> getListOfPreConfigfuredRobots(ReceitaBx params) {
//		ArrayList<Robot> result = new ArrayList<>();
//		result.add(getRobot01(certificate));
//		result.add(getRobot02(certificate));
//		return result;
//	}

	public static List<Robot> getListOfPreConfigfuredRobots(Certificate certificate) {
		ArrayList<Robot> result = new ArrayList<>();
		result.add(getRobot01(certificate));
		result.add(getRobot02(certificate));
		return result;
	}
	
	private static Robot getRobot01(Certificate certificate) {
		Robot result = new Robot(1,"Robot for 1366x768 resolution", 768, 1366, 418, 616, true, getRobot01Actions(certificate));
		return result;
	}
	
	private static List<RobotAction> getRobot01Actions(Certificate certificate) {
		int counter = 10;
		
		ArrayList<RobotAction> actions = new ArrayList<>();
		ArrayList<RobotCommand> commands = new ArrayList<>();
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, null, true));
		actions.add(new RobotAction(counter, "Esperar o programa abrir", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 451, 565, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Buscar Certificado'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 598, 542, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, certificate.FILE_PATH() + "\\" + certificate.FILE_NAME(), true));
		actions.add(new RobotAction(counter += 10, "Colar o caminho do certificado", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 923, 541, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Escolha'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 617, 386, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, certificate.FILE_PASS(), true));
		actions.add(new RobotAction(counter += 10, "Colar o password do certificado", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 640, 414, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'OK'", true, new ArrayList<>(commands)));
		
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 850, 533, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar no campo 'selecionar o perfil'", true, new ArrayList<>(commands)));
//
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 844, 566, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Selecionar o perfil 'Procurador'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 875, 568, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Entrar'", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 264, 118, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Pesquisa'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 887, 221, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione Sistema'", true, new ArrayList<>(commands)));
				
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 827, 243, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contribuições'", true, new ArrayList<>(commands)));

//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 710, 255, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contabil'", true, new ArrayList<>(commands)));
//
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 688, 267, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED ECF'", true, new ArrayList<>(commands)));
//
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 679, 286, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED EFD-Reinf'", true, new ArrayList<>(commands)));
//
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 701, 297, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Fiscal-EFD ICMS IPI'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 729, 252, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de arquivo'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 727, 261, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Escrituração'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 674, 279, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de pesquisa'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 597, 295, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Periodo de Entrega'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 519, 322, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, Constants.PROGRAM_PERIOD_START, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Inicio'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 497, 341, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, Constants.PROGRAM_PERIOD_END, true));
		commands.add(new RobotCommand(4, Command.TAB, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Fim'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 659, 594, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
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
	
	private static Robot getRobot02(Certificate certificate) {
		Robot result = new Robot(1,"Robot for 1920x1080 resolution", 1080, 1920, 418, 616, true, getRobot02Actions(certificate));
		return result;
	}
	
	private static List<RobotAction> getRobot02Actions(Certificate certificate) {
		int counter = 10;
		
		ArrayList<RobotAction> actions = new ArrayList<>();
		ArrayList<RobotCommand> commands = new ArrayList<>();
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.WAIT, null, null, null, true));
		actions.add(new RobotAction(counter, "Esperar o programa abrir", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 744, 722, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Buscar Certificado'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 861, 700, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, certificate.FILE_PATH() + "\\" + certificate.FILE_NAME(), true));
		actions.add(new RobotAction(counter += 10, "Colar o caminho do certificado", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 1201, 700, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Escolha'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 881, 543, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, certificate.FILE_PASS(), true));
		actions.add(new RobotAction(counter += 10, "Colar o password do certificado", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 921, 573, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'OK'", true, new ArrayList<>(commands)));

//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 951, 690, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar no campo 'selecionar o perfil'", true, new ArrayList<>(commands)));
//
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 96wwwwwwww1, 720, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Selecionar o perfil 'Procurador'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 1160, 720, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no botão 'Entrar'", true, new ArrayList<>(commands)));
		
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 526, 276, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Pesquisa'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 880, 377, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione Sistema'", true, new ArrayList<>(commands)));
				
		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 877, 397, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contribuições'", true, new ArrayList<>(commands)));
		
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 882, 410, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Contabil'", true, new ArrayList<>(commands)));
//
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 873, 424, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED ECF'", true, new ArrayList<>(commands)));
//
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 883, 437, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED EFD-Reinf'", true, new ArrayList<>(commands)));
//
//		commands.clear();
//		commands.add(new RobotCommand(1, Command.MOVE, 877, 452, null, true));
//		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
//		actions.add(new RobotAction(counter += 10, "Clicar na opção 'SPED Fiscal-EFD ICMS IPI'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 876, 407, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de arquivo'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 872, 422, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Escrituração'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 857, 433, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Selecione um tipo de pesquisa'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 851, 451, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar na opção 'Periodo de Entrega'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 768, 480, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, Constants.PROGRAM_PERIOD_START, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Inicio'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 766, 500, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
		commands.add(new RobotCommand(3, Command.PASTE, null, null, Constants.PROGRAM_PERIOD_END, true));
		commands.add(new RobotCommand(4, Command.TAB, null, null, null, true));
		actions.add(new RobotAction(counter += 10, "Clicar no campo 'Data de Fim'", true, new ArrayList<>(commands)));

		commands.clear();
		commands.add(new RobotCommand(1, Command.MOVE, 933, 753, null, true));
		commands.add(new RobotCommand(2, Command.CLICK, null, null, null, true));
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
