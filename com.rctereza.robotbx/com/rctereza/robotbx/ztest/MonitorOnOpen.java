package com.rctereza.robotbx.ztest;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class MonitorOnOpen {

	public static void main(String[] args) {
		// Get all monitors
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();

		// Choose a monitor (for example, monitor 2 if it exists)
		int monitorIndex = 1; // 0 = first (primary), 1 = second, etc.
		
		if (monitorIndex >= screens.length) {
			System.out.println("Monitor " + (monitorIndex + 1) + " not found. Using primary instead.");
			monitorIndex = 0;
		}

		GraphicsDevice chosenMonitor = screens[monitorIndex];

		// Get the bounds (position and size) of that monitor
		Rectangle bounds = chosenMonitor.getDefaultConfiguration().getBounds();

		// Create a JFrame on that monitor
		JFrame frame = new JFrame("Opened on Monitor " + (monitorIndex + 1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);

		// Place it in the top-left corner of the chosen monitor
//		frame.setLocation(bounds.x, bounds.y);

		//Place it on the center of the chosen monitor
		int x = bounds.x + (bounds.width - frame.getWidth()) / 2;
		int y = bounds.y + (bounds.height - frame.getHeight()) / 2;
		frame.setLocation(x, y);


		frame.setVisible(true);
	}

}
