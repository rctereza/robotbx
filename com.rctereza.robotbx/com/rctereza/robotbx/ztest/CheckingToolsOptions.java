package com.rctereza.robotbx.ztest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.ocr.ExtractImageText;
import com.rctereza.robotbx.threads.ProcessRobot;
import com.rctereza.robotbx.tools.Actions;

public class CheckingToolsOptions {

	private static final Logger logger = LoggerFactory.getLogger(ProcessRobot.class);
	
	private static ReceitaBx original;
	
	private static String salvarOsArquivosEm = "C:\\Temp\\ReceitanetBX";
	private static String criarSubDiretorio = "EB Criar sub-diretério para cada tipo de arquivo.";
	private static String numeroDownloads = "Número de downloads simultâneos: [5H";
	private static String salvarLog = "EB Salvar log para depuração.";

	public CheckingToolsOptions() {	
	}

	public CheckingToolsOptions(ReceitaBx original) {	
		CheckingToolsOptions.original = original;
	}
	
	public static void main(String[] args) throws Exception {
		
		Actions actions = new Actions();
		
		actions.Wait(5000);

		Class<CheckingToolsOptions> clazz = CheckingToolsOptions.class;
		
		Constructor<CheckingToolsOptions> constructor = clazz.getConstructor(ReceitaBx.class);
		
		CheckingToolsOptions obj = constructor.newInstance(original);

		Method method = clazz.getDeclaredMethod("CheckTOMenu", Actions.class);

		method.setAccessible(true);

		method.invoke(obj, actions);
		
		System.out.println("Done!");
	}

	@SuppressWarnings("unused")
	private void CheckTOMenu(Actions actions) throws Exception {

		boolean changed = false;

		actions.Wait(1000); // 2 seconds

		actions.Alt_F_O();

		actions.Wait(1000); // 2 seconds

		ExtractImageText mb = new ExtractImageText(Constants.PROGRAM_NAME, 3.0);
		String text = mb.getText();
		logger.info("This is the current values: {}", text);

		if (!text.contains(salvarOsArquivosEm)) {
			logger.info("01 - Changing 'Salvar os arquivos em' to [{}]", salvarOsArquivosEm);
			// moveMousePointer(850, 412, monitor, robot, actions);
			// actions.Click();
			// actions.Wait(1000);
			actions.Ctrl_A();
			actions.Paste(salvarOsArquivosEm);
			actions.Wait(1000);
			changed = true;

		}

		actions.Tab();
		actions.Tab();

		if (!text.contains(criarSubDiretorio)) {
			logger.info("02 - Selecting 'Criar sub-diretório para cada tipo de arquivo'.");
			// moveMousePointer(788, 453, monitor, robot, actions);
			actions.SpaceBar();
			actions.Wait(1000);
			changed = true;

		}

		actions.Tab();
		actions.Tab();

		if (!text.contains(numeroDownloads)) {
			logger.info("03 - Setting 'Número de downloads simultâneos:' to [5].");
			// moveMousePointer(967, 512, monitor, robot, actions);
			// actions.Click();
			// actions.Wait(1000);
			actions.Ctrl_A();
			actions.Paste("5");
			actions.Wait(1000);
			changed = true;

		}

		actions.Tab();
		actions.Tab();

		if (!text.contains(salvarLog)) {
			logger.info("04 - Selecting 'Salvar log para depuração.");
			// moveMousePointer(788, 620, monitor, robot, actions);
			// actions.Click();
			actions.SpaceBar();
			actions.Wait(1000);
			actions.Tab();

			logger.info("05 - Changing the path to [{}].", salvarOsArquivosEm + "\\receitanetbx.log");
			// moveMousePointer(810, 642, monitor, robot, actions);
			// actions.Click();
			// actions.Wait(1000);
			actions.Ctrl_A();
			actions.Paste(salvarOsArquivosEm + "\\receitanetbx.log");
			actions.Wait(1000);
			changed = true;
		}
		if (changed) {
			// Save
			// moveMousePointer(990, 672, monitor, robot, actions);
			// actions.Click();
			actions.Tab();
			actions.Tab();
			actions.Tab();
			actions.SpaceBar();
			actions.Wait(1000);

			// OK
			// moveMousePointer(960, 550, monitor, robot, actions);
			// actions.Click();
			actions.SpaceBar();
			logger.info("Parameters fixed with success.");

		} else {
			System.out.println("Cancelar");
			// moveMousePointer(1100, 672, monitor, robot, actions);
			// actions.Wait(1000);
			// actions.Click();
			actions.Tab();
			actions.Tab();
			actions.Tab();
			actions.Tab();
			actions.SpaceBar();

			logger.info("Parameters already fixed. No change needed.");
		}

	}
}
