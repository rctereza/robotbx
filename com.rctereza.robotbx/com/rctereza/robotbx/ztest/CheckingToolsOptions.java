package com.rctereza.robotbx.ztest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.tools.Actions;
import com.rctereza.robotocr.MessageBox2;

public class CheckingToolsOptions {

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

		actions.Wait(2000); // 2 seconds

		actions.Alt_F_O();

		actions.Wait(2000); // 2 seconds

		MessageBox2 mb = new MessageBox2(Constants.PROGRAM_NAME, 3.0);
		String text = mb.getText();
		System.out.println(text);

		if (!text.contains(salvarOsArquivosEm)) {
			System.out.println("salvarOsArquivosEm");
			actions.Move(850, 412);
			actions.Click();
			actions.Wait(1000);
			actions.Ctrl_A();
			actions.Paste(salvarOsArquivosEm);
			changed = true;

		}
		if (!text.contains(criarSubDiretorio)) {
			System.out.println("criarSubDiretorio");
			actions.Move(788, 453);
			actions.Click();
			actions.Wait(1000);
			changed = true;

		}
		if (!text.contains(numeroDownloads)) {
			System.out.println("numeroDownloads");
			actions.Move(970, 510);
			actions.Click();
			actions.Wait(1000);
			actions.Ctrl_A();
			actions.Paste("5");
			actions.Enter();
			changed = true;

		}
		if (!text.contains(salvarLog)) {
			System.out.println("salvarLog");
			actions.Move(788, 613);
			actions.Click();
			actions.Wait(1000);
			actions.Move(810, 640);
			actions.Click();
			actions.Wait(1000);
			actions.Ctrl_A();
			actions.Paste(salvarOsArquivosEm + "\\receitanetbx.log");
			changed = true;
		}
		if (changed) {
			System.out.println("Salvar");
			actions.Move(992, 669);
			actions.Click();

			actions.Wait(2000);

			System.out.println("OK");
			actions.Move(956, 542);
			actions.Click();

		} else {
			System.out.println("Cancelar");
			actions.Move(1100, 669);
			actions.Wait(1000);
			actions.Click();
		}

	}
}
