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

	private static String arquivosPastaRaiz = "C:\\Temp\\ReceitanetBX\\";

	private static String salvarOsArquivosEm = arquivosPastaRaiz + "Arquivos\\";
	private static String criarSubDiretorio = "EB Criar sub-diretório para cada tipo de arquivo.";
	private static String numeroDownloads = "Numero de downloads simultâneos: |5";
	private static String atualizacaoPedidos = "Atualização de pedidos (em minutos): 60";
	private static String salvarLog = "EB Salvar log para depuração.";
	private static String errorMsg = "Não foi possivel gerar o arquivo de log";

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

		actions.Wait(1000);

		actions.Alt_F_O();

		actions.Wait(1000);

		ExtractImageText mb = new ExtractImageText(Constants.PROGRAM_NAME, 2.0);
		String text = mb.getText(1, 1);
		logger.info("This is the current Text values: {}", text);

		mb = new ExtractImageText(Constants.PROGRAM_NAME, 4.0);
		String text2 = mb.getText(1, 1);
		logger.info("This is the current Text2 values: {}", text2);

		if (!text.toUpperCase().contains(salvarOsArquivosEm.toUpperCase())) {
			logger.info("01 - Changing 'Salvar os arquivos em' to [{}]", salvarOsArquivosEm);
			actions.Ctrl_A();
			actions.Paste(salvarOsArquivosEm);
			actions.Wait(1000);
			changed = true;

		}

		actions.Tab();
		actions.Tab();

		if (!text2.contains(criarSubDiretorio)) {
			logger.info("02 - Selecting 'Criar sub-diretório para cada tipo de arquivo'.");
			actions.SpaceBar();
			actions.Wait(1000);
			changed = true;

		}

		actions.Tab();
		actions.Tab();

		if (!text2.contains(numeroDownloads)) {
			logger.info("03 - Setting 'Número de downloads simultâneos:' to [5].");
			actions.Ctrl_A();
			actions.Paste("5");
			actions.Wait(1000);
			changed = true;

		}

		actions.Tab();

		if (!text2.contains(atualizacaoPedidos)) {
			logger.info("04 - Setting 'Atualização de pedidos (em minutos)' to [60].");
			actions.Ctrl_A();
			actions.Paste("60");
			actions.Wait(1000);
			changed = true;

		}

		actions.Tab();

		if (!text2.contains(salvarLog)) {
			logger.info("05 - Selecting 'Salvar log para depuração.");
			actions.SpaceBar();
			actions.Wait(1000);
			actions.Tab();

			logger.info("06 - Changing the path to [{}].", arquivosPastaRaiz + "receitanetbx.log");
			actions.Ctrl_A();
			actions.Paste(arquivosPastaRaiz + "receitanetbx.log");
			actions.Wait(1000);
			changed = true;
		} else {
			actions.Tab();
		}

		if (changed) {
			System.out.println("Salvar");
			actions.Tab();
			actions.Tab();
			actions.Tab();
			actions.SpaceBar();
			actions.Wait(1000);

			mb = new ExtractImageText(Constants.PROGRAM_NAME, 2.0);
			text2 = mb.getText(1, 1);
			//logger.info("This is the current Text2 values: {}", text2);

			if (text2.contains(errorMsg)) {

				//actions.SpaceBar();
				logger.warn("ATTENTION! " + errorMsg + "... [" + arquivosPastaRaiz + "receitanetbx.log]");
				
			} else {

				actions.SpaceBar();
				logger.info("Parameters fixed with success.");

			}

		} else {
			System.out.println("Cancelar");
			actions.Tab();
			actions.Tab();
			actions.Tab();
			actions.Tab();
			actions.SpaceBar();

			logger.info("Parameters already fixed. No change needed.");
		}

	}
}
