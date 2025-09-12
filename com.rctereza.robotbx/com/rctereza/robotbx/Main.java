package com.rctereza.robotbx;

import java.awt.AWTException;

import com.rctereza.robotbx.controllers.Controller;

public class Main {
	
	private static final Main instance = new Main();
	
	private Controller controller;
	
	private Main() {
	}

	public static Main getInstance() {
		return instance;
	}
	
	private void init() throws AWTException, InterruptedException {
		controller = new Controller();
		controller.startThreads();
	}
	
	public static void main(String[] args) throws AWTException, InterruptedException {
		Main.getInstance().init();
	}
}
