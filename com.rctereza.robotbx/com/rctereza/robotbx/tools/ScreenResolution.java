package com.rctereza.robotbx.tools;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

public class ScreenResolution {

	private static final Logger logger = LoggerFactory.getLogger(ScreenResolution.class);
	
	private static Rectangle targetMonitorBounds = null;
	
    public interface MyUser32 extends StdCallLibrary {
        MyUser32 INSTANCE = Native.load("user32", MyUser32.class);
        int GetDpiForWindow(HWND hwnd);
    }
    
	public static int getWidth() {
		return User32.INSTANCE.GetSystemMetrics(0); // SM_CXSCREEN
	}

	public static int getHeight() {
		return User32.INSTANCE.GetSystemMetrics(1); // SM_CYSCREEN
	}

	public static String getResolution() {
		return String.valueOf(getWidth()) + "x" + String.valueOf(getHeight());
	}

	public static String getResolution(GraphicsDevice gd) {
		Rectangle r = gd.getDefaultConfiguration().getBounds();
		return String.valueOf(r.width) + "x" + String.valueOf(r.height);
	}

	public static String getDisplayLabel(Window window) {

		String result = "Resolução do monitor (";

		GraphicsDevice gd = getCurrentMonitor(window);

		result += gd.getIDstring().replace("\\", "") + " " + getDpiScaling(gd) + ")";

		return result;
	}

	public static GraphicsDevice getCurrentMonitor(Window window) {

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		GraphicsDevice[] screens = ge.getScreenDevices();

		Rectangle windowBounds = window.getBounds();

		for (GraphicsDevice screen : screens) {

			Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();

			if (screenBounds.intersects(windowBounds)) {
				return screen;
			}
		}

		return ge.getDefaultScreenDevice();
	}

	public static String getDpiScaling(GraphicsDevice gd) {
		String result = "";
		
		Window window = getWindow(gd);
		
		HWND hwnd = new HWND(Native.getComponentPointer(window));

        int dpi = MyUser32.INSTANCE.GetDpiForWindow(hwnd);

//		System.out.println("DPI: " + dpi);

		double scale = dpi / 96.0;

//		System.out.println("Scale: " + scale);
		
		if (scale == 1.0) {
			result += "100%";
		} else if (scale == 1.25) {
			result += "125%";
		} else if (scale == 1.5) {
			result += "150%";
		} else if (scale == 1.75) {
			result += "175%";
		} else if (scale == 2.0) {
			result += "200%";
		} else {
			result += ">200%";
		}
		
		return result;
	}
	
	public static boolean moveAppTo1920x1080Monitor() {
		boolean result = true;
		
		// Detect monitors
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();

		targetMonitorBounds = null;
		
		int count = 0;
		for (GraphicsDevice screen : screens) {
		    Rectangle bounds = screen.getDefaultConfiguration().getBounds();
		    if (bounds.width == 1920 && bounds.height == 1080) {
		        targetMonitorBounds = bounds;
		        break;
		    }
		    count++;
		}
		
		if (targetMonitorBounds == null) {
		    logger.warn("1920x1080 screen could not be found!");
			return false;
		}
		
		// Find window
		User32 user32 = User32.INSTANCE;
		HWND hwnd = user32.FindWindow(null, Constants.PROGRAM_NAME);

		if (hwnd == null) {
		    logger.warn("Window not found: {}", Constants.PROGRAM_NAME);
		    return false;
		}

		// Restore if minimized
		user32.ShowWindow(hwnd, WinUser.SW_RESTORE);

		// Bring to foreground
		user32.SetForegroundWindow(hwnd);

		// Small delay to allow redraw
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Get window rectangle
		RECT rect = new RECT();

		boolean window = user32.GetWindowRect(hwnd, rect);

		if (!window) {
			logger.warn("Failed to get window rect.");
		    return false;
		}

		// Window size
		int windowWidth = rect.right - rect.left;
		int windowHeight = rect.bottom - rect.top;

//		System.out.println("Window Width : " + windowWidth);
//		System.out.println("Window Height: " + windowHeight);

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
			logger.info("Window centered on monitor [{}].", count);
		} else {
			logger.warn("Failed to move window.");
			result = false;
		}
		
		return result;
	}

	public static Rectangle getTargetMonitorBounds() {
		return targetMonitorBounds;
	}
	
	private static Window getWindow(GraphicsDevice gd) {
		Window result = null;
		
		for (Window w : Window.getWindows()) {

		    if (!w.isShowing()) {
		        continue;
		    }

		    GraphicsDevice device =
		        w.getGraphicsConfiguration()
		         .getDevice();

		    if (device.equals(gd)) {
		    	result = w;
		        //System.out.println("Found window: " + w);
		    }
		}
		
		return result;
	}
	
}