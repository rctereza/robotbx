package com.rctereza.robotbx.ocr;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinNT.HRESULT;

public class WindowDimensions {
	
	private static final Logger logger = LoggerFactory.getLogger(WindowDimensions.class);

	private Dimension dimension;
	private Rectangle rectangle;

	public interface Dwmapi extends Library {
		Dwmapi INSTANCE = Native.load("dwmapi", Dwmapi.class);

		int DWMWA_EXTENDED_FRAME_BOUNDS = 9;

		HRESULT DwmGetWindowAttribute(HWND hwnd, int dwAttribute, RECT rect, int cbAttribute);
	}

	public WindowDimensions(String windowTitle) throws Exception {
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

	private void findDimensions(String windowTitle) throws Exception {

		HWND hwnd = User32.INSTANCE.FindWindow(null, windowTitle);

		if (hwnd == null || hwnd.getPointer() == null) {
			
			logger.warn("Failed to find a window. [" + windowTitle + "]");
			
		} else {

			RECT rect = new RECT();

			// Try DWM first (more accurate)
			HRESULT result = Dwmapi.INSTANCE.DwmGetWindowAttribute(hwnd, Dwmapi.DWMWA_EXTENDED_FRAME_BOUNDS, rect,
					rect.size());

			if (result.intValue() == 0) { // S_OK

				int width = rect.right - rect.left;
				int height = rect.bottom - rect.top;

				dimension = new Dimension(width, height);
				rectangle = new Rectangle(rect.left, rect.top, width, height);

			} else {
				// Fallback to GetWindowRect if DWM fails
				if (User32.INSTANCE.GetWindowRect(hwnd, rect)) {

					int width = rect.right - rect.left;
					int height = rect.bottom - rect.top;

					dimension = new Dimension(width, height);
					rectangle = new Rectangle(rect.left, rect.top, width, height);

				} else {
					
					logger.warn("Failed to get window dimensions. [" + windowTitle + "]");
					
				}
			}
		}
	}
}
