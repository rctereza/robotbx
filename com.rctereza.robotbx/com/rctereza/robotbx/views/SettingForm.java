package com.rctereza.robotbx.views;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.Main;
import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.Setting;
import com.rctereza.robotbx.models.Setting.KeepWhichFiles;
import com.rctereza.robotbx.tools.FileUtils;

import net.miginfocom.swing.MigLayout;

public class SettingForm extends JDialog {

	private static final long serialVersionUID = -8935336038076796989L;

	private static final String FOLDER_TOOLTIP = "Clique duas vezes sobre este campo para acessar o diretório informado nele.";

	private static final Logger logger = LoggerFactory.getLogger(SettingForm.class);

	private JPanel panelMain;

	private JLabel softwareNameLabel;
	private JTextField softwareNameTextField;

	private JLabel softwareLocationLabel;
	private JTextField softwareLocationTextField;
	private JButton softwareLocationSelectButton;

	private JLabel softwareLocationJarLabel;
	private JComboBox<String> softwareLocationJarComboBox;

	private JLabel filesDownloadedLocationLabel;
	private JTextField filesDownloadLocationTextField;
	private JLabel filesDownloadLocationHelperLabel;
	private JButton filesDownloadedLocationButton;

	// ---------------------------------------------------------------------------------------
	// private JLabel makeSubFolderForEachFileTypeDefaultLabel;
	private JCheckBox makeSubFolderForEachFileTypeDefaultCheckBox;

	// private JLabel downloadFilesAutomaticallyDefaultLabel;
	private JCheckBox downloadFilesAutomaticallyDefaultCheckBox;

	private JLabel numberOfSimultaneousDownloadsDefaultLabel;
	private JSpinner numberOfSimultaneousDownloadsDefaultSpinner;

	private JLabel orderUpdatesPerMinuteDefaultLabel;
	private JSpinner orderUpdatesPerMinuteDefaultSpinner;

	// ---------------------------------------------------------------------------------------
	// private JLabel makeSubFolderForEachFileTypeRecommendedLabel;
	private JCheckBox makeSubFolderForEachFileTypeRecommendedCheckBox;

	// private JLabel downloadFilesAutomaticallyRecommendedLabel;
	private JCheckBox downloadFilesAutomaticallyRecommendedCheckBox;

	private JLabel numberOfSimultaneousDownloadsRecommendedLabel;
	private JSpinner numberOfSimultaneousDownloadsRecommendedSpinner;

	private JLabel orderUpdatesPerMinuteRecommendedLabel;
	private JSpinner orderUpdatesPerMinuteRecommendedSpinner;

	private JLabel saveLogForDebuggingLabel;
	private JTextField fileLogLocationTextField;
	private JLabel fileLogLocationHelperLabel;
	private JButton fileLogLocationButton;

	private JLabel moveFileToNewLocationAfterConclusionLabel;
	private JTextField moveFileToNewLocationAfterConclusionTextField;
	private JLabel moveFileToNewLocationAfterConclusionHelperLabel;
	private JButton moveFileToNewLocationAfterConclusionButton;

	private JLabel filesToKeepAfterConclusionLabel;
	private JRadioButton filesToKeepAfterConclusionAll;
	private JRadioButton filesToKeepAfterConclusionLastRectification;

	private JRadioButton defaultSettingRadio;
	private JRadioButton recommendedSettingRadio;

	private JButton closeButton;
	private JButton saveButton;

	private Setting originalSetting;

	private Boolean firstAccess;

	private Listenable listener;

