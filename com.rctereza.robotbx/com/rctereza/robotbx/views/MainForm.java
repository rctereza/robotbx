package com.rctereza.robotbx.views;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

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
import com.rctereza.robotbx.exceptions.InvalidScreenResolution;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.Scheme;
import com.rctereza.robotbx.tools.ScreenResolution;
import com.rctereza.robotbx.tools.SpedUtils;
import com.rctereza.robotbx.tools.ValidateCpfCnpj;

import net.miginfocom.swing.MigLayout;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 8829044892271317875L;

	private static final String softwareNameAndVersion = Constants.SOFTWARE_NAME + " " + Constants.SOFTWARE_VERSION;

	private static Controller controller;

	private MaskFormatter cnpjMask;
	private MaskFormatter cpfMask;

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
	private JFormattedTextField profileTypeValueTextField;

	private JLabel systemLabel;
	private JComboBox<String> systemComboBox;

	private JLabel systemFileTypeLabel;
	private JComboBox<String> systemFileTypeComboBox;

	private JLabel systemSearchTypeLabel;
	private JComboBox<String> systemSearchTypeComboBox;

	private JPanel systemSearchFieldsPanel;

	private JButton searchButton;
	private JButton closeButton;

	private ReceitaBx receitaBx = FileUtils.loadReceitaBx();

	private Listenable listener;

	public MainForm(Controller controller) throws ParseException {
		super(softwareNameAndVersion);

		MainForm.controller = controller;
		
		MainForm.controller.addObjectListener(new Listenable() {
			@Override
			public void value(Object... objs) {
				if (objs != null && objs.length > 0) {
					String action = (String) objs[0];
					if (action.equals(Menu.DONE.getValue())) {
						searchButton.setEnabled(true);
					}
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				listener.value(Menu.CLOSE.getValue());
			}

			public void windowIconified(WindowEvent e) {
				listener.value(Menu.MINIMIZE.getValue());
			}
		});

		// FileUtils.removeCertificatePathChosen();

		cnpjMask = new MaskFormatter("##.###.###/####-##");
		cnpjMask.setPlaceholderCharacter('_');

		cpfMask = new MaskFormatter("###.###.###-##");
		cpfMask.setPlaceholderCharacter('_');

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
				/*
				 * if (e.getStateChange() == ItemEvent.SELECTED) { Certificate certificate =
				 * (Certificate) e.getItem();
				 * passwordTextField.setText(certificate.FILE_PASS()); }
				 */
			}
		});

		certificateLoadButton = new JButton("Carregar");
		certificateLoadButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Selecione a pasta do(s) certificado(s)");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFolder = chooser.getSelectedFile();
				loadCertificateComboBox(selectedFolder.getAbsolutePath());
			}
		});

		loadCertificateComboBox(FileUtils.getCertificatePathSaved());
		certificateComboBox.setSelectedItem(receitaBx.CERTIFICADO());

		// LINE 3
		passwordLabel = new JLabel("Senha do certificado");
		passwordTextField = new JTextField();
		passwordTextField.setText(receitaBx.SENHA());

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
		profileTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					profileTypeValueTextField.setValue(null);
					if (profileTypeComboBox.getSelectedItem().toString().equals("CNPJ"))
						profileTypeValueTextField.setFormatterFactory(new DefaultFormatterFactory(cnpjMask));
					else
						profileTypeValueTextField.setFormatterFactory(new DefaultFormatterFactory(cpfMask));
				}
			}
		});

		profileTypeValueTextField = new JFormattedTextField(cpfMask);
		profileTypeValueTextField.setVisible(false);

		if (receitaBx.PERFIL() != null && receitaBx.PERFIL().equals(profileProcurador.getText())) {
			profileProcurador.setSelected(true);
			profileTypeComboBox.setVisible(true);
			profileTypeValueTextField.setVisible(true);
			profileTypeComboBox.setSelectedItem(receitaBx.PERFIL_TYPE());
			profileTypeValueTextField.setValue(receitaBx.PERFIL_VALUE());
		}

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

		// LINE 6
		systemFileTypeLabel = new JLabel("Selecione um tipo de arquivo");
		systemFileTypeComboBox = new JComboBox<String>(SpedUtils.getSystemFileType(Sped.CONTRIBUICOES));
		systemFileTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Sped value = Sped.getSped(systemComboBox.getSelectedItem().toString());

					DefaultComboBoxModel<String> modelForSystemSearchType = new DefaultComboBoxModel<>();
					modelForSystemSearchType.addAll(SpedUtils.getSystemSearchTypeList(value, e.getItem().toString()));
					systemSearchTypeComboBox.removeAllItems();
					systemSearchTypeComboBox.setModel(modelForSystemSearchType);
					systemSearchTypeComboBox.setSelectedIndex(0);
				}
			}
		});

		// LINE 7
		systemSearchTypeLabel = new JLabel("Selecione um tipo de pesquisa");
		systemSearchTypeComboBox = new JComboBox<String>(SpedUtils.getSystemSearchType(Sped.CONTRIBUICOES, ""));
		systemSearchTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Sped system = Sped.getSped(systemComboBox.getSelectedItem().toString());
					String systemFileType = systemFileTypeComboBox.getSelectedItem().toString();
					if (systemSearchFieldsPanel != null && systemSearchFieldsPanel.isShowing()) {
						panelMain.remove(systemSearchFieldsPanel);
						try {
							systemSearchFieldsPanel = SpedUtils.getSearchFields(system, systemFileType,
									e.getItem().toString(), receitaBx);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						panelMain.add(systemSearchFieldsPanel, "cell 0 9, span, grow, wrap");
						panelMain.revalidate();
						panelMain.repaint();
					}
					else {
						try {
							systemSearchFieldsPanel = SpedUtils.getSearchFields(Sped.getSped(receitaBx.SISTEMA()), receitaBx.TIPO_ARQUIVO(),
									receitaBx.TIPO_PESQUISA(), receitaBx);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		// LINE 8
		JSeparator horizontalLine = new JSeparator(JSeparator.HORIZONTAL);

		// LINE 9
		systemSearchFieldsPanel = SpedUtils.getSearchFields(Sped.CONTRIBUICOES, "", "", receitaBx);

		systemComboBox.setSelectedItem(receitaBx.SISTEMA());
		systemFileTypeComboBox.setSelectedItem(receitaBx.TIPO_ARQUIVO());
		systemSearchTypeComboBox.setSelectedItem(receitaBx.TIPO_PESQUISA());

		// LINE 10
		searchButton = new JButton("Pesquisar");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = validateFormFields();
				if (result.equals("")) {
					try {
						searchButton.setEnabled(false);
						MainForm.controller.startThreads(receitaBx);
					} catch (AWTException | InterruptedException | InvalidScreenResolution e1) {
//						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, result, "Atenção", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		closeButton = new JButton("Sair");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.value(Menu.CLOSE.getValue());
			}
		});

//		JPanel panel = new JPanel(new MigLayout("wrap, insets 10, debug", "[]10[]10[]", "[]10[]10[]"));
		panelMain = new JPanel(new MigLayout("", "[]10[]10[]", "[] [] []"));

		panelMain.add(themeLabel, "split, span3, right");
		panelMain.add(themeButton, "wrap");
		panelMain.add(screenResolutionLabel, "left, sg 1");
		panelMain.add(screenResolutionTextField, "pushx, growx, wrap");
		panelMain.add(certificateLabel, "left, sg 1");
		panelMain.add(certificateComboBox, "growx, w ::600");
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
		panelMain.add(searchButton, "cell 0 10");
		panelMain.add(closeButton, "cell 2 10, left, wrap");

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

	private String validateFormFields() {
		StringBuilder result = new StringBuilder("");

		String SCREEN = screenResolutionTextField.getText();
		Certificate CERTIFICADO = null;
		String SENHA = "";
		String PERFIL = "";
		String PERFIL_TYPE = "";
		String PERFIL_VALUE = "";
		String SISTEMA = systemComboBox.getSelectedItem().toString();
		String TIPO_ARQUIVO = systemFileTypeComboBox.getSelectedItem().toString();
		String TIPO_PESQUISA = systemSearchTypeComboBox.getSelectedItem().toString();
		String DATA_INICIO = "";
		String DATA_FIM = "";
		String CNPJ_INCORPORADORA = "";
		String TIPO_EVENTO = "";
		String BAIXAR_ARQUIVO_ASSINADO = "";
		String CNPJ_ESTABELECIMENTO = "";
		Boolean BUSCAR_TODOS_ESTABLECIMENTOS = false;
		String INSCRICAO_ESTADUAL = "";
		Boolean ULTIMO_ARQUIVO_TRANSMITIDO = false;

		if (certificateComboBox.getSelectedIndex() == -1)
			result.append("Favor selecionar um certificado.\n");
		else
			CERTIFICADO = (Certificate) certificateComboBox.getSelectedItem();

		if (passwordTextField.getText().isBlank())
			result.append("Favor informar a senha do certificado.\n");
		else
			SENHA = passwordTextField.getText();

		if (profileProcurador.isSelected()) {

			PERFIL = profileProcurador.getText();
			PERFIL_TYPE = profileTypeComboBox.getSelectedItem().toString();
			PERFIL_VALUE = profileTypeValueTextField.getText();

			if (PERFIL_TYPE.equals("CPF")) {
				if (!ValidateCpfCnpj.isCpfValid(PERFIL_VALUE)) {
					result.append("Favor informar um CPF válido.\n");
				}
			} else {
				if (!ValidateCpfCnpj.isCnpjValid(PERFIL_VALUE)) {
					result.append("Favor informar um CNPJ válido.\n");
				}
			}
		} else {
			PERFIL = profileContribuinte.getText();
		}

		for (Component c : systemSearchFieldsPanel.getComponents()) {
			if (c instanceof JTextField) {
				JTextField textField = (JTextField) c;
//				System.out.println("JTextField : " + textField.getText());
				if (textField.getName().equals("DATA_INICIO")) {
					DATA_INICIO = textField.getText();
				} else if (textField.getName().equals("DATA_FIM")) {
					DATA_FIM = textField.getText();
				} else if (textField.getName().equals("CNPJ_INCORPORADORA")) {
					CNPJ_INCORPORADORA = textField.getText();
				} else if (textField.getName().equals("CNPJ_ESTABELECIMENTO")) {
					CNPJ_ESTABELECIMENTO = textField.getText();
				} else if (textField.getName().equals("INSCRICAO_ESTADUAL")) {
					INSCRICAO_ESTADUAL = textField.getText();
				}
			} else if (c instanceof JComboBox) {
				JComboBox<?> comboBox = (JComboBox<?>) c;
//				System.out.println("JComboBox : " + comboBox.getSelectedItem().toString());
				if (comboBox.getName().equals("TIPO_EVENTO")) {
					TIPO_EVENTO = comboBox.getSelectedItem().toString();
				} else if (comboBox.getName().equals("BAIXAR_ARQUIVO_ASSINADO")) {
					BAIXAR_ARQUIVO_ASSINADO = comboBox.getSelectedItem().toString();
				}
			} else if (c instanceof JCheckBox) {
				JCheckBox checkBox = (JCheckBox) c;
//				System.out.println("JCheckBox : " + checkBox.getText());
				if (checkBox.getName().equals("BUSCAR_TODOS_ESTABLECIMENTOS")) {
					BUSCAR_TODOS_ESTABLECIMENTOS = checkBox.isSelected();
				} else if (checkBox.getName().equals("ULTIMO_ARQUIVO_TRANSMITIDO")) {
					ULTIMO_ARQUIVO_TRANSMITIDO = checkBox.isSelected();
				}
			}
		}

		if (result.isEmpty()) {
			receitaBx = new ReceitaBx(SCREEN, CERTIFICADO, SENHA, PERFIL, PERFIL_TYPE, PERFIL_VALUE, SISTEMA,
					TIPO_ARQUIVO, TIPO_PESQUISA, DATA_INICIO, DATA_FIM, CNPJ_INCORPORADORA, TIPO_EVENTO,
					BAIXAR_ARQUIVO_ASSINADO, CNPJ_ESTABELECIMENTO, BUSCAR_TODOS_ESTABLECIMENTOS, INSCRICAO_ESTADUAL,
					ULTIMO_ARQUIVO_TRANSMITIDO);

			FileUtils.saveReceitaBx(receitaBx);
		}

		return result.toString();
	}

}
