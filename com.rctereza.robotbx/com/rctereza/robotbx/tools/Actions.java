package com.rctereza.robotbx.tools;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Actions {

	private static final int defaultDelay = 1000; 

	private final Robot robot;

	public Actions() throws AWTException {
		robot = new Robot();
	}

	public void Wait(Integer milliSeconds) {
		robot.delay(milliSeconds);
	}

	public void Move(int x, int y) {
		robot.mouseMove(x, y);
		robot.delay(defaultDelay);
	}

	public void Click() {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Left-click
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(defaultDelay);
	}

	public void Type(String text) {
		for (char c : text.toCharArray()) {
			robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
			robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
			robot.delay(300);
		}
		robot.delay(defaultDelay);
	}

	public void Paste(String text) {
		ClipboardUtils.clearClipboard();
		ClipboardUtils.copyToClipboard(text);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.delay(defaultDelay);
	}
	
	public void Tab() {
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.delay(defaultDelay);
	}

	public void Enter() {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(defaultDelay);
	}
	
	public void SpaceBar() {
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.delay(defaultDelay);
	}
	
	public void AltArrowDown() {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.delay(defaultDelay);
	}

	public void ArrowDown() {
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.delay(defaultDelay);
	}
	
	public void ArrowUp() {
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.delay(defaultDelay);
	}
	
	public void Alt_F_O() {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F);
        robot.keyRelease(KeyEvent.VK_F);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.delay(300);
        robot.keyPress(KeyEvent.VK_O);
        robot.keyRelease(KeyEvent.VK_O);
        robot.delay(defaultDelay);
	}
	
	public void Ctrl_A() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(defaultDelay);
	}
}
