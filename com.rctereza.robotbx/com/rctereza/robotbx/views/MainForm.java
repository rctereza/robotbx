package com.rctereza.robotbx.views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.components.DarkLightSwitchIcon;
import com.rctereza.robotbx.controllers.Controller;
import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.enums.Sped;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.Scheme;
import com.rctereza.robotbx.tools.ScreenResolution;
import com.rctereza.robotbx.tools.SpedUtils;

import net.miginfocom.swing.MigLayout;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 8829044892271317875L;

	private static final String softwareNameAndVersion = Constants.SOFTWARE_NAME + " " + Constants.SOFTWARE_VERSION;
	
	private DarkLightSwitchIcon darkLightSwitchIcon;

	private JPanel panelMain;
	
	private JLabel themeLabel;
	private JToggleButton themeButton;

	private JLabel screenResolutionLabel;
	private JTextField screenResolutionTextField;

	private JLabel certificateLabel;
	private JComboBox<Certificate> certificateComboBox;
	private JButton certificateLoadButton;

	private JLabel passwordLabel;
	private JTextField passwordTextField;

	private JLabel profileLabel;
	private JRadioButton profileContribuinte;
	private JRadioButton profileProcurador;
	private JComboBox<String> profileTypeComboBox;
	private JTextField profileTypeValueTextField;

	private JLabel systemLabel;
	private JComboBox<String> systemComboBox;

	private JLabel systemFileTypeLabel;
	private JComboBox<String> systemFileTypeComboBox;

	private JLabel systemSearchTypeLabel;
	private JComboBox<String> systemSearchTypeComboBox;

	private JPanel systemSearchFieldsPanel;
	
	private JButton searchButton;

	// private static Controller controller;

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

		// LINE 0
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

		// LINE 1
		screenResolutionLabel = new JLabel("Monitor Resolução");
		screenResolutionTextField = new JTextField();
		screenResolutionTextField.setText(ScreenResolution.getResolution());
		screenResolutionTextField.setEditable(false);

		// LINE 2
		certificateLabel = new JLabel("Selecione um certificado");
		certificateComboBox = new JComboBox<>();
		certificateComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Certificate certificate = (Certificate) e.getItem();
					passwordTextField.setText(certificate.FILE_PASS());
				}
			}
		});

		certificateLoadButton = new JButton("Carregar");
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

		// LINE 3
		passwordLabel = new JLabel("Senha do certificado");
		passwordTextField = new JTextField();

		// LINE 4
		profileLabel = new JLabel("Selecione um perfil");
		profileContribuinte = new JRadioButton("Contribuinte", true);
		profileContribuinte.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				profileTypeComboBox.setVisible(false);
				profileTypeValueTextField.setVisible(false);
			}
		});

		profileProcurador = new JRadioButton("Procurador");
		profileProcurador.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				profileTypeComboBox.setVisible(true);
				profileTypeValueTextField.setVisible(true);
			}
		});

		ButtonGroup profileGroup = new ButtonGroup();
		profileGroup.add(profileContribuinte);
		profileGroup.add(profileProcurador);

		String[] profileTypes = { "CPF", "CNPJ" };
		profileTypeComboBox = new JComboBox<String>(profileTypes);
		profileTypeComboBox.setVisible(false);

		profileTypeValueTextField = new JTextField();
		profileTypeValueTextField.setVisible(false);

		// LINE 5
		systemLabel = new JLabel("Selecione um sistema");
		systemComboBox = new JComboBox<String>(SpedUtils.getSystemList());
		systemComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Sped value = Sped.getSped(e.getItem().toString());

					DefaultComboBoxModel<String> modelForSystemFileType = new DefaultComboBoxModel<>();
					modelForSystemFileType.addAll(SpedUtils.getSystemFileTypeList(value));
					systemFileTypeComboBox.removeAllItems();
					systemFileTypeComboBox.setModel(modelForSystemFileType);
					systemFileTypeComboBox.setSelectedIndex(0);
				}
			}
		});

		//LINE 6
		systemFileTypeLabel = new JLabel("Selecione um tipo de arquivo");
		systemFileTypeComboBox = new JComboBox<String>(SpedUtils.getSystemFileType(Sped.CONTRIBUICOES));
		systemFileTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Sped value = Sped.getSped(systemComboBox.getSelectedItem().toString());
					
					DefaultComboBoxModel<String> modelForSystemSearchType = new DefaultComboBoxModel<>();
					modelForSystemSearchType.addAll(SpedUtils.getSystemSearchTypeList(value,e.getItem().toString()));
					systemSearchTypeComboBox.removeAllItems();
					systemSearchTypeComboBox.setModel(modelForSystemSearchType);
					systemSearchTypeComboBox.setSelectedIndex(0);
				}
			}
		});
		
		//LINE 7
		systemSearchTypeLabel = new JLabel("Selecione um tipo de pesquisa");
		systemSearchTypeComboBox = new JComboBox<String>(SpedUtils.getSystemSearchType(Sped.CONTRIBUICOES, ""));
		systemSearchTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Sped system = Sped.getSped(systemComboBox.getSelectedItem().toString());
					String systemFileType = systemFileTypeComboBox.getSelectedItem().toString();
					
					panelMain.remove(systemSearchFieldsPanel);
					systemSearchFieldsPanel = SpedUtils.getSearchFields(system, systemFileType, e.getItem().toString());
					panelMain.add(systemSearchFieldsPanel, "cell 0 9, span, grow, wrap");
					panelMain.revalidate();
					panelMain.repaint();
				}
			}
		});
	
		//LINE 8
		JSeparator horizontalLine = new JSeparator(JSeparator.HORIZONTAL);
		
		//LINE 9
		systemSearchFieldsPanel = SpedUtils.getSearchFields(Sped.CONTRIBUICOES, "", "");
		
		//LINE 10
		searchButton = new JButton("Pesquisar");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button clicked!");
			}
		});

