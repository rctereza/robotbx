package com.rctereza.robotbx;

public class Constants {

	private Constants() {}

	public static final boolean DEBUG = false;
	
	public static final int WINDOW_WIDTH = 1200;
	public static final int WINDOW_HEIGHT = 850;

	public static final String SOFTWARE_NAME = "Receitanet BX Robô";
	public static final String SOFTWARE_VERSION = "1.00";
	
	public static final String SOFTWARE_SECRET = "Z*mon5xuR3c3i7aBx";
	public static final String SOFTWARE_SECURE_FILE = System.getenv("LOCALAPPDATA") + "\\Robotbx\\data.sec";

	public static final String PROGRAM_NAME = "Receitanet BX";
	public static final String PROGRAM_PATH = "C:\\Program Files (x86)\\Programas RFB\\Receitanet BX";
	public static final String PROGRAM_COMMAND = "\"C:\\Program Files (x86)\\Programas RFB\\Receitanet BX\\receitanetbx-gui-1.9.24.jar\"";
	public static final String PROGRAM_COMMAND2 = "\"C:\\Program Files (x86)\\Programas RFB\\Receitanet BX\\ReceitanetBX.exe\"";
	public static final String PROGRAM_CERTIFICATES = "C:\\Temp\\Certificados";
	public static final String PROGRAM_DOWNLOADED_FOLDER = "C:\\Temp\\ReceitanetBX";
	
//	public static final String PROGRAM_PERIOD_START = "01/01/2020";
//	public static final String PROGRAM_PERIOD_END = getCurrentDate();
	
	public static final String CERTIFICATE_PATH = "certificatePath";
	public static final String SCHEME_THEME = "schemeTheme";

	// C:\Users\UserName\AppData\Local\robotbx\ocr
	public static final String OCR_SYSTEM_PATH = System.getenv("LOCALAPPDATA") + "\\Robotbx\\ocr\\";
	public static final String OCR_FILE_NAME = "text_extracted.txt";
	public static final String OCR_IMAGE_NAME = "image_captured.png";

//	private static String getCurrentDate() {
//		Date currentDate = new Date();
//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//		return formatter.format(currentDate);
//	}
}
