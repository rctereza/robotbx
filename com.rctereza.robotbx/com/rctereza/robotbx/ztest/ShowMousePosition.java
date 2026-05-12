package com.rctereza.robotbx.ztest;

import java.awt.MouseInfo;
import java.awt.Point;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HMONITOR;
import com.sun.jna.platform.win32.WinUser.MONITORINFO;

public class ShowMousePosition {

	public static void main(String[] args) {

		while (true) {

			// Get pointer info
			// Java mouse position
			Point mousePoint = MouseInfo.getPointerInfo().getLocation();

			// Convert to Win32 POINT
			POINT.ByValue winPoint = new POINT.ByValue();

			winPoint.x = mousePoint.x;
			winPoint.y = mousePoint.y;

			// Get monitor containing mouse
			HMONITOR monitor = User32.INSTANCE.MonitorFromPoint(winPoint, WinUser.MONITOR_DEFAULTTONEAREST);

			// Retrieve monitor information
			MONITORINFO monitorInfo = new MONITORINFO();

			User32.INSTANCE.GetMonitorInfo(monitor, monitorInfo);

			int width = monitorInfo.rcMonitor.right - monitorInfo.rcMonitor.left;

			int height = monitorInfo.rcMonitor.bottom - monitorInfo.rcMonitor.top;

			boolean primary = (monitorInfo.dwFlags & WinUser.MONITORINFOF_PRIMARY) != 0;

			float relativeX = (float) mousePoint.x / width;
			float relativeY = (float) mousePoint.y / height;

			System.out.println("Mouse position: " + mousePoint.x + "," + mousePoint.y + " - Mouse is on monitor: "
					+ width + "x" + height + " - Primary monitor: " + primary + " - RelativeX=" + relativeX
					+ ", RelativeY=" + relativeY);

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
