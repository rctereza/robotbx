package com.rctereza.robotbx.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.crypto.NoSuchPaddingException;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
import com.rctereza.robotbx.enums.SpedSearchField;
import com.rctereza.robotbx.enums.Status;
import com.rctereza.robotbx.exceptions.InvalidCertificate;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.tools.CryptoUtils;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.Scheme;
import com.rctereza.robotbx.tools.ScreenResolution;
import com.rctereza.robotbx.tools.SpedUtils;
import com.rctereza.robotbx.tools.ValidateCpfCnpj;
import com.rctereza.robotbx.tools.ValidateDate;
import com.rctereza.robotbx.tools.ValidatePfx;
import com.rctereza.robotbx.wrappers.AppData;
import com.rctereza.robotbx.wrappers.Ref;

import net.miginfocom.swing.MigLayout;

public class MainForm2 extends JFrame {

	private static final long serialVersionUID = 8829044892271317875L;

	private static final String softwareNameAndVersion = Constants.SOFTWARE_NAME + " " + Constants.SOFTWARE_VERSION;

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
	private JButton passwordCheckButton;

	private JLabel customerLabel;
	private JTextField customerTextField;

	private JLabel customerDocumentLabel;
	private JTextField customerDocumentTextField;

	private JLabel downloadFolderLabel;
	private JTextField downloadFolderTextField;
	private JButton downloadFolderOpenButton;

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

	private JButton startButton;
	private JButton closeButton;

	// Grid fields
	private JButton addButton;
	private JTable itemsTable;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;
	private RemoverButtonEditor removerEditor;
	private static final String[] FIXED_COLUMNS = { "Status", "Sistema", "Tipo de Arquivo", "Tipo de Pesquisa" };
	// Stores the map of dynamic field names -> values for each row
	private final List<Map<String, String>> rowDynamicData = new ArrayList<>();

	private AppData appData = null;

	private List<ReceitaBx> receitaBxList = null;

	private ReceitaBx receitaBx = new ReceitaBx();

	private Controller controller = null;

	private Listenable listener = null;

