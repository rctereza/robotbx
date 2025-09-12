package com.rctereza.robotbx.tools;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Actions {

	private static final int delay = 1000; 

	private final Robot robot;

	public Actions() throws AWTException {
		robot = new Robot();
	}

	public void Wait() {
		robot.delay(delay * 2);
	}

	public void Move(int x, int y) {
		robot.mouseMove(x, y);
		robot.delay(delay);
	}

	public void Click() {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Left-click
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(delay);
	}

	public void Type(String text) {
		for (char c : text.toCharArray()) {
			robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
			robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
			robot.delay(delay / 2);
		}
	}

	public void Paste(String text) {
		ClipboardUtils.clearClipboard();
		ClipboardUtils.copyToClipboard(text);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.delay(delay);
	}
	
	public void Tab() {
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
		robot.delay(delay);
	}

}
