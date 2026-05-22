package com.rctereza.robotbx;

public class Constants {

	private Constants() {}

	public static final boolean DEBUG = false;
	
	public static final String SOFTWARE_NAME = "Receitanet BX Robô";
	public static final String SOFTWARE_VERSION = "1.00";
	public static final String SOFTWARE_WORK_FOLDER = System.getenv("LOCALAPPDATA") + "\\Robotbx"; // C:\Users\UserName\AppData\Local\Robotbx
		
	public static final String SOFTWARE_SECRET = "Z*mon5xuR3c3i7aBx";
	public static final String SOFTWARE_SECURE_FILE = SOFTWARE_WORK_FOLDER + "\\data.sec";
	
	public static final String OCR_FILE_NAME = "text_extracted.txt";
	public static final String OCR_IMAGE_NAME = "image_captured.png";
	public static final String OCR_SYSTEM_PATH = SOFTWARE_WORK_FOLDER + "\\ocr\\";

	public static final String PROGRAM_NAME = "Receitanet BX";
	public static final String PROGRAM_PATH = "C:\\Program Files (x86)\\Programas RFB\\Receitanet BX";
	public static final String PROGRAM_COMMAND = "C:\\Program Files (x86)\\Programas RFB\\Receitanet BX\\receitanetbx-gui-1.9.24.jar";

	public static final String CERTIFICATE_PATH = "certificatePath";
	public static final String SOFTWARE_PATH = "softwarePath";
	public static final String SCHEME_THEME = "schemeTheme";
}
