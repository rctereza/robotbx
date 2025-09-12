package com.rctereza.robotbx.tools;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public class WindowDimensions {

	private int width;
	private int height;

	public interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.load("user32", User32.class);

		int FindWindowA(String lpClassName, String lpWindowName);

		boolean GetWindowRect(int hwnd, RECT rect);
	}

	// Define RECT structure for window position and size
	public static class RECT extends Structure {
		public int left;
		public int top;
		public int right;
		public int bottom;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("left", "top", "right", "bottom");
		}
	}
	
	public WindowDimensions(String windowTitle) {
		int hwnd = User32.INSTANCE.FindWindowA(null, windowTitle);
		if (hwnd != 0) {
			RECT rect = new RECT();
			if (User32.INSTANCE.GetWindowRect(hwnd, rect)) {
				width = rect.right - rect.left;
				height = rect.bottom - rect.top;
			} else {
				System.out.println("Failed to get window dimensions. [" + windowTitle + "]");
			}
		} else {
			System.out.println("Window not found. [" + windowTitle + "]");
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