	public MainForm2() throws Exception {
		super(softwareNameAndVersion);

//		appData = CryptoUtils.loadRef(Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE, AppData.class,	AppData::new);
		appData = CryptoUtils.loadEncryptedGCM(Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE, AppData.class,
				AppData::new);

		receitaBxList = appData.getLastListAdded();

		if (receitaBxList.size() > 0) {
			receitaBx = receitaBxList.get(0);
		}

		controller = new Controller();

		// setIconImage(Resources.getImage(Constants.SOFTWARE_ICON, null));

		setJMenuBar(createMenuBar());

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

		themeLabel = new JLabel("Trocar Tema");
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
		screenResolutionLabel = new JLabel("Resolução do Monitor");
		screenResolutionTextField = new JTextField();
		screenResolutionTextField.setText(ScreenResolution.getResolution());
		screenResolutionTextField.setEditable(false);

		// LINE 2
		certificateLabel = new JLabel("Selecione um Certificado");
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
		certificateLoadButton.setPreferredSize(new Dimension(80, 20));
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
		selectCertificateComboBoxItem(receitaBx.CERTIFICADO().toString());
		// System.out.println(receitaBx.CERTIFICADO());
		// certificateComboBox.setSelectedItem(receitaBx.CERTIFICADO());

		// LINE 3
		passwordLabel = new JLabel("Senha do Certificado");
		passwordTextField = new JTextField();
		passwordTextField.setText(receitaBx.CERTIFICADO().PASS());
		passwordCheckButton = new JButton("Validar");
		passwordCheckButton.setPreferredSize(new Dimension(80, 20));
		passwordCheckButton.addActionListener(e -> {
			Ref<Certificate> CERTIFICADO = new Ref<>((Certificate) certificateComboBox.getSelectedItem());
			try {
				ValidatePfx.load(CERTIFICADO, passwordTextField.getText());
				// ValidatePfx.print();
				customerTextField.setText(ValidatePfx.getCustomer());
				customerDocumentTextField.setText(ValidatePfx.getCustomerDocument());
				JOptionPane.showMessageDialog(null, "O certificado foi validado com sucesso.", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (InvalidCertificate e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
			}
		});

		// LINE 4
//		JSeparator horizontalLine = new JSeparator(JSeparator.HORIZONTAL);

		// LINE 5
		customerLabel = new JLabel("Nome do Cliente");
		customerTextField = new JTextField();
		customerTextField.setText(receitaBx.NOME_CLIENTE());
		customerTextField.setEditable(false);

		// LINE 6
		customerDocumentLabel = new JLabel("CNPJ do Cliente");
		customerDocumentTextField = new JTextField();
		customerDocumentTextField.setText(receitaBx.CNPJ_CLIENTE());
		customerDocumentTextField.setEditable(false);

		// LINE 7
		downloadFolderLabel = new JLabel("Caminho dos Arquivos baixados");
		downloadFolderTextField = new JTextField();
		downloadFolderTextField.setText(
				Objects.requireNonNullElse(receitaBx.CAMINHO_ARQUIVOS_BAIXADOS(), Constants.PROGRAM_DOWNLOADED_FOLDER));
		downloadFolderOpenButton = new JButton("Abrir");
		downloadFolderOpenButton.setPreferredSize(new Dimension(80, 20));
		downloadFolderOpenButton.addActionListener(e -> {
			String folderPath = downloadFolderTextField.getText();
			Path path = Paths.get(folderPath);
			if (Files.exists(path) && Files.isDirectory(path)) {
				try {
					new ProcessBuilder("explorer.exe", folderPath).start();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"O caminho informado não existe ou não é um diretorio: " + folderPath, "Atenção",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// LINE 8
//		JSeparator horizontalLine = new JSeparator(JSeparator.HORIZONTAL);

		// LINE 9
		profileLabel = new JLabel("Selecione um Perfil");
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

		// LINE 10
		systemLabel = new JLabel("Selecione um Sistema");
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

		// LINE 11
		systemFileTypeLabel = new JLabel("Selecione um Tipo de Arquivo");
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

		// LINE 12
		systemSearchTypeLabel = new JLabel("Selecione um Tipo de Pesquisa");
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
						panelMain.add(systemSearchFieldsPanel, "cell 0 14, span, grow, wrap");
						panelMain.revalidate();
						panelMain.repaint();
					} else {
						try {
							systemSearchFieldsPanel = SpedUtils.getSearchFields(Sped.getSped(receitaBx.SISTEMA()),
									receitaBx.TIPO_ARQUIVO(), receitaBx.TIPO_PESQUISA(), receitaBx);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		// LINE 13
//		JSeparator horizontalLine = new JSeparator(JSeparator.HORIZONTAL);

		// LINE 14
		systemSearchFieldsPanel = SpedUtils.getSearchFields(Sped.CONTRIBUICOES, "", "", receitaBx);

		systemComboBox.setSelectedItem(receitaBx.SISTEMA());
		systemFileTypeComboBox.setSelectedItem(receitaBx.TIPO_ARQUIVO());
		systemSearchTypeComboBox.setSelectedItem(receitaBx.TIPO_PESQUISA());

		// LINE 15 - Adicionar button
		addButton = new JButton("Adicionar");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = validateFormFields();
				if (result.equals("")) {
					if (isTableAlreadyPopulated()) {
						String[] options = { "Sim", "Não" };

						int choice = JOptionPane.showOptionDialog(null, // Parent component
								"Este item já foi adicionado. Deseja Atualizá-lo?", // Message
								"Confirmação", // Title
								JOptionPane.YES_NO_OPTION, // Option type
								JOptionPane.QUESTION_MESSAGE, // Message type
								null, // Icon (null for default)
								options, // Custom button labels
								options[0] // Default button focused
						);

						if (choice == 0) { // Sim
							updateGrid();
						}

					} else {
						addRowToGrid();
					}
				} else {
					JOptionPane.showMessageDialog(null, result, "Atenção", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// LINE 16 - Grid (table) to hold the queued searches
		TableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
			private final Icon pendingIcon = loadIcon("/icons/pending.png");
			private final Icon successIcon = loadIcon("/icons/success.png");
			private final Icon errorIcon = loadIcon("/icons/error.png");
			private final Icon warningIcon = loadIcon("/icons/warning.png");

			private Icon loadIcon(String path) {
				java.net.URL url = getClass().getResource(path);
				return (url != null) ? new ImageIcon(url) : null;
			}

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				if (value instanceof Status status) {
					label.setText(status.getValue());
					label.setIcon(null);

					switch (status) {

					case PENDING:
						label.setIcon(pendingIcon);
						label.setToolTipText(Status.PENDING.getValue());
						break;

					case SUCCESS:
						label.setIcon(successIcon);
						label.setToolTipText(Status.SUCCESS.getValue());
						break;

					case ERROR:
						label.setIcon(errorIcon);
						label.setToolTipText(Status.ERROR.getValue());
						break;

					case WARNING:
						label.setIcon(warningIcon);
						label.setToolTipText(Status.WARNING.getValue());
						break;

					}

				}

				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;
			}

		};
		
		removerEditor = new RemoverButtonEditor();
		tableModel = new DefaultTableModel(FIXED_COLUMNS, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// column here is the MODEL index — check if this model column is the Remover
				// one
				return getColumnName(column).equals("");
			}
		};
		itemsTable = new JTable(tableModel) {
			@Override
			public javax.swing.table.TableCellRenderer getCellRenderer(int row, int viewCol) {
				int modelCol = convertColumnIndexToModel(viewCol);

				// Button Column
				if (tableModel.getColumnName(modelCol).equals("")) {
					return (table, value, isSelected, hasFocus, r, c) -> {
						JButton btn = new JButton("Remover");
						btn.setOpaque(true);
						return btn;
					};
				}

				// ✅ Status column
				if (modelCol == 0) {
					return statusRenderer;
				}

				// Default renderer but centered
				TableCellRenderer defaultRenderer = super.getCellRenderer(row, viewCol);

				return (table, value, isSelected, hasFocus, r, c) -> {
					Component comp = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus,
							r, c);

					if (comp instanceof JLabel) {
						((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);

					}

					return comp;
				};

//				return super.getCellRenderer(row, viewCol);
			}

			@Override
			public javax.swing.table.TableCellEditor getCellEditor(int row, int viewCol) {
				int modelCol = convertColumnIndexToModel(viewCol);
				if (tableModel.getColumnName(modelCol).equals("")) {
					return removerEditor;
				}
				return super.getCellEditor(row, viewCol);
			}
		};
		itemsTable.setRowHeight(24); // important for icons
		itemsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		itemsTable.getTableHeader().setReorderingAllowed(false);
		// Activate the button editor on a single click instead of double-click
		itemsTable.putClientProperty("JTable.autoStartsEdit", false);
		itemsTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int viewCol = itemsTable.columnAtPoint(e.getPoint());
				int row = itemsTable.rowAtPoint(e.getPoint());
				if (row >= 0 && viewCol >= 0) {
					int modelCol = itemsTable.convertColumnIndexToModel(viewCol);
					if (tableModel.getColumnName(modelCol).equals("")) {
						itemsTable.editCellAt(row, viewCol);
					}
				}
			}
		});
		itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemsTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = itemsTable.getSelectedRow();
				if (selectedRow != -1) {
					// System.out.println("Selected row: " + selectedRow);
					int colCount = itemsTable.getColumnCount();
					for (int col = 0; col < colCount; col++) {
						String columnName = itemsTable.getColumnName(col);
						Object value = itemsTable.getValueAt(selectedRow, col);
						// System.out.println(columnName + ": " + value);

						if (columnName.equals("Sistema")) {
							systemComboBox.setSelectedItem(value);
						}
						if (columnName.equals("Tipo de Arquivo")) {
							systemFileTypeComboBox.setSelectedItem(value);
						}
						if (columnName.equals("Tipo de Pesquisa")) {
							systemSearchTypeComboBox.setSelectedItem(value);
						}

						for (Component c : systemSearchFieldsPanel.getComponents()) {
							if (c instanceof JTextField) {
								JTextField textField = (JTextField) c;
//		        				System.out.println("JTextField : " + textField.getText());
								if (textField.getName().equals(columnName)) {
									textField.setText((String) value);
									break;
								}
							}
							if (c instanceof JComboBox) {
								JComboBox<?> comboBox = (JComboBox<?>) c;
//		        				System.out.println("JComboBox : " + comboBox.getSelectedItem().toString());
								if (comboBox.getName().equals(columnName)) {
									comboBox.setSelectedItem((String) value);
									break;
								}
							}
							if (c instanceof JCheckBox) {
								JCheckBox checkBox = (JCheckBox) c;
//		        				System.out.println("JCheckBox : " + checkBox.getText());
								if (checkBox.getName().equals(columnName)) {
									if (String.valueOf(value).equals("true"))
										checkBox.setSelected(true);
									else
										checkBox.setSelected(false);

									break;
								}
							}
						}
					}
				}
			}
		});

		tableScrollPane = new JScrollPane(itemsTable);
		tableScrollPane.setPreferredSize(new java.awt.Dimension(0, 155));

		// LINE 17
		startButton = new JButton("Iniciar");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableModel.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null,
							"Adicione pelo menos um item à lista antes de iniciar o processamento.", "Atenção",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					Ref<List<ReceitaBx>> list = new Ref<>(getListOfFiles());

					startButton.setEnabled(false);

					controller.startRobot(list);

					updateGridStatusColumn(list.get());
					
					saveListOfFiles(list.get());
					
					StringBuilder message = new StringBuilder("O processo foi concluido. Veja o resultado abaixo.\n\n");

					if (list.get().size() > 0) {
						for (ReceitaBx entry : list.get()) {
							message.append(entry.SISTEMA()).append(" - ").append(entry.TIPO_ARQUIVO()).append(" - ")
									.append(entry.MENSAGEM_CONCLUSAO_PROCESSAMENTO());

							//System.out.println("DATA_HORA_CONCLUSAO_PROCESSAMENTO->" + entry.DATA_HORA_CONCLUSAO_PROCESSAMENTO() );
							
							if (entry.DATA_HORA_CONCLUSAO_PROCESSAMENTO() != null
									&& entry.DATA_HORA_CONCLUSAO_PROCESSAMENTO().length() > 0) {

								if (entry.TOTAL_PERIODOS_FALTANDO() == 0)
									message.append(" [Nenhum período esta faltando]").append("\n");
								else {
									message.append(" [" + entry.TOTAL_PERIODOS_FALTANDO() + "] período(s) faltando.");
									message.append(" [" + entry.PERIODOS_FALTANDO() + "]").append("\n");
								}
							}
						}
					}

					JOptionPane.showMessageDialog(null, message.toString(), "Information",
							JOptionPane.INFORMATION_MESSAGE);


				} catch (InvalidCertificate | InvalidKeyException | InvalidAlgorithmParameterException
						| NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
						| IOException | InterruptedException | ExecutionException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

				startButton.setEnabled(true);
			}
		});

		closeButton = new JButton("Sair");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.value(Menu.CLOSE.getValue());
			}
		});

		populateGrid();

