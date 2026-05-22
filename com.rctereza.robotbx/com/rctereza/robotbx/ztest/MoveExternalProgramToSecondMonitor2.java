package com.rctereza.robotbx.ztest;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser;

public class MoveExternalProgramToSecondMonitor2 {

	public static void main(String[] args) throws Exception {
		
		String windowTitle = "Receitanet BX";

		// Detect monitors
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();

//		if (screens.length < 2) {
//		    System.out.println("Only one monitor detected.");
//		    return;
//		}

		int count = 0;
		Rectangle targetMonitorBounds = null;
		
		for (GraphicsDevice screen : screens) {

		    Rectangle bounds = screen.getDefaultConfiguration().getBounds();

		    System.out.println(count++ + 
		            " - Monitor found: "
		            + bounds.width + "x" + bounds.height
		            + " at X=" + bounds.x
		            + " Y=" + bounds.y
		    );

		    if (bounds.width == 1920 && bounds.height == 1080) {
		        targetMonitorBounds = bounds;
		        System.out.println("1920x1080 monitor found!");
		        break;
		    }
		}
		
		// Second monitor
		//Rectangle bounds = screens[1].getDefaultConfiguration().getBounds();

		System.out.println("Monitor[" + count + "] bounds: " + targetMonitorBounds);

		// Find window
		User32 user32 = User32.INSTANCE;
		HWND hwnd = user32.FindWindow(null, windowTitle);

		if (hwnd == null) {
		    System.out.println("Window not found: " + windowTitle);
		    return;
		}

		// Restore if minimized
		user32.ShowWindow(hwnd, WinUser.SW_RESTORE);

		// Bring to foreground
		user32.SetForegroundWindow(hwnd);

		// Small delay to allow redraw
		Thread.sleep(300);

		// Get window rectangle
		RECT rect = new RECT();

		boolean result = user32.GetWindowRect(hwnd, rect);

		if (!result) {
		    System.out.println("Failed to get window rect.");
		    return;
		}

		// Window size
		int windowWidth = rect.right - rect.left;
		int windowHeight = rect.bottom - rect.top;

		System.out.println("Window Width : " + windowWidth);
		System.out.println("Window Height: " + windowHeight);

		// Center on second monitor
		int x = targetMonitorBounds.x + (targetMonitorBounds.width - windowWidth) / 2;
		int y = targetMonitorBounds.y + (targetMonitorBounds.height - windowHeight) / 2;

		// Move window
		boolean moved = user32.MoveWindow(
		        hwnd,
		        x,
		        y,
		        windowWidth,
		        windowHeight,
		        true
		);

		// Restore if minimized
		user32.ShowWindow(hwnd, WinUser.SW_RESTORE);

		// Bring to foreground
		user32.SetForegroundWindow(hwnd);
		
		if (moved) {
		    System.out.println("Window centered on second monitor.");
		} else {
		    System.out.println("Failed to move window.");
		}
	}
}
