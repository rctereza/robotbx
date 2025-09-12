package com.rctereza.robotbx.ztest;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import com.rctereza.robotbx.tools.ScreenResolution;

public class ShowMousePosition {

	private static final String resolution = ScreenResolution.getResolution();
	
	public static void main(String[] args) {
		
		while (true) {

			// Get pointer info
			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
			Point point = pointerInfo.getLocation();

			int x = (int) point.getX();
			int y = (int) point.getY();

			System.out.println("Screen Resolution: " + resolution + ", Mouse position: X=" + x + " Y=" + y);

			try {
				Thread.sleep(5000); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
