package com.rctereza.robotbx;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.exceptions.ErrorSavingSecureFile;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.tools.CryptoUtils;
import com.rctereza.robotbx.tools.Scheme;
import com.rctereza.robotbx.views.MainForm;
import com.rctereza.robotbx.wrappers.AppData;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static final Main instance = new Main();

	private static AppData appData;

	private MainForm mainForm;

	private Main() {
	}

	private static Main getInstance() {
		return instance;
	}

	private void showApp() throws ErrorSavingSecureFile {
		mainForm.setVisible(true);
		mainForm.init();
	}

	private void hideApp() {
		mainForm.setVisible(false);
		Window[] windows = JWindow.getWindows();
		for (int i = 0; i < windows.length; i++) {
			if (windows[i].isDisplayable()) {
				windows[i].dispose();
			}
		}
	}

	private void init() throws Exception {

		mainForm = new MainForm();

		mainForm.addObjectListener(new Listenable() {
			@Override
			public void value(Object... objs) {
				if (objs != null && objs.length > 0) {
					String action = (String) objs[0];
					if (action.equals(Menu.EXIT.getValue())) {
//						if (JOptionPane.showConfirmDialog(null, "Close the application?", "Confirm",
//								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						logger.info("Closing application...");
						mainForm.dispose();
						System.gc();
						System.exit(0);
//						}
					} else if (action.equals(Menu.RESTART.getValue())) {
						logger.info("Restarting application...");
						hideApp();
						try {
							Thread.sleep(2000);
							showApp();
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							JOptionPane.showMessageDialog(null,
									"Um erro ocorreu ao reinicilizar o aplicativo.\nFavor checar o log para mais detalhes.",
									"Erro", JOptionPane.ERROR_MESSAGE);
							System.gc();
							System.exit(0);
						}
					}
				}
			}
		});

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					showApp();
				} catch (ErrorSavingSecureFile e) {
					logger.error(e.getMessage(), e);
					JOptionPane.showMessageDialog(null,
							"Um erro ocorreu ao inicilizar o aplicativo.\nFavor checar o log para mais detalhes.",
							"Erro", JOptionPane.ERROR_MESSAGE);
					System.gc();
					System.exit(0);
				}
			}
		});

	}
	
	public static AppData getAppData() {
		if (appData == null) {
			try {
				appData = CryptoUtils.loadEncryptedGCM(Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE,
						AppData.class, AppData::new);
			} catch (ErrorSavingSecureFile e) {
				logger.error("1-Fatal Error: {}", e.getMessage(), e);
				JOptionPane.showMessageDialog(null,
						"1-Um erro fatal ocorreu e a aplicação não pode prosseguir.\n\nFavor contactar o suporte do sistema.\n[" + e.getMessage() + "]",
						"Erro", JOptionPane.ERROR_MESSAGE);
				System.gc();
				System.exit(0);
			}
		}
		return appData;
	}
	
	public static void saveAppData() {
		try {
			CryptoUtils.saveEncryptedGCM(appData, Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE);
		} catch (ErrorSavingSecureFile e) {
			logger.error("2-Fatal Error: {}", e.getMessage(), e);
			JOptionPane.showMessageDialog(null,
					"2-Um erro fatal ocorreu e a aplicação não pode prosseguir.\n\nFavor contactar o suporte do sistema.\n[" + e.getMessage() + "]",
					"Erro", JOptionPane.ERROR_MESSAGE);
			System.gc();
			System.exit(0);
		}
	}

	public static void main(String[] args) throws Exception {
		logger.info("Opening application...");
		System.setProperty("sun.java2d.uiScale", "1.0");
		System.setProperty("sun.java2d.dpiaware", "true");

		FlatRobotoFont.install();
		UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12));
		if (Scheme.isLafDark()) {
			FlatDarculaLaf.setup();
		} else {
			FlatIntelliJLaf.setup();
		}
		Main.getInstance().init();
	}
}