//		panelMain = new JPanel(new MigLayout("wrap, insets 10, debug", "[]10[]10[]", "[]10[]10[]"));
		panelMain = new JPanel(new MigLayout("", "[]10[]10[]", "[] [] []"));

		panelMain.add(themeLabel, "split, span3, right");
		panelMain.add(themeButton, "wrap");
		panelMain.add(screenResolutionLabel, "left, sg 1");
		panelMain.add(screenResolutionTextField, "pushx, growx, wrap");
		panelMain.add(certificateLabel, "left, sg 1");
//		panelMain.add(certificateComboBox, "growx, w ::600");
		panelMain.add(certificateComboBox, "pushx, growx");
		panelMain.add(certificateLoadButton, "left, wrap");
		panelMain.add(passwordLabel, "left, sg 1");
		panelMain.add(passwordTextField, "pushx, growx");
		panelMain.add(passwordCheckButton, "left, wrap");
		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap");
		panelMain.add(customerLabel, "left, sg 1");
		panelMain.add(customerTextField, "pushx, growx, wrap");
		panelMain.add(customerDocumentLabel, "left, sg 1");
		panelMain.add(customerDocumentTextField, "pushx, growx, wrap");
		panelMain.add(downloadFolderLabel, "left, sg 1");
		panelMain.add(downloadFolderTextField, "pushx, growx");
		panelMain.add(downloadFolderOpenButton, "left, wrap");
		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap");
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
		// panelMain.add(horizontalLine, "span, grow, wrap");
		panelMain.add(systemSearchFieldsPanel, "cell 0 14, span, grow, wrap");
		panelMain.add(addButton, "cell 0 15, wrap");
		panelMain.add(tableScrollPane, "cell 0 16, span, grow, wrap");
		panelMain.add(startButton, "cell 0 17");
		panelMain.add(closeButton, "cell 2 17, left, wrap");

