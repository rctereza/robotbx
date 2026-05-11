package com.rctereza.robotbx.ocr;

import java.awt.Dimension;
import java.awt.Rectangle;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HMONITOR;
import com.sun.jna.platform.win32.WinUser.MONITORINFO;

public class WindowDimensions {

//	private static final Logger logger = LoggerFactory.getLogger(WindowDimensions.class);

	private Dimension monitor;
	private Dimension dimension;
	private Rectangle rectangle;

	public interface Dwmapi extends Library {
		Dwmapi INSTANCE = Native.load("dwmapi", Dwmapi.class);

		int DWMWA_EXTENDED_FRAME_BOUNDS = 9;

		HRESULT DwmGetWindowAttribute(HWND hwnd, int dwAttribute, RECT rect, int cbAttribute);
	}

	public WindowDimensions(String windowTitle) throws Exception {
		findDimensions(windowTitle);
		findMonitor(windowTitle);
	}

	public Dimension getMonitor() {
		return monitor;
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

			throw new Exception("Failed to find a window. [" + windowTitle + "]");

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

					throw new Exception("Failed to get window dimensions. [" + windowTitle + "]");

				}
			}
		}
	}

	private void findMonitor(String windowTitle) {

		HWND hwnd = User32.INSTANCE.FindWindow(null, windowTitle);

		if (hwnd == null) {
			System.out.println("Window not found");
			return;
		}

		// Get monitor from window
		HMONITOR hmonitor = User32.INSTANCE.MonitorFromWindow(hwnd, WinUser.MONITOR_DEFAULTTONEAREST);

		// Retrieve monitor information
		MONITORINFO monitorInfo = new MONITORINFO();

		User32.INSTANCE.GetMonitorInfo(hmonitor, monitorInfo);

		// Monitor bounds
//		int x = monitorInfo.rcMonitor.left;
//		int y = monitorInfo.rcMonitor.top;
//		boolean primary = (monitorInfo.dwFlags & WinUser.MONITORINFOF_PRIMARY) != 0;

		int width = monitorInfo.rcMonitor.right - monitorInfo.rcMonitor.left;

		int height = monitorInfo.rcMonitor.bottom - monitorInfo.rcMonitor.top;

		monitor = new Dimension(width, height);

//		System.out.println("Monitor position: " + x + "," + y);
//		System.out.println("Monitor size: " + width + "x" + height);
//		System.out.println("Primary monitor: " + primary);
	}
}