//		JPanel panel = new JPanel(new MigLayout("wrap, insets 10, debug", "[]10[]10[]", "[]10[]10[]"));
		panelMain = new JPanel(new MigLayout("", "[]10[]10[]", "[] [] []"));

		panelMain.add(themeLabel, "split, span3, right");
		panelMain.add(themeButton, "wrap");
		//panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "split, span, growx, pushx, wrap");
		panelMain.add(screenResolutionLabel, "left, sg 1");
		panelMain.add(screenResolutionTextField, "pushx, growx, wrap");
		panelMain.add(certificateLabel, "left, sg 1");
		panelMain.add(certificateComboBox, "pushx, growx");
		panelMain.add(certificateLoadButton, "left, wrap");
		panelMain.add(passwordLabel, "left, sg 1");
		panelMain.add(passwordTextField, "pushx, growx, wrap");
		panelMain.add(profileLabel, "left, sg 1");
		panelMain.add(profileContribuinte, "split");
		panelMain.add(profileProcurador);
		panelMain.add(profileTypeComboBox);
		panelMain.add(profileTypeValueTextField, "pushx, growx, wrap");
		panelMain.add(systemLabel, "left, sg 1");
		panelMain.add(systemComboBox, "wrap");
		panelMain.add(systemFileTypeLabel, "left, sg 1");
		panelMain.add(systemFileTypeComboBox, "wrap");
		panelMain.add(systemSearchTypeLabel, "left, sg 1");
		panelMain.add(systemSearchTypeComboBox, "wrap");
		panelMain.add(horizontalLine, "span, grow, wrap");
		panelMain.add(systemSearchFieldsPanel, "cell 0 9, span, grow, wrap");
		panelMain.add(searchButton, "cell 0 10, wrap");

		TitledBorder title;
		title = BorderFactory.createTitledBorder("Pesquisa de Arquivos");
		panelMain.setBorder(title);
		
		this.add(panelMain);

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
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						FlatAnimatedLafChange.showSnapshot();
						FlatIntelliJLaf.setup();
						FlatLaf.updateUI();
						FlatAnimatedLafChange.hideSnapshotWithAnimation();
					}
				});
			} else {
				Scheme.setLafDark();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						FlatAnimatedLafChange.showSnapshot();
						FlatDarculaLaf.setup();
						FlatLaf.updateUI();
						FlatAnimatedLafChange.hideSnapshotWithAnimation();
					}
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