//		TitledBorder title;
//		title = BorderFactory.createTitledBorder("Pesquisa de Arquivos");
//		panelMain.setBorder(title);

		this.add(panelMain);

		setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	private String validateFormFields() {
		StringBuilder result = new StringBuilder("");

		Ref<Certificate> CERTIFICADO = null;
		String SENHA = "";
		String DATA_INICIO = "";
		String DATA_FIM = "";

		if (certificateComboBox.getSelectedIndex() == -1)
			result.append("Favor selecionar um certificado.\n");
		else
			CERTIFICADO = new Ref<>((Certificate) certificateComboBox.getSelectedItem());

		if (passwordTextField.getText().isBlank())
			result.append("Favor informar a senha do certificado.\n");
		else
			SENHA = passwordTextField.getText();

		result.append(ValidatePfx.check(CERTIFICADO, SENHA));

		if (customerTextField.getText().isBlank())
			result.append("Favor informar o nome do cliente clicando no botão 'Validar'.\n");
		else {
			if (!customerTextField.getText().equals(ValidatePfx.getCustomer())) {
				result.append(
						"O nome do cliente esta diferente do encontrado no certificado! Clique no botão 'Validar' para atualizá-lo.\n");
			}
		}

		if (customerDocumentTextField.getText().isBlank())
			result.append("Favor informar o cnpj do cliente clicando no botão 'Validar'.\n");
		else {
			if (!customerDocumentTextField.getText().equals(ValidatePfx.getCustomerDocument())) {
				result.append(
						"O cnpj do cliente esta diferente do encontrado no certificado! Clique no botão 'Validar' para atualizá-lo.\n");
			}
		}

		if (downloadFolderTextField.getText().isBlank())
			result.append("Favor informar o caminho que os arquivos serão baixados.\n");
		else {
			String folderPath = downloadFolderTextField.getText();
			Path path = Paths.get(folderPath);
			if (!Files.exists(path) || !Files.isDirectory(path)) {
				result.append("O caminho informado não existe ou não é um diretorio: " + folderPath + "\n");
			}
		}

		if (profileProcurador.isSelected()) {

			if (profileTypeComboBox.getSelectedItem().toString().equals("CPF")) {
				if (!ValidateCpfCnpj.isCpfValid(profileTypeValueTextField.getText())) {
					result.append("Favor informar um CPF válido.\n");
				}
			} else {
				if (!ValidateCpfCnpj.isCnpjValid(profileTypeValueTextField.getText())) {
					result.append("Favor informar um CNPJ válido.\n");
				}
			}
		}

		if (systemComboBox.getSelectedIndex() == -1)
			result.append("Favor selecionar um sistema.\n");

		if (systemFileTypeComboBox.getSelectedIndex() == -1)
			result.append("Favor selecionar um tipo de arquivo.\n");

		if (systemSearchTypeComboBox.getSelectedIndex() == -1)
			result.append("Favor selecionar um tipo de pesquisa.\n");

		for (Component c : systemSearchFieldsPanel.getComponents()) {
			if (c instanceof JTextField) {
				JTextField textField = (JTextField) c;
//				System.out.println("JTextField : " + textField.getText());
				if (textField.getName().equals(SpedSearchField.DATA_INICIO.getValue())) {
					DATA_INICIO = textField.getText();
					if (!ValidateDate.isValidDate(DATA_INICIO)) {
						// System.out.println("DATA_INICIO [" + DATA_INICIO + "]");
						result.append("Favor informar uma data inicial válida.\n");
					}
				} else if (textField.getName().equals(SpedSearchField.DATA_FIM.getValue())) {
					DATA_FIM = textField.getText();
					if (!ValidateDate.isValidDate(DATA_FIM)) {
						// System.out.println("DATA_FIM [" + DATA_FIM + "]");
						result.append("Favor informar uma data final válida.\n");
					} else {
						if (!ValidateDate.isValidRange(DATA_INICIO, DATA_FIM)) {
							result.append("Favor informar uma data inicial que seja anterior a data final.\n");
						}
					}
				} else if (textField.getName().equals(SpedSearchField.CNPJ_INCORPORADORA.getValue())) {
					if (!ValidateCpfCnpj.isCnpjValid(textField.getText())) {
						result.append("Favor informar um cnpj válido para a Incorporadora.\n");
					}
				} else if (textField.getName().equals(SpedSearchField.CNPJ_ESTABELECIMENTO.getValue())) {
					if (!ValidateCpfCnpj.isCnpjValid(textField.getText())) {
						result.append("Favor informar um cnpj válido para o Estabelecimento.\n");
					}
//				} else if (textField.getName().equals(SpedSearchField.INSCRICAO_ESTADUAL.getValue())) {
//					INSCRICAO_ESTADUAL = textField.getText();
				}
			} else if (c instanceof JComboBox) {
				JComboBox<?> comboBox = (JComboBox<?>) c;
//				System.out.println("JComboBox : " + comboBox.getSelectedItem().toString());
				if (comboBox.getName().equals(SpedSearchField.TIPO_EVENTO.getValue())) {
					if (comboBox.getSelectedIndex() == -1) {
						result.append("Favor selecionar um tipo de evento.\n");
					}
				} else if (comboBox.getName().equals(SpedSearchField.BAIXAR_ARQUIVO_ASSINADO.getValue())) {
					if (comboBox.getSelectedIndex() == -1) {
						result.append(
								"Favor selecionar se o arquivo deve ser baixado com assinatura digital ou não.\n");
					}
				}
//			} else if (c instanceof JCheckBox) {
//				JCheckBox checkBox = (JCheckBox) c;
//				System.out.println("JCheckBox : " + checkBox.getText());
//				if (checkBox.getName().equals(SpedSearchField.BUSCAR_TODOS_ESTABLECIMENTOS.getValue())) {
//					BUSCAR_TODOS_ESTABLECIMENTOS = checkBox.isSelected();
//				} else if (checkBox.getName().equals(SpedSearchField.ULTIMO_ARQUIVO_TRANSMITIDO.getValue())) {
//					ULTIMO_ARQUIVO_TRANSMITIDO = checkBox.isSelected();
//				}
			}
		}

		return result.toString();
	}

	private boolean isTableAlreadyPopulated() {
		boolean result = false;

		String sistema = systemComboBox.getSelectedItem().toString();
		String tipoArquivo = systemFileTypeComboBox.getSelectedItem().toString();
		String tipoPesquisa = systemSearchTypeComboBox.getSelectedItem().toString();

		for (int row = 0; row < tableModel.getRowCount(); row++) {
			if (tableModel.getValueAt(row, getModelColumnIndex("Sistema")).toString().equals(sistema)
					&& tableModel.getValueAt(row, getModelColumnIndex("Tipo de Arquivo")).toString().equals(tipoArquivo)
					&& tableModel.getValueAt(row, getModelColumnIndex("Tipo de Pesquisa")).toString()
							.equals(tipoPesquisa)) {
				result = true;
				break;
			}
		}

		return result;
	}

	private void updateGrid() {

		String sistema = systemComboBox.getSelectedItem().toString();
		String tipoArquivo = systemFileTypeComboBox.getSelectedItem().toString();
		String tipoPesquisa = systemSearchTypeComboBox.getSelectedItem().toString();

		for (int row = 0; row < tableModel.getRowCount(); row++) {
			if (tableModel.getValueAt(row, getModelColumnIndex("Sistema")).toString().equals(sistema)
					&& tableModel.getValueAt(row, getModelColumnIndex("Tipo de Arquivo")).toString().equals(tipoArquivo)
					&& tableModel.getValueAt(row, getModelColumnIndex("Tipo de Pesquisa")).toString()
							.equals(tipoPesquisa)) {

				for (Component c : systemSearchFieldsPanel.getComponents()) {
					if (c instanceof JTextField) {
						JTextField textField = (JTextField) c;
//        				System.out.println("JTextField: " + textField.getName() + " ["+ textField.getText() + "]");
						tableModel.setValueAt(textField.getText(), row, getModelColumnIndex(textField.getName()));
					}
					if (c instanceof JComboBox) {
						JComboBox<?> comboBox = (JComboBox<?>) c;
//        				System.out.println("JComboBox : " + comboBox.getSelectedItem().toString());
						tableModel.setValueAt(comboBox.getSelectedItem(), row, getModelColumnIndex(comboBox.getName()));
					}
					if (c instanceof JCheckBox) {
						JCheckBox checkBox = (JCheckBox) c;
//        				System.out.println("JCheckBox : " + checkBox.getText());
						tableModel.setValueAt(checkBox.isSelected(), row, getModelColumnIndex(checkBox.getName()));
					}
				}

				break;
			}
		}
	}

	private void updateGridStatusColumn(List<ReceitaBx> list) {

		for (ReceitaBx receitaBx : list) {

			Status status = receitaBx.STATUS();
			String sistema = receitaBx.SISTEMA();
			String tipoArquivo = receitaBx.TIPO_ARQUIVO();
			String tipoPesquisa = receitaBx.TIPO_PESQUISA();

			for (int row = 0; row < tableModel.getRowCount(); row++) {

				if (tableModel.getValueAt(row, getModelColumnIndex("Sistema")).toString().equals(sistema)
						&& tableModel.getValueAt(row, getModelColumnIndex("Tipo de Arquivo")).toString()
								.equals(tipoArquivo)
						&& tableModel.getValueAt(row, getModelColumnIndex("Tipo de Pesquisa")).toString()
								.equals(tipoPesquisa)) {

					tableModel.setValueAt(status.getValue(), row, getModelColumnIndex("Status"));
					break;
				}
			}
		}
	}

	private void populateGrid() {

		for (ReceitaBx receitaBx : receitaBxList) {

			Status status = receitaBx.STATUS();
			String sistema = receitaBx.SISTEMA();
			String tipoArquivo = receitaBx.TIPO_ARQUIVO();
			String tipoPesquisa = receitaBx.TIPO_PESQUISA();
			Map<String, String> dynamicFields = new LinkedHashMap<>();

			if (receitaBx.DATA_INICIO() != null && !receitaBx.DATA_INICIO().equals(""))
				dynamicFields.put(SpedSearchField.DATA_INICIO.getValue(), receitaBx.DATA_INICIO());

			if (receitaBx.DATA_FIM() != null && !receitaBx.DATA_FIM().equals(""))
				dynamicFields.put(SpedSearchField.DATA_FIM.getValue(), receitaBx.DATA_FIM());

			if (receitaBx.CNPJ_INCORPORADORA() != null && !receitaBx.CNPJ_INCORPORADORA().equals(""))
				dynamicFields.put(SpedSearchField.CNPJ_INCORPORADORA.getValue(), receitaBx.CNPJ_INCORPORADORA());

			if (sistema.equals(Sped.EFD.getValue())) {

				dynamicFields.put(SpedSearchField.TIPO_EVENTO.getValue(), receitaBx.TIPO_EVENTO());

				dynamicFields.put(SpedSearchField.BAIXAR_ARQUIVO_ASSINADO.getValue(),
						receitaBx.BAIXAR_ARQUIVO_ASSINADO());

			}

			if (sistema.equals(Sped.FISCAL.getValue())) {

				dynamicFields.put(SpedSearchField.CNPJ_ESTABELECIMENTO.getValue(), receitaBx.CNPJ_ESTABELECIMENTO());

				dynamicFields.put(SpedSearchField.INSCRICAO_ESTADUAL.getValue(),
						receitaBx.INSCRICAO_ESTADUAL().toString());

				dynamicFields.put(SpedSearchField.BUSCAR_TODOS_ESTABLECIMENTOS.getValue(),
						receitaBx.BUSCAR_TODOS_ESTABLECIMENTOS().toString());

				dynamicFields.put(SpedSearchField.ULTIMO_ARQUIVO_TRANSMITIDO.getValue(),
						receitaBx.ULTIMO_ARQUIVO_TRANSMITIDO().toString());

			}

			addRowToGrid(status, sistema, tipoArquivo, tipoPesquisa, dynamicFields);
		}

	}

	private void addRowToGrid() {
		Status status = Status.PENDING;
		String sistema = systemComboBox.getSelectedItem().toString();
		String tipoArquivo = systemFileTypeComboBox.getSelectedItem().toString();
		String tipoPesquisa = systemSearchTypeComboBox.getSelectedItem().toString();

		Map<String, String> dynamicFields = collectDynamicFields();
		addRowToGrid(status, sistema, tipoArquivo, tipoPesquisa, dynamicFields);
	}

	private void addRowToGrid(Status status, String sistema, String tipoArquivo, String tipoPesquisa,
			Map<String, String> dynamicFields) {

		// Find current Remover column position (-1 = not yet added)
		int removerModelIndex = -1;
		for (int c = 0; c < tableModel.getColumnCount(); c++) {
			if (tableModel.getColumnName(c).equals("")) {
				removerModelIndex = c;
				break;
			}
		}

		// Add any new dynamic columns that don't exist yet
		boolean newColumnsAdded = false;
		for (String fieldName : dynamicFields.keySet()) {
			boolean found = false;
			for (int c = 0; c < tableModel.getColumnCount(); c++) {
				if (tableModel.getColumnName(c).equals(fieldName)) {
					found = true;
					break;
				}
			}
			if (!found) {
				tableModel.addColumn(fieldName);
				newColumnsAdded = true;
			}
		}

		// Add Remover column for the first time
		if (removerModelIndex == -1) {
			tableModel.addColumn("");
			wireRemoverColumn();
		} else if (newColumnsAdded) {
			// New dynamic columns were appended AFTER the Remover column in the model —
			// move the Remover view-column to the rightmost position so it stays last.
			int removerViewIndex = itemsTable.convertColumnIndexToView(removerModelIndex);
			int lastViewIndex = itemsTable.getColumnCount() - 1;
			if (removerViewIndex != lastViewIndex) {
				itemsTable.getColumnModel().moveColumn(removerViewIndex, lastViewIndex);
			}
		}

		// Build the row data aligned to model column order
		int colCount = tableModel.getColumnCount();
		Object[] rowData = new Object[colCount];
		for (int c = 0; c < colCount; c++) {
			String colName = tableModel.getColumnName(c);
			if (colName.equals("Status"))
				rowData[c] = status;
			else if (colName.equals("Sistema"))
				rowData[c] = sistema;
			else if (colName.equals("Tipo de Arquivo"))
				rowData[c] = tipoArquivo;
			else if (colName.equals("Tipo de Pesquisa"))
				rowData[c] = tipoPesquisa;
			else if (colName.equals(""))
				rowData[c] = "Remover";
			else
				rowData[c] = dynamicFields.getOrDefault(colName, "");
		}

		tableModel.addRow(rowData);
		rowDynamicData.add(dynamicFields);
	}

	private Map<String, String> collectDynamicFields() {
		Map<String, String> fields = new LinkedHashMap<>();
		for (Component c : systemSearchFieldsPanel.getComponents()) {
			if (c instanceof JTextField) {
				JTextField tf = (JTextField) c;
				if (tf.getName() != null && !tf.getName().isBlank()) {
					fields.put(tf.getName(), tf.getText());
				}
			} else if (c instanceof JComboBox) {
				JComboBox<?> cb = (JComboBox<?>) c;
				if (cb.getName() != null && !cb.getName().isBlank() && cb.getSelectedItem() != null) {
					fields.put(cb.getName(), cb.getSelectedItem().toString());
				}
			} else if (c instanceof JCheckBox) {
				JCheckBox chk = (JCheckBox) c;
				if (chk.getName() != null && !chk.getName().isBlank()) {
					fields.put(chk.getName(), String.valueOf(chk.isSelected()));
				}
			}
		}
		return fields;
	}

	private class RemoverButtonEditor extends javax.swing.AbstractCellEditor
			implements javax.swing.table.TableCellEditor {

		private static final long serialVersionUID = 1L;
		private final JButton btn = new JButton("Remover");
		private int currentRow = -1;

		public RemoverButtonEditor() {
			btn.addActionListener(ev -> {
				fireEditingStopped();
				if (currentRow >= 0 && currentRow < tableModel.getRowCount()) {
					tableModel.removeRow(currentRow);
					if (currentRow < rowDynamicData.size()) {
						rowDynamicData.remove(currentRow);
					}
				}
			});
		}

		@Override
		public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			currentRow = row;
			return btn;
		}

		@Override
		public Object getCellEditorValue() {
			return "Remover";
		}
	}

	private void wireRemoverColumn() {
		javax.swing.table.TableColumn removerCol = itemsTable.getColumn("");
		removerCol.setPreferredWidth(85);
		removerCol.setMaxWidth(85);
	}

	private List<ReceitaBx> getListOfFiles()
			throws InvalidCertificate, InvalidKeyException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IOException {

		List<ReceitaBx> result = new ArrayList<>();

		String SCREEN = screenResolutionTextField.getText();

		Ref<Certificate> CERTIFICADO = new Ref<>((Certificate) certificateComboBox.getSelectedItem());
		String SENHA = passwordTextField.getText();

		ValidatePfx.load(CERTIFICADO, SENHA);

		String NOME_CLIENTE = customerTextField.getText();
		String CNPJ_CLIENTE = customerDocumentTextField.getText();
		String CAMINHO_ARQUIVOS_BAIXADOS = downloadFolderTextField.getText();

		String PERFIL = profileContribuinte.getText();
		String PERFIL_TYPE = "";
		String PERFIL_VALUE = "";

		if (profileProcurador.isSelected()) {
			PERFIL = profileProcurador.getText();
			PERFIL_TYPE = profileTypeComboBox.getSelectedItem().toString();
			PERFIL_VALUE = profileTypeValueTextField.getText();
		}

		for (int row = 0; row < tableModel.getRowCount(); row++) {

			String SISTEMA = (String) tableModel.getValueAt(row, getModelColumnIndex("Sistema"));
			String TIPO_ARQUIVO = (String) tableModel.getValueAt(row, getModelColumnIndex("Tipo de Arquivo"));
			String TIPO_PESQUISA = (String) tableModel.getValueAt(row, getModelColumnIndex("Tipo de Pesquisa"));
			String DATA_INICIO = "";
			String DATA_FIM = "";
			String CNPJ_INCORPORADORA = "";
			String TIPO_EVENTO = "";
			String BAIXAR_ARQUIVO_ASSINADO = "";
			String CNPJ_ESTABELECIMENTO = "";
			Boolean BUSCAR_TODOS_ESTABLECIMENTOS = false;
			String INSCRICAO_ESTADUAL = "";
			Boolean ULTIMO_ARQUIVO_TRANSMITIDO = false;
			String ULTIMO_PEDIDO_SOLICITADO = "";
			String DATA_HORA_CONCLUSAO_PROCESSAMENTO = "";
			String MENSAGEM_CONCLUSAO_PROCESSAMENTO = "";
			String PERIODOS_FALTANDO = "";
			Integer TOTAL_PERIODOS_FALTANDO = 0;
			Status STATUS = Status.PENDING;

			// Dynamic fields — read from the stored map for this row
			Map<String, String> dynamic = rowDynamicData.get(row);
			for (Map.Entry<String, String> entry : dynamic.entrySet()) {
				// System.out.println(" " + entry.getKey() + ": " + entry.getValue());
				if (entry.getKey().equals(SpedSearchField.DATA_INICIO.getValue())) {
					DATA_INICIO = entry.getValue();
				} else if (entry.getKey().equals(SpedSearchField.DATA_FIM.getValue())) {
					DATA_FIM = entry.getValue();
				} else if (entry.getKey().equals(SpedSearchField.CNPJ_INCORPORADORA.getValue())) {
					CNPJ_INCORPORADORA = entry.getValue();
				} else if (entry.getKey().equals(SpedSearchField.TIPO_EVENTO.getValue())) {
					TIPO_EVENTO = entry.getValue();
				} else if (entry.getKey().equals(SpedSearchField.BAIXAR_ARQUIVO_ASSINADO.getValue())) {
					BAIXAR_ARQUIVO_ASSINADO = entry.getValue();
				} else if (entry.getKey().equals(SpedSearchField.CNPJ_ESTABELECIMENTO.getValue())) {
					CNPJ_ESTABELECIMENTO = entry.getValue();
				} else if (entry.getKey().equals(SpedSearchField.BUSCAR_TODOS_ESTABLECIMENTOS.getValue())) {
					BUSCAR_TODOS_ESTABLECIMENTOS = Boolean.parseBoolean(entry.getValue());
				} else if (entry.getKey().equals(SpedSearchField.INSCRICAO_ESTADUAL.getValue())) {
					INSCRICAO_ESTADUAL = entry.getValue();
				} else if (entry.getKey().equals(SpedSearchField.ULTIMO_ARQUIVO_TRANSMITIDO.getValue())) {
					ULTIMO_ARQUIVO_TRANSMITIDO = Boolean.parseBoolean(entry.getValue());
				}
			}

			ReceitaBx receitaBx = new ReceitaBx(SCREEN, CERTIFICADO.get(), NOME_CLIENTE, CNPJ_CLIENTE,
					CAMINHO_ARQUIVOS_BAIXADOS, PERFIL, PERFIL_TYPE, PERFIL_VALUE, SISTEMA, TIPO_ARQUIVO, TIPO_PESQUISA,
					DATA_INICIO, DATA_FIM, CNPJ_INCORPORADORA, TIPO_EVENTO, BAIXAR_ARQUIVO_ASSINADO,
					CNPJ_ESTABELECIMENTO, BUSCAR_TODOS_ESTABLECIMENTOS, INSCRICAO_ESTADUAL, ULTIMO_ARQUIVO_TRANSMITIDO,
					ULTIMO_PEDIDO_SOLICITADO, DATA_HORA_CONCLUSAO_PROCESSAMENTO, MENSAGEM_CONCLUSAO_PROCESSAMENTO,
					PERIODOS_FALTANDO, TOTAL_PERIODOS_FALTANDO, STATUS);

			result.add(receitaBx);
		}

		saveListOfFiles(result);

		return result;
	}

	/** Helper: returns the model column index by name, or -1 if not found. */
	private int getModelColumnIndex(String columnName) {
		for (int c = 0; c < tableModel.getColumnCount(); c++) {
			if (tableModel.getColumnName(c).equals(columnName)) {
				return c;
			}
		}
		return -1;
	}

	private void saveListOfFiles(List<ReceitaBx> list) throws InvalidKeyException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IOException {

		if (appData.getLastListAdded().size() == 0) {
			appData.addList(list);
		} else {
			appData.updateList(appData.getLastIdAdded(), list);
		}
		CryptoUtils.saveEncryptedGCM(appData, Constants.SOFTWARE_SECRET, Constants.SOFTWARE_SECURE_FILE);
	}

	private JMenuBar createMenuBar() {

		JMenuItem historic = new JMenuItem(Menu.HISTORIC.getValue());
		historic.setMnemonic(KeyEvent.VK_H);
		historic.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		historic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HistoricForm form = new HistoricForm(MainForm2.this, Menu.HISTORIC);
				form.addObjectListener(new Listenable() {
					@Override
					public void value(Object... objs) {
						// performActions(form, objs);
					}
				});
				form.load();
			}
		});

		JMenuItem setting = new JMenuItem(Menu.SETTING.getValue());
		setting.setMnemonic(KeyEvent.VK_C);
		setting.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		setting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingForm form = new SettingForm(MainForm2.this, Menu.SETTING);
				form.addObjectListener(new Listenable() {
					@Override
					public void value(Object... objs) {
						// performActions(form, objs);
					}
				});
				form.load();
			}
		});

		JMenuItem exit = new JMenuItem("Sair");
		exit.setMnemonic(KeyEvent.VK_S);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.value(Menu.CLOSE.getValue());
			}
		});

		JMenu fileMenu = new JMenu("Menu");
		fileMenu.setMnemonic(KeyEvent.VK_M);
		fileMenu.add(historic);
		fileMenu.addSeparator();
		fileMenu.add(setting);
		fileMenu.addSeparator();
		fileMenu.add(exit);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);

		return menuBar;
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

	public void selectCertificateComboBoxItem(String valueToFind) {
		for (int i = 0; i < certificateComboBox.getItemCount(); i++) {
			Certificate cert = certificateComboBox.getItemAt(i);
			if (cert.toString().equals(valueToFind)) {
				certificateComboBox.setSelectedIndex(i);
				break; // stop once found
			}
		}
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

	public void addObjectListener(Listenable listener) {
		this.listener = listener;
	}

}