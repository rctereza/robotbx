package com.rctereza.robotbx.ztest;

import java.awt.Dimension;

import com.rctereza.robotbx.models.Robot;

public class Calculation {

	public static void main(String[] args) {

		Integer x = 810;
		Robot robot = new Robot(1, "Robot for 1920x1080 screen resolution", 1080, 1920, true, null);

		Float relativeX = (x.floatValue() / robot.SCREEN_WIDTH());
		System.out.println(relativeX);

		Dimension monitor = new Dimension(1366, 768);
		int newX = (int) (relativeX * monitor.width);
		System.out.println(newX);
	}

}
