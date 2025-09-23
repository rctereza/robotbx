package com.rctereza.robotbx;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.rctereza.robotbx.controllers.Controller;
import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.tools.Scheme;
import com.rctereza.robotbx.views.MainForm;

public class Main {

	private static final Main instance = new Main();

	private Controller controller;
	
	private MainForm mainForm;

	private Main() {
	}

	public static Main getInstance() {
		return instance;
	}

	private void showApp() {
		mainForm.setVisible(true);
	}
	
	private void init() throws AWTException, InterruptedException {
		
		controller = new Controller();
//		controller.startThreads();
		
		mainForm = new MainForm(controller);
		
		mainForm.addObjectListener(new Listenable() {
			@Override
			public void value(Object... objs) {
				if (objs != null && objs.length > 0) {
					String action = (String) objs[0];
					if (action.equals(Menu.CLOSE.getValue())) {
//						if (JOptionPane.showConfirmDialog(null, "Close the application?", "Confirm",
//								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							mainForm.dispose();
							System.gc();
							System.exit(0);
//						}
					}
				}
			}
		});
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				showApp();
			}
		});
		
	}

	public static void main(String[] args) throws AWTException, InterruptedException {
		FlatRobotoFont.install();
		UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12));
		if (Scheme.isLafDark()) {
			FlatDarculaLaf.setup();
		}
		else {
			FlatIntelliJLaf.setup();
		}
		Main.getInstance().init();
	}

}
