package com.rctereza.robotbx.ztest;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class MonitorInfo {

	public static void main(String[] args) {
		// Get local graphics environment
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		// Get all monitors (screen devices)
		GraphicsDevice[] screens = ge.getScreenDevices();

		// Get the default (primary) screen device
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();

		System.out.println("Number of monitors: " + screens.length);

		for (int i = 0; i < screens.length; i++) {
			GraphicsDevice screen = screens[i];
			DisplayMode dm = screen.getDisplayMode();
			int width = dm.getWidth();
			int height = dm.getHeight();

			boolean isPrimary = (screen.equals(defaultScreen));

			System.out.println("Monitor " + (i + 1) + ": " + width + "x" + height + (isPrimary ? " [Primary]" : ""));
		}
		
	}

}
