package com.rctereza.robotbx.ztest;

import java.awt.Rectangle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Setting;
import com.rctereza.robotbx.models.Setting.KeepWhichFiles;
import com.rctereza.robotbx.ocr.ExtractImageText;
import com.rctereza.robotbx.ocr.TessUtils;
import com.rctereza.robotbx.threads.ProcessRobot;
import com.rctereza.robotbx.tools.Actions;

public class CheckingToolsOptions2 {

	private static final Logger logger = LoggerFactory.getLogger(ProcessRobot.class);

	private static ReceitaBx original;

//	private static String arquivosPastaRaiz = "C:\\Temp\\receitanet\\";

//	private static String salvarOsArquivosEm = arquivosPastaRaiz + "Arquivos\\";
//	private static String criarSubDiretorio = "EB Criar sub-diretório para cada tipo de arquivo.";
//	private static String numeroDownloads = "Numero de downloads simultâneos: |5";
//	private static String atualizacaoPedidos = "Atualização de pedidos (em minutos): 60";
//	private static String salvarLog = "EB Salvar log para depuração.";
//	private static String errorMsg = "Não foi possivel gerar o arquivo de log";

	public CheckingToolsOptions2() {
	}

	public CheckingToolsOptions2(ReceitaBx original) {
		CheckingToolsOptions2.original = original;
	}

	public static void main(String[] args) throws Exception {

		Actions actions = new Actions();

		actions.Wait(5000);

		Class<CheckingToolsOptions2> clazz = CheckingToolsOptions2.class;

		Constructor<CheckingToolsOptions2> constructor = clazz.getConstructor(ReceitaBx.class);

		CheckingToolsOptions2 obj = constructor.newInstance(original);

		Method method = clazz.getDeclaredMethod("CheckTOMenu", Actions.class);

		method.setAccessible(true);

		method.invoke(obj, actions);

		logger.info("Done!");
	}

	@SuppressWarnings("unused")
	private void CheckTOMenu(Actions actions) throws Exception {

//		actions.Alt_F();
//		actions.Enter();
//		
//		actions.Wait(2000);
		
		Setting obj = getSystemCurrentValues();
		
		System.out.println(obj);
		
	}
	
	@SuppressWarnings("unused")
	private void CheckTOMenu2(Actions actions) throws Exception {

//		actions.Alt_F();
//		actions.Enter();
//		
//		Rectangle rect = new Rectangle(
//		        789,   			// x
//		        408,   			// y
//		        241,   // width  = 1030 - 789 = 241
//		        21     // height = 429 - 408 = 21
//		 );
//		ExtractImageText mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
//		String text = mb.getTextFrom(rect);
////		logger.info("1-This is the current Text values: {}", text);
//	
//		rect = new Rectangle(
//				784,   // x
//		        451,   // y
//		        10,    // width  = 794 - 784 = 10
//		        11     // height = 463 - 451 = 12
//		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
//		int value = mb.countDarkPixels(rect, 100);
////		logger.info("2-This is the current Pixel count: {}\n", value);
//		
//		
//		rect = new Rectangle(
//				784,   // x
//				479,   // y
//		        10,    // width  = 794 - 784 = 10
//		        11     // height = 490 - 477 = 12
//		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
//		value = mb.countDarkPixels(rect, 100);
////		logger.info("3-This is the current Pixel count: {}\n", value);
//		
//
//		rect = new Rectangle(
//				963,   // x
//				507,   // y
//		        12,    // width  = 974 - 963 = 11
//		        16     // height = 523 - 507 = 16
//		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
//		text = mb.getTextFrom(rect);
////		logger.info("4-This is the current Text values: {}", text);
//
//		
//		rect = new Rectangle(
//				973,   // x
//				558,   // y
//		        44,    // width  = 1017 - 973 = 11
//		        16     // height = 575 - 558 = 16
//		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
//		text = mb.getTextFrom(rect);
////		logger.info("5-This is the current Text values: {}", text);
//		
//		
//		rect = new Rectangle(
//				784,   // x
//				614,   // y
//		        10,    // width  = 795 - 784 = 11
//		        11     // height = 626 - 614 = 12
//		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
//		value = mb.countDarkPixels(rect, 100);
////		logger.info("6-This is the current Pixel count: {}\n", value);
//	    
//		
//		rect = new Rectangle(
//				784,   // x
//				632,   // y
//		        256,   // width  = 1040 - 783 = 257
//		        21     // height = 654 - 632 = 22
//		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
//		text = mb.getTextFrom(rect);
////		logger.info("7-This is the current Text values: {}", text);
//		
	}
	