	public SettingForm(Window parent, Menu menu) {
		super(parent, menu.getValue(), ModalityType.APPLICATION_MODAL);

		// setIconImage(Resources.getImage(Constants.SOFTWARE_ICON, null));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (!firstAccess) {
					listener.value(Menu.CLOSE.getValue());
				}
			}
		});

		originalSetting = loadSetting();

		softwareNameLabel = new JLabel("Nome do Software:");
		softwareNameTextField = new JTextField();
		softwareNameTextField.setText(originalSetting.SOFTWARE_NAME());
		softwareNameTextField.setEnabled(false);

		softwareLocationLabel = new JLabel("Diretório de Instalação:");

		softwareLocationTextField = new JTextField();
		softwareLocationTextField.setText(originalSetting.SOFTWARE_PATH());	
		softwareLocationTextField.setEnabled(false);
		softwareLocationTextField.setToolTipText(FOLDER_TOOLTIP);
		softwareLocationTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!softwareLocationTextField.getText().isEmpty()) {
						FileUtils.openFolderWithExplorer(softwareLocationTextField.getText());
					}
				}
			}
		});

		softwareLocationSelectButton = new JButton("Selecionar");
		softwareLocationSelectButton.setPreferredSize(new Dimension(80, 20));
		softwareLocationSelectButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Selecione o diretório de instalação do software");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				softwareLocationTextField.setText(chooser.getSelectedFile().getAbsolutePath());

				softwareLocationJarComboBox.removeAllItems();
				softwareLocationJarComboBox
						.setModel(FileUtils.getModelOfSoftwares(softwareLocationTextField.getText()));
				if (softwareLocationJarComboBox.getModel().getSize() > 0) {
					softwareLocationJarComboBox.setSelectedIndex(0);
				}
			}
		});

		softwareLocationJarLabel = new JLabel("Arquivo de Inicialização:");
		softwareLocationJarComboBox = new JComboBox<>();
		softwareLocationJarComboBox.setModel(FileUtils.getModelOfSoftwares(originalSetting.SOFTWARE_PATH()));
		softwareLocationJarComboBox.setSelectedItem(originalSetting.SOFTWARE_PROGRAM());

		filesDownloadedLocationLabel = new JLabel("Diretório do Download:");

		filesDownloadLocationTextField = new JTextField();
		filesDownloadLocationTextField.setText(originalSetting.DOWNLOAD_FOLDER());
		filesDownloadLocationTextField.setEnabled(false);
		filesDownloadLocationTextField.setToolTipText(FOLDER_TOOLTIP);
		filesDownloadLocationTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!filesDownloadLocationTextField.getText().isEmpty()) {
						FileUtils.openFolderWithExplorer(filesDownloadLocationTextField.getText());
					}
				}
			}
		});

		filesDownloadedLocationButton = new JButton("Selecionar");
		filesDownloadedLocationButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Selecione o diretório que os arquivos serão baixados");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				filesDownloadLocationTextField.setText(chooser.getSelectedFile().getAbsolutePath() + "\\");
				// fileLogLocationTextField.setText(filesDownloadLocationTextField.getText() +
				// "\\receitanetbx.log");
			}
		});

		// makeSubFolderForEachFileTypeDefaultLabel = new JLabel("");
		makeSubFolderForEachFileTypeDefaultCheckBox = new JCheckBox("Criar sub-diretório para cada tipo de arquivo.");
		makeSubFolderForEachFileTypeDefaultCheckBox.setSelected(false);
		makeSubFolderForEachFileTypeDefaultCheckBox.setEnabled(false);

		// downloadFilesAutomaticallyDefaultLabel = new JLabel("");
		downloadFilesAutomaticallyDefaultCheckBox = new JCheckBox("Baixar arquivos automaticamente.");
		downloadFilesAutomaticallyDefaultCheckBox.setSelected(false);
		downloadFilesAutomaticallyDefaultCheckBox.setEnabled(false);

		numberOfSimultaneousDownloadsDefaultLabel = new JLabel(" Quantos arquivos baixar simultaneamente?");
		numberOfSimultaneousDownloadsDefaultLabel.setEnabled(false);
		numberOfSimultaneousDownloadsDefaultSpinner = new JSpinner(
				new SpinnerListModel(new Integer[] { 1, 2, 3, 4, 5 }));
		numberOfSimultaneousDownloadsDefaultSpinner.setValue(1);
		numberOfSimultaneousDownloadsDefaultSpinner.setEnabled(false);

		orderUpdatesPerMinuteDefaultLabel = new JLabel(" Minutos para esperar a próxima atualização dos pedidos?");
		orderUpdatesPerMinuteDefaultLabel.setEnabled(false);
		orderUpdatesPerMinuteDefaultSpinner = new JSpinner(
				new SpinnerListModel(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
		orderUpdatesPerMinuteDefaultSpinner.setValue(10);
		orderUpdatesPerMinuteDefaultSpinner.setEnabled(false);

		// ---------------------------------------------------------------------------------------------------------------
		// makeSubFolderForEachFileTypeRecommendedLabel = new JLabel("");
		makeSubFolderForEachFileTypeRecommendedCheckBox = new JCheckBox(
				"Criar sub-diretório para cada tipo de arquivo.");
		makeSubFolderForEachFileTypeRecommendedCheckBox.setSelected(Constants.PROGRAM_MAKE_SUBFOLDER);
		makeSubFolderForEachFileTypeRecommendedCheckBox.setEnabled(false);

		// downloadFilesAutomaticallyRecommendedLabel = new JLabel("");
		downloadFilesAutomaticallyRecommendedCheckBox = new JCheckBox("Baixar arquivos automaticamente.");
		downloadFilesAutomaticallyRecommendedCheckBox.setSelected(Constants.PROGRAM_AUTO_DOWNLOAD);
		downloadFilesAutomaticallyRecommendedCheckBox.setEnabled(false);

		numberOfSimultaneousDownloadsRecommendedLabel = new JLabel(" Quantos arquivos baixar simultaneamente?");
		numberOfSimultaneousDownloadsRecommendedLabel.setEnabled(false);
		numberOfSimultaneousDownloadsRecommendedSpinner = new JSpinner(
				new SpinnerListModel(new Integer[] { 1, 2, 3, 4, 5 }));
		numberOfSimultaneousDownloadsRecommendedSpinner.setValue(Constants.PROGRAM_NUMBER_DOWNLOAD_SIMULTANEOUS);
		numberOfSimultaneousDownloadsRecommendedSpinner.setEnabled(false);

		orderUpdatesPerMinuteRecommendedLabel = new JLabel(" Minutos para esperar a próxima atualização dos pedidos?");

		orderUpdatesPerMinuteRecommendedLabel.setEnabled(false);
		orderUpdatesPerMinuteRecommendedSpinner = new JSpinner(
				new SpinnerListModel(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 60 }));
		orderUpdatesPerMinuteRecommendedSpinner.setValue(Constants.PROGRAM_MINUTES_FOR_NEXT_ORDER_UPDATE);
		orderUpdatesPerMinuteRecommendedSpinner.setEnabled(false);

		saveLogForDebuggingLabel = new JLabel("Diretório do Log:");
		fileLogLocationTextField = new JTextField();
		fileLogLocationTextField.setText(originalSetting.LOG_FOLDER());
		fileLogLocationTextField.setEnabled(false);
		fileLogLocationTextField.setToolTipText(FOLDER_TOOLTIP);
		fileLogLocationTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!fileLogLocationTextField.getText().isEmpty()) {
						FileUtils.openFolderWithExplorer(
								fileLogLocationTextField.getText().replace("receitanetbx.log", ""));
					}
				}
			}
		});

		fileLogLocationButton = new JButton("Selecionar");
		fileLogLocationButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Selecione o diretório que o arquivo de log será criado");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getAbsolutePath() + "\\";
				if (path.contains(filesDownloadLocationTextField.getText())) {
					JOptionPane.showMessageDialog(SettingForm.this,
							"O dirétorio do Log não pode ser o mesmo que o diretório do Download.\nSelecione outro diretório!",
							"Atenção", JOptionPane.WARNING_MESSAGE);
				} else {
					fileLogLocationTextField.setText(path + "receitanetbx.log");
				}
			}
		});

		moveFileToNewLocationAfterConclusionLabel = new JLabel("Diretório do Resultado:");
		moveFileToNewLocationAfterConclusionTextField = new JTextField();
		moveFileToNewLocationAfterConclusionTextField.setText(originalSetting.SAVE_FOLDER());
		moveFileToNewLocationAfterConclusionTextField.setEnabled(false);
		moveFileToNewLocationAfterConclusionTextField.setToolTipText(FOLDER_TOOLTIP);
		moveFileToNewLocationAfterConclusionTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!moveFileToNewLocationAfterConclusionTextField.getText().isEmpty()) {
						FileUtils.openFolderWithExplorer(moveFileToNewLocationAfterConclusionTextField.getText());
					}
				}
			}
		});

		moveFileToNewLocationAfterConclusionButton = new JButton("Selecionar");
		moveFileToNewLocationAfterConclusionButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle(
					"Selecione o diretório que os arquivos serão movidos após a conslusão do processamento");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				moveFileToNewLocationAfterConclusionTextField
						.setText(chooser.getSelectedFile().getAbsolutePath() + "\\");
			}
		});

		filesToKeepAfterConclusionLabel = new JLabel("Quais arquivos manter?");
		filesToKeepAfterConclusionAll = new JRadioButton("Todos", true);
		filesToKeepAfterConclusionLastRectification = new JRadioButton("Ultima retificação");

		if (originalSetting.KEEP_WHICH_FILES() != null) {
			switch (originalSetting.KEEP_WHICH_FILES()) {
			case ALL:
				filesToKeepAfterConclusionAll.setSelected(true);
				filesToKeepAfterConclusionLastRectification.setSelected(false);
				break;
			case ONLY_AMEND:
				filesToKeepAfterConclusionAll.setSelected(false);
				filesToKeepAfterConclusionLastRectification.setSelected(true);
				break;
			default:
				break;
			}
		}

		ButtonGroup filesToKeepAfterConclusionGroup = new ButtonGroup();
		filesToKeepAfterConclusionGroup.add(filesToKeepAfterConclusionAll);
		filesToKeepAfterConclusionGroup.add(filesToKeepAfterConclusionLastRectification);

		defaultSettingRadio = new JRadioButton("Opção Padrão", true);
		recommendedSettingRadio = new JRadioButton("Opção Recomendada");

		ButtonGroup defaultSettingGroup = new ButtonGroup();
		defaultSettingGroup.add(defaultSettingRadio);
		defaultSettingGroup.add(recommendedSettingRadio);

		if (originalSetting.MAKE_SUBFOLDER()) {
			defaultSettingRadio.setSelected(false);
			recommendedSettingRadio.setSelected(true);
		}

		saveButton = new JButton("Salvar");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (AllFieldsArePopulated()) {

					boolean foundChanges = false;

					Setting setting = getList().getFirst();

					if (!originalSetting.equals(setting)) {
						foundChanges = true;
						// logger.debug("Original.: {}" , originalSetting);
						// logger.debug("NewObject: {}" , setting);
					}

					if (!firstAccess && !foundChanges) {

						JOptionPane.showMessageDialog(SettingForm.this,
								"Não há a necessidade de salvar os dados. Nenhuma configuração foi alterada.",
								"Atenção", JOptionPane.WARNING_MESSAGE);

					} else {

						saveSetting();

						listener.value(Menu.CLOSE.getValue());
					}
				} else {
					JOptionPane.showMessageDialog(SettingForm.this,
							"Você deve preencher todos os campos dessa tela para conseguir salvá-los.", "Atenção",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		closeButton = new JButton("Fechar");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean foundChanges = false;

				Setting setting = getList().getFirst();

				if (!originalSetting.equals(setting)) {
					foundChanges = true;
//					logger.debug("Original.: {}" , originalSetting);
//					logger.debug("NewObject: {}" , setting);
				}

				if (firstAccess || foundChanges) {
					String message = "Houveram alterações. Deseja sair sem salvá-las?";
					String[] options = { "Sim", "Não" };
					String defaultButton = options[1];

					if (firstAccess) {
						message = "Para seu primeiro acesso os dados precisam ser salvos. Vamos salvá-los?";
						defaultButton = options[0];
					}

					int choice = JOptionPane.showOptionDialog(SettingForm.this, // Parent component
							message, // Message
							"Confirmação", // Title
							JOptionPane.YES_NO_OPTION, // Option type
							JOptionPane.QUESTION_MESSAGE, // Message type
							null, // Icon (null for default)
							options, // Custom button labels
							defaultButton // Default button focused
					);
					if (choice == 0) { // Sim
						if (firstAccess) {
							saveSetting();
						}
						listener.value(Menu.CLOSE.getValue());
					}
				} else {
					listener.value(Menu.CLOSE.getValue());
				}
			}
		});

		filesDownloadLocationHelperLabel = new JLabel("e.g. C:\\Temp\\ReceitanetBX\\Arquivos\\");
		filesDownloadLocationHelperLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
		filesDownloadLocationHelperLabel.setFont(filesDownloadLocationHelperLabel.getFont().deriveFont(10f));

		fileLogLocationHelperLabel = new JLabel("e.g. C:\\Temp\\ReceitanetBX\\receitanetbx.log");
		fileLogLocationHelperLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
		fileLogLocationHelperLabel.setFont(fileLogLocationHelperLabel.getFont().deriveFont(10f));

		moveFileToNewLocationAfterConclusionHelperLabel = new JLabel("e.g. C:\\Temp\\ReceitanetBX\\Resultado\\");
		moveFileToNewLocationAfterConclusionHelperLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
		moveFileToNewLocationAfterConclusionHelperLabel
				.setFont(moveFileToNewLocationAfterConclusionHelperLabel.getFont().deriveFont(10f));

		// panelMain = new JPanel(new MigLayout("wrap, insets 10, debug",
		// "[]10[]10[]10[]", ""));
		panelMain = new JPanel(new MigLayout("", "[200]10[350]10[350]10[100]", ""));

		/*
		 * panelMain.add(new JLabel("1"), ""); panelMain.add(new JLabel("2"), "");
		 * panelMain.add(new JLabel("3"), ""); panelMain.add(new JLabel("4"), "wrap");
		 *
		 */

		panelMain.add(softwareNameLabel, "left, sg 1");
		panelMain.add(softwareNameTextField, "span 2, pushx, growx, wrap");

		panelMain.add(softwareLocationLabel, "left, sg 1");
		panelMain.add(softwareLocationTextField, "span 2, pushx, growx");
		panelMain.add(softwareLocationSelectButton, "left, wrap");

		panelMain.add(softwareLocationJarLabel, "left, sg 1");
		panelMain.add(softwareLocationJarComboBox, "span 2, pushx, growx, wrap");

		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap, gapy 5 5");

		panelMain.add(filesDownloadedLocationLabel, "top, left, sg 1");
		panelMain.add(filesDownloadLocationTextField, "top, span 2, split 2, flowy, pushx, growx");
		panelMain.add(filesDownloadLocationHelperLabel);
		panelMain.add(filesDownloadedLocationButton, "top, left, wrap");

		panelMain.add(saveLogForDebuggingLabel, "top, left, sg 1");
		panelMain.add(fileLogLocationTextField, "top, span 2, split 2, flowy, pushx, growx");
		panelMain.add(fileLogLocationHelperLabel);
		panelMain.add(fileLogLocationButton, "top, left, wrap");

		panelMain.add(moveFileToNewLocationAfterConclusionLabel, "top, left, sg 1");
		panelMain.add(moveFileToNewLocationAfterConclusionTextField, "top, span 2, split 2, flowy, pushx, growx");
		panelMain.add(moveFileToNewLocationAfterConclusionHelperLabel);
		panelMain.add(moveFileToNewLocationAfterConclusionButton, "top, left, wrap");

		panelMain.add(filesToKeepAfterConclusionLabel, "left, sg 1");
		panelMain.add(filesToKeepAfterConclusionAll, "split, span 3");
		panelMain.add(filesToKeepAfterConclusionLastRectification, "wrap");

		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap, gapy 5 5");

		panelMain.add(defaultSettingRadio, "left, span 2");
		panelMain.add(recommendedSettingRadio, "left, span 2, wrap");

		panelMain.add(makeSubFolderForEachFileTypeDefaultCheckBox, "left, span 2");
		panelMain.add(makeSubFolderForEachFileTypeRecommendedCheckBox, "span 2, wrap");

		panelMain.add(downloadFilesAutomaticallyDefaultCheckBox, "left, span 2");
		panelMain.add(downloadFilesAutomaticallyRecommendedCheckBox, "span 2, wrap");

		panelMain.add(numberOfSimultaneousDownloadsDefaultLabel, "left, split 2, span 2");
		panelMain.add(numberOfSimultaneousDownloadsDefaultSpinner, "");
		panelMain.add(numberOfSimultaneousDownloadsRecommendedLabel, "left, split 2, span 2");
		panelMain.add(numberOfSimultaneousDownloadsRecommendedSpinner, "wrap");

		panelMain.add(orderUpdatesPerMinuteDefaultLabel, "left, split 2, span 2");
		panelMain.add(orderUpdatesPerMinuteDefaultSpinner, "");
		panelMain.add(orderUpdatesPerMinuteRecommendedLabel, "left, split 2, span 2");
		panelMain.add(orderUpdatesPerMinuteRecommendedSpinner, "wrap");

		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap, gapy 5 5");

		panelMain.add(saveButton, "left");
		panelMain.add(closeButton, "span 3, right, wrap");

		this.add(panelMain);

		setSize(900, 550);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void addObjectListener(Listenable listener) {
		this.listener = listener;
	}

	public void load() {
		this.setVisible(true);
	}

	public void close() {
		this.setVisible(false);
		this.dispose();
	}

	private void saveSetting() {
		logger.info("Saving setting data...");

		if (Main.getAppData().getSequence(Setting.class) == 0) {
			Main.getAppData().setSequence(Setting.class, Main.getAppData().nextSequence(Setting.class));
		}

		Main.getAppData().addList(Setting.class, Main.getAppData().getSequence(Setting.class), getList());

		Main.saveAppData();

		logger.info("Setting data was saved with success.");

		JOptionPane.showMessageDialog(SettingForm.this, "Todos os dados foram salvos com sucesso.", "informação",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private Setting loadSetting() {
		List<Setting> list = Main.getAppData().getLastListAdded(Setting.class);
		if (list.size() > 0) {
			firstAccess = false;
			List<Setting> deepCopy = list.stream()
					.map(p -> new Setting(p.SOFTWARE_NAME(), p.SOFTWARE_PATH(), p.SOFTWARE_PROGRAM(),
							p.DOWNLOAD_FOLDER(), p.SAVE_FOLDER(), p.LOG_FOLDER(), p.SAVE_LOG(), p.MAKE_SUBFOLDER(),
							p.AUTO_DOWNLOAD(), p.NUMBER_DOWNLOAD_SIMULTANEOUS(), p.MINUTES_FOR_NEXT_ORDER_UPDATE(),
							p.KEEP_WHICH_FILES(), p.DATA_UPDATED()))
					.collect(Collectors.toCollection(ArrayList::new)); // collect() generates a list that can be changed
			// .toList(); // toList() generates a list that cannot be changed (immutable)
			return deepCopy.getFirst();
		} else {
			firstAccess = true;
			try {
				FileUtils.createDirectory(Constants.PROGRAM_SAVEDFILES_PATH);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return new Setting(Constants.PROGRAM_NAME, Constants.PROGRAM_PATH, Constants.PROGRAM_COMMAND,
					Constants.PROGRAM_DOCUMENTS_PATH, Constants.PROGRAM_SAVEDFILES_PATH, Constants.PROGRAM_LOG_PATH,
					true, Constants.PROGRAM_MAKE_SUBFOLDER, Constants.PROGRAM_AUTO_DOWNLOAD,
					Constants.PROGRAM_NUMBER_DOWNLOAD_SIMULTANEOUS, Constants.PROGRAM_MINUTES_FOR_NEXT_ORDER_UPDATE,
					KeepWhichFiles.ALL, false);
		}
	}

	protected boolean AllFieldsArePopulated() {
		boolean result = true;

		if ((softwareLocationTextField.getText() == null || softwareLocationTextField.getText().isBlank())
				|| (softwareLocationJarComboBox.getSelectedItem() == null
						|| softwareLocationJarComboBox.getSelectedIndex() == -1)
				|| (filesDownloadLocationTextField.getText() == null
						|| filesDownloadLocationTextField.getText().isBlank())
				|| (fileLogLocationTextField.getText() == null || fileLogLocationTextField.getText().isBlank())
				|| (moveFileToNewLocationAfterConclusionTextField.getText() == null
						|| moveFileToNewLocationAfterConclusionTextField.getText().isBlank()))
			result = false;

		return result;
	}

	private List<Setting> getList() {

		List<Setting> result = new ArrayList<>();
		
		String SOFTWARE_NAME = softwareNameTextField.getText();
		String SOFTWARE_PATH = softwareLocationTextField.getText();
		String SOFTWARE_PROGRAM = (softwareLocationJarComboBox.getSelectedItem() != null
				? softwareLocationJarComboBox.getSelectedItem().toString()
				: "");
		String DOWNLOAD_FOLDER = filesDownloadLocationTextField.getText();
		String SAVE_FOLDER = moveFileToNewLocationAfterConclusionTextField.getText();
		String LOG_FOLDER = fileLogLocationTextField.getText();
		Boolean SAVE_LOG = false;
		KeepWhichFiles KEEP_WHICH_FILES = (filesToKeepAfterConclusionAll.isSelected() ? KeepWhichFiles.ALL
				: KeepWhichFiles.ONLY_AMEND);

		Boolean MAKE_SUBFOLDER = makeSubFolderForEachFileTypeDefaultCheckBox.isSelected();
		Boolean AUTO_DOWNLOAD = downloadFilesAutomaticallyDefaultCheckBox.isSelected();
		Integer NUMBER_DOWNLOAD_SIMULTANEOUS = (Integer) numberOfSimultaneousDownloadsDefaultSpinner.getValue();
		Integer MINUTES_FOR_NEXT_ORDER_UPDATE = (Integer) orderUpdatesPerMinuteDefaultSpinner.getValue();

		if (recommendedSettingRadio.isSelected()) {
			SAVE_LOG = true;
			MAKE_SUBFOLDER = makeSubFolderForEachFileTypeRecommendedCheckBox.isSelected();
			AUTO_DOWNLOAD = downloadFilesAutomaticallyRecommendedCheckBox.isSelected();
			NUMBER_DOWNLOAD_SIMULTANEOUS = (Integer) numberOfSimultaneousDownloadsRecommendedSpinner.getValue();
			MINUTES_FOR_NEXT_ORDER_UPDATE = (Integer) orderUpdatesPerMinuteRecommendedSpinner.getValue();
		}

		result.add(new Setting(SOFTWARE_NAME, SOFTWARE_PATH, SOFTWARE_PROGRAM, DOWNLOAD_FOLDER,
				SAVE_FOLDER, LOG_FOLDER, SAVE_LOG, MAKE_SUBFOLDER, AUTO_DOWNLOAD, NUMBER_DOWNLOAD_SIMULTANEOUS,
				MINUTES_FOR_NEXT_ORDER_UPDATE, KEEP_WHICH_FILES, true));

		return result;
	}

}
