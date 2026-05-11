package com.rctereza.robotbx.tools;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;

public class ScreenResolution {
	
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