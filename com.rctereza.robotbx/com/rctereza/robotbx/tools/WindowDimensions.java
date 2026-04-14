package com.rctereza.robotbx.tools;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public class WindowDimensions {

	private Dimension dimension;
	private Rectangle rectangle;

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

	public WindowDimensions() {
	}

	public WindowDimensions(String windowTitle) {
		findDimensions(windowTitle);
	}

	public void calculate(String windowTitle) {
		findDimensions(windowTitle);
	}
	
	public Dimension getDimension() {
		return dimension;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	@Override
	public String toString() {
		return "WindowDimensions [dimension=" + dimension + ", rectangle=" + rectangle + "]";
	}

	private void findDimensions(String windowTitle) {
		int hwnd = User32.INSTANCE.FindWindowA(null, windowTitle);
		if (hwnd != 0) {
			RECT rect = new RECT();
			if (User32.INSTANCE.GetWindowRect(hwnd, rect)) {
				int width = rect.right - rect.left;
				int height = rect.bottom - rect.top;
				dimension = new Dimension(width, height);
				rectangle = new Rectangle(rect.left, rect.top, width, height);
			} else {
				System.out.println("Failed to get window dimensions. [" + windowTitle + "]");
			}
		} else {
			System.out.println("Window not found. [" + windowTitle + "]");
		}
	}
}
