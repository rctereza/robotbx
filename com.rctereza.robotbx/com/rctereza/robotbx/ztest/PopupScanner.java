package com.rctereza.robotbx.ztest;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;

public class PopupScanner {

	public static void main(String[] args) {
		User32 user32 = User32.INSTANCE;

		user32.EnumWindows((hWnd, data) -> {
			char[] windowText = new char[512];
			user32.GetWindowText(hWnd, windowText, 512);
			String wText = Native.toString(windowText).trim();

			if (!wText.isEmpty()) {
				System.out.println("Found window: " + wText);

				// Enumerate child windows (controls inside the popup)
				user32.EnumChildWindows(hWnd, (child, d) -> {
					char[] buffer = new char[512];
					user32.GetClassName(child, buffer, 512);
					String className = Native.toString(buffer).trim();

					if ("Static".equals(className)) {
						char[] textBuf = new char[512];
						user32.GetWindowText(child, textBuf, 512);
						String message = Native.toString(textBuf).trim();
						if (!message.isEmpty()) {
							System.out.println("   Message: " + message);
						}
					}
					return true;
				}, Pointer.NULL);
			}

			return true; // continue enumeration
		}, Pointer.NULL);
	}
}
