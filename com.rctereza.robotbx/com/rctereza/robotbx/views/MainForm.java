package com.rctereza.robotbx.views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
import com.rctereza.robotbx.enums.Sped;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.Scheme;
import com.rctereza.robotbx.tools.ScreenResolution;
import com.rctereza.robotbx.enums.Menu;

import net.miginfocom.swing.MigLayout;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 8829044892271317875L;

	private static final String softwareNameAndVersion = Constants.SOFTWARE_NAME + " " + Constants.SOFTWARE_VERSION;

	private DarkLightSwitchIcon darkLightSwitchIcon;

	private JLabel screenResolutionLabel;
	private JTextField screenResolutionTextField;
	
	private JLabel themeLabel;
	private JToggleButton themeButton;

	private JLabel certificateLabel;
	private JComboBox<Certificate> certificateComboBox;
	private JButton certificateLoadButton;

	private JLabel passwordLabel;
	private JTextField passwordTextField;
	
	private JLabel profileLabel;
	private JRadioButton profileContribuinte;
	private JRadioButton profileProcurador;

	private JLabel reportLabel;
	private JRadioButton reportSpedContribuicoes;
	private JRadioButton reportSpedContabil;
	private JRadioButton reportSpedECF;
	private JRadioButton reportSpedEFD;
	private JRadioButton reportSpedFiscal;
	
	private JButton runButton;

	//	private static Controller controller;

	private Listenable listener;

	public MainForm(Controller controller) {
		super(softwareNameAndVersion);

//		MainForm.controller = controller;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				listener.value(Menu.CLOSE.getValue());
			}

			public void windowIconified(WindowEvent e) {
				listener.value(Menu.MINIMIZE.getValue());
			}
		});
		
		//LINE 0
		darkLightSwitchIcon = new DarkLightSwitchIcon();
		darkLightSwitchIcon.setCenterSpace(20);

		themeLabel = new JLabel("Switch Themes");
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
		
		if (Scheme.isLafDark()) {
			themeButton.setSelected(true);
		}

		//LINE 1
		screenResolutionLabel = new JLabel("Screen Resoltuion");
		screenResolutionTextField = new JTextField();
		screenResolutionTextField.setText(ScreenResolution.getResolution());
		screenResolutionTextField.setEditable(false);
		
		//LINE 2
		certificateLabel = new JLabel("Certificate");

		certificateComboBox = new JComboBox<>();
		certificateComboBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Certificate certificate = (Certificate) e.getItem();
				passwordTextField.setText(certificate.FILE_PASS());
			}
		});

		certificateLoadButton = new JButton("Load");
		certificateLoadButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Select a folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFolder = chooser.getSelectedFile();
				loadCertificateComboBox(selectedFolder.getAbsolutePath());
			}
		});

		loadCertificateComboBox(FileUtils.getCertificatePathSaved());

		//LINE 3
		passwordLabel = new JLabel("Password");
		passwordTextField = new JTextField();
		
		//LINE 4
		profileLabel = new JLabel("Profile");
		profileContribuinte = new JRadioButton("Contribuinte", true);
		profileProcurador = new JRadioButton("Procurador");

		ButtonGroup profileGroup = new ButtonGroup();
		profileGroup.add(profileContribuinte);
		profileGroup.add(profileProcurador);

		//LINE 5
		reportLabel = new JLabel("Report");
		reportSpedContribuicoes = new JRadioButton(Sped.CONTRIBUICOES.getValue(), true);
		reportSpedContabil= new JRadioButton(Sped.CONTABIL.getValue());
		reportSpedECF= new JRadioButton(Sped.ECF.getValue());
		reportSpedEFD= new JRadioButton(Sped.EFD.getValue());
		reportSpedFiscal= new JRadioButton(Sped.FISCAL.getValue());
		
		ButtonGroup reportGroup = new ButtonGroup();
		reportGroup.add(reportSpedContribuicoes);
		reportGroup.add(reportSpedContabil);
		reportGroup.add(reportSpedECF);
		reportGroup.add(reportSpedEFD);
		reportGroup.add(reportSpedFiscal);

		//LINE 6
		runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button clicked!");
            }
        });
		
//		JPanel panel = new JPanel(new MigLayout("wrap, insets 10, debug", "[]10[]10[]", "[]10[]10[]"));
		JPanel panel = new JPanel(new MigLayout("", "[]10[]10[]", "[] [] []"));

		panel.add(themeLabel, "split, span3, right");
		panel.add(themeButton, "wrap");

		panel.add(screenResolutionLabel, "left, sg 1");
		panel.add(screenResolutionTextField, "pushx, growx, wrap");
		panel.add(certificateLabel, "left, sg 1");
		panel.add(certificateComboBox, "pushx, growx");
		panel.add(certificateLoadButton, "left, wrap");
		panel.add(passwordLabel, "left, sg 1");
		panel.add(passwordTextField, "pushx, growx, wrap");
		panel.add(profileLabel, "left, sg 1");
		panel.add(profileContribuinte, "split");
		panel.add(profileProcurador, "wrap");
		panel.add(reportLabel, "left, sg 1");
		panel.add(reportSpedContribuicoes, "split");
		panel.add(reportSpedContabil);
		panel.add(reportSpedECF);
		panel.add(reportSpedEFD);
		panel.add(reportSpedFiscal, "wrap");
		panel.add(runButton, "cell 1 6, wrap");
		
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
				Scheme.removeLafDark();
				EventQueue.invokeLater(() -> {
					FlatAnimatedLafChange.showSnapshot();
					FlatIntelliJLaf.setup();
					FlatLaf.updateUI();
					FlatAnimatedLafChange.hideSnapshotWithAnimation();
				});
			} else {
				Scheme.setLafDark();
				EventQueue.invokeLater(() -> {
					FlatAnimatedLafChange.showSnapshot();
					FlatDarculaLaf.setup();
					FlatLaf.updateUI();
					FlatAnimatedLafChange.hideSnapshotWithAnimation();
				});
			}
		}
	}

	private void loadCertificateComboBox(String path) {
		if (path != null && !path.trim().isEmpty()) {
			List<Certificate> list = FileUtils.getListOfCertificates(path);
			if (list.size() > 0) {
				FileUtils.saveCertificatePathChosen(path);
				DefaultComboBoxModel<Certificate> model = new DefaultComboBoxModel<>();
				model.addAll(list);
				certificateComboBox.removeAllItems();
				certificateComboBox.setModel(model);
			}
		}
	}

}
