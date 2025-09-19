package com.rctereza.robotbx;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

	private Constants() {}

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	public static final String SOFTWARE_NAME = "Receitanet BX Robot";
	public static final String SOFTWARE_VERSION = "1.00";

	public static final String PROGRAM_NAME = "Receitanet BX";
	public static final String PROGRAM_PATH = "C:\\Program Files (x86)\\Programas RFB\\Receitanet BX";
	public static final String PROGRAM_COMMAND = "\"C:\\Program Files (x86)\\Programas RFB\\Receitanet BX\\ReceitanetBX.exe\"";
	public static final String PROGRAM_CERTIFICATES = "C:\\Temp\\java\\certificates";
	public static final String PROGRAM_PERIOD_START = "01012020";
	public static final String PROGRAM_PERIOD_END = getCurrentDate();

	private static String getCurrentDate() {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		return formatter.format(currentDate);
	}
}
