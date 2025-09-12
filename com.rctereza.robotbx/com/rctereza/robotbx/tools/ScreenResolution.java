package com.rctereza.robotbx.tools;

import com.sun.jna.platform.win32.User32;

public class ScreenResolution {

	public static int getWidth() {
		return User32.INSTANCE.GetSystemMetrics(0); // SM_CXSCREEN
	}

	public static int getHeight() {
		return User32.INSTANCE.GetSystemMetrics(1); // SM_CYSCREEN
	}

	public static String getResolution() {
		return String.valueOf(getWidth()) + "x" + String.valueOf(getHeight());
	}
}