	private Setting getSystemCurrentValues() throws Exception {
		
		String  SOFTWARE_NAME = null;
		String  SOFTWARE_PATH = null;
		String  SOFTWARE_PROGRAM = null;
		String  DOWNLOAD_FOLDER = "";
		String  SAVE_FOLDER = null;
		String  LOG_FOLDER = "";
		Boolean SAVE_LOG = false;
		Boolean MAKE_SUBFOLDER = false;
		Boolean AUTO_DOWNLOAD = false;
		Integer NUMBER_DOWNLOAD_SIMULTANEOUS = null;
		Integer MINUTES_FOR_NEXT_ORDER_UPDATE = null;
		KeepWhichFiles KEEP_WHICH_FILES = null;
		Boolean DATA_UPDATED = null;
			
		Rectangle rect = new Rectangle(
		        789,   			// x
		        408,   			// y
		        241,   // width  = 1030 - 789 = 241
		        21     // height = 429 - 408 = 21
		 );
		ExtractImageText mb = new ExtractImageText(Constants.PROGRAM_NAME, 2.0);
		DOWNLOAD_FOLDER = mb.getTextFrom(rect, TessUtils.FIELD_TYPE.TEXT);
//		logger.info("1-This is the current Text values: {}", text);
		//System.exit(0);
		
		
		rect = new Rectangle(
				784,   // x
		        451,   // y
		        10,    // width  = 794 - 784 = 10
		        11     // height = 463 - 451 = 12
		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
		int value = mb.countDarkPixels(rect, 100);
		if (value > 30) MAKE_SUBFOLDER = true;
//		logger.info("2-This is the current Pixel count: {}\n", value);
		
		
		rect = new Rectangle(
				784,   // x
				479,   // y
		        10,    // width  = 794 - 784 = 10
		        11     // height = 490 - 477 = 12
		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
		value = mb.countDarkPixels(rect, 100);
		if (value > 30) AUTO_DOWNLOAD = true;
//		logger.info("3-This is the current Pixel count: {}\n", value);
		

		rect = new Rectangle(
				963,   // x
				507,   // y
		        12,    // width  = 974 - 963 = 11
		        16     // height = 523 - 507 = 16
		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
		String text = mb.getTextFrom(rect, TessUtils.FIELD_TYPE.NONE);
		NUMBER_DOWNLOAD_SIMULTANEOUS = Integer.parseInt(text.replaceAll("\\D", "")); 
//		logger.info("4-This is the current Text values: {}", text);

		
		rect = new Rectangle(
				973,   // x
				558,   // y
		        44,    // width  = 1017 - 973 = 11
		        16     // height = 575 - 558 = 16
		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
		text = mb.getTextFrom(rect, TessUtils.FIELD_TYPE.NONE);
		MINUTES_FOR_NEXT_ORDER_UPDATE = Integer.parseInt(text.replaceAll("\\D", "")); 
//		logger.info("5-This is the current Text values: {}", text);
		
		
		rect = new Rectangle(
				784,   // x
				614,   // y
		        10,    // width  = 795 - 784 = 11
		        11     // height = 626 - 614 = 12
		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
		value = mb.countDarkPixels(rect, 100);
		if (value > 30) SAVE_LOG = true;
//		logger.info("6-This is the current Pixel count: {}\n", value);
	    
		
		rect = new Rectangle(
				784,   // x
				632,   // y
		        256,   // width  = 1040 - 783 = 257
		        21     // height = 654 - 632 = 22
		 );
//		mb = new ExtractImageText(Constants.PROGRAM_NAME, 1.0);
		LOG_FOLDER = mb.getTextFrom(rect, TessUtils.FIELD_TYPE.TEXT);
		
		return new Setting(SOFTWARE_NAME, SOFTWARE_PATH, SOFTWARE_PROGRAM, DOWNLOAD_FOLDER,
				SAVE_FOLDER, LOG_FOLDER, SAVE_LOG, MAKE_SUBFOLDER, AUTO_DOWNLOAD, NUMBER_DOWNLOAD_SIMULTANEOUS,
				MINUTES_FOR_NEXT_ORDER_UPDATE, KEEP_WHICH_FILES,DATA_UPDATED);
	}
}
