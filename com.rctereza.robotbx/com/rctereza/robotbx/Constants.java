package com.rctereza.robotbx;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

	private Constants() {}

	public static final boolean DEBUG = true;
	
	public static final int WINDOW_WIDTH = 900;
	public static final int WINDOW_HEIGHT = 550;

	public static final String SOFTWARE_NAME = "Receitanet BX Robô";
	public static final String SOFTWARE_VERSION = "1.00";

	public static final String PROGRAM_NAME = "Receitanet BX";
	public static final String PROGRAM_PATH = "C:\\Program Files (x86)\\Programas RFB\\Receitanet BX";
	public static final String PROGRAM_COMMAND = "\"C:\\Program Files (x86)\\Programas RFB\\Receitanet BX\\receitanetbx-gui-1.9.24.jar\"";
	public static final String PROGRAM_CERTIFICATES = "C:\\Temp\\java\\certificates";
	public static final String PROGRAM_PERIOD_START = "01/01/2020";
	public static final String PROGRAM_PERIOD_END = getCurrentDate();
	
	public static final String CERTIFICATE_PATH = "certificatePath";
	public static final String SCHEME_THEME = "schemeTheme";

	private static String getCurrentDate() {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(currentDate);
	}
}
