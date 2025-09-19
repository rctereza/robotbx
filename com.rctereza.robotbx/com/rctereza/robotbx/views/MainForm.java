package com.rctereza.robotbx.views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.components.DarkLightSwitchIcon;
import com.rctereza.robotbx.controllers.Controller;
import com.rctereza.robotbx.enums.MenuOption;
import com.rctereza.robotbx.interfaces.Listenable;

import net.miginfocom.swing.MigLayout;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 8829044892271317875L;

	private static final String softwareNameAndVersion = Constants.SOFTWARE_NAME + " " + Constants.SOFTWARE_VERSION;

	private DarkLightSwitchIcon darkLightSwitchIcon;

	private JLabel themeLabel;
	private JToggleButton themeButton;

	private JLabel certificateLabel;
	private JTextField certificateField;
	private JButton certificateButton;

//	private static Controller controller;

	private Listenable listener;

	public MainForm(Controller controller) {
		super(softwareNameAndVersion);

//		MainForm.controller = controller;

		darkLightSwitchIcon = new DarkLightSwitchIcon();
		darkLightSwitchIcon.setCenterSpace(20);

		themeLabel = new JLabel("Switch Themes ");
		
		themeButton = new JToggleButton();
		themeButton.setIcon(darkLightSwitchIcon);

		themeButton.putClientProperty(FlatClientProperties.STYLE,
				"" + "arc:999;" + "borderWidth:0;" + "focusWidth:0;" + "innerFocusWidth:0");

		themeButton.addActionListener(new ActionListener() {
			private final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
			private ScheduledFuture<?> scheduledFuture;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (scheduledFuture != null) {
					scheduledFuture.cancel(true);
				}
				scheduledFuture = scheduled.schedule(() -> {
					changeThemes(themeButton.isSelected());
				}, 500, TimeUnit.MILLISECONDS);
			}
		});

		certificateLabel = new JLabel("Certificate(s) Folder:");
		
		certificateField = new JTextField();
		certificateField.setEditable(false);

		certificateButton = new JButton("Choose");
		certificateButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Select a folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFolder = chooser.getSelectedFile();
				certificateField.setText(selectedFolder.getAbsolutePath());
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				listener.value(MenuOption.CLOSE.getValue());
			}

			public void windowIconified(WindowEvent e) {
				listener.value(MenuOption.MINIMIZE.getValue());
			}
		});


//		JPanel panel = new JPanel(new MigLayout("wrap, insets 10, debug", "[]10[]10[]", "[]10[]10[]"));
		JPanel panel = new JPanel(new MigLayout("","[]10[]10[]", "[] [] []"));
		
		panel.add(themeLabel, "split, span3, right");
		panel.add(themeButton, "wrap");
		panel.add(certificateLabel, "left, sg 1");
		panel.add(certificateField, "pushx, growx");
		panel.add(certificateButton, "left, wrap");
		
		this.add(panel);

		setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public void addObjectListener(Listenable listener) {
		this.listener = listener;
	}

	private void changeThemes(boolean dark) {
		if (FlatLaf.isLafDark() != dark) {
			if (!dark) {
				EventQueue.invokeLater(() -> {
					FlatAnimatedLafChange.showSnapshot();
					FlatIntelliJLaf.setup();
					FlatLaf.updateUI();
					FlatAnimatedLafChange.hideSnapshotWithAnimation();
				});
			} else {
				EventQueue.invokeLater(() -> {
					FlatAnimatedLafChange.showSnapshot();
					FlatDarculaLaf.setup();
					FlatLaf.updateUI();
					FlatAnimatedLafChange.hideSnapshotWithAnimation();
				});
			}
		}
	}
}
