package com.rctereza.robotbx.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.Main;
import com.rctereza.robotbx.components.DarkLightSwitchIcon;
import com.rctereza.robotbx.controllers.Controller;
import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.enums.Sped;
import com.rctereza.robotbx.enums.SpedSearchField;
import com.rctereza.robotbx.enums.Status;
import com.rctereza.robotbx.exceptions.InvalidCertificate;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.models.Procurator;
import com.rctereza.robotbx.models.ReceitaBx;
import com.rctereza.robotbx.models.Setting;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.Scheme;
import com.rctereza.robotbx.tools.ScreenResolution;
import com.rctereza.robotbx.tools.SpedUtils;
import com.rctereza.robotbx.tools.ValidateCpfCnpj;
import com.rctereza.robotbx.tools.ValidateDate;
import com.rctereza.robotbx.tools.ValidatePfx;
import com.rctereza.robotbx.wrappers.Ref;

import net.miginfocom.swing.MigLayout;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 8829044892271317875L;

	private static final Logger logger = LoggerFactory.getLogger(MainForm.class);

	private static final String softwareNameAndVersion = Constants.SOFTWARE_NAME + " " + Constants.SOFTWARE_VERSION;

	private GraphicsDevice lastMonitor;

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
//	private JTextField customerTextField;
	private JComboBox<Procurator> customerComboBox;

	private JLabel customerDocumentLabel;
	private JTextField customerDocumentTextField;

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
	private JButton exitButton;

	// Grid fields
	private JButton addButton;
	private JTable itemsTable;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;
	private RemoverButtonEditor removerEditor;
	private static final String[] FIXED_COLUMNS = { "Status", "Sistema", "Tipo de Arquivo", "Tipo de Pesquisa" };
	// Stores the map of dynamic field names -> values for each row
	private final List<Map<String, String>> rowDynamicData = new ArrayList<>();

	private ReceitaBx receitaBx = new ReceitaBx();

	private List<ReceitaBx> receitaBxList;

	private Controller controller;

	private Listenable listener;

	private Timer monitorTimer;

	private JMenuItem setting;

	public MainForm() throws Exception {
		super(softwareNameAndVersion);

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

		lastMonitor = ScreenResolution.getCurrentMonitor(MainForm.this);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {

				if (monitorTimer != null) {
					monitorTimer.stop();
				}

				monitorTimer = new Timer(500, ev -> {

					monitorTimer.stop();

					GraphicsDevice currentMonitor = ScreenResolution.getCurrentMonitor(MainForm.this);
					if (!currentMonitor.equals(lastMonitor)) {
						lastMonitor = currentMonitor;
						screenResolutionLabel.setText(ScreenResolution.getDisplayLabel(MainForm.this));
						screenResolutionTextField.setText(ScreenResolution.getResolution(lastMonitor));
					}
				});

				monitorTimer.setRepeats(false);
				monitorTimer.start();
			}
		});

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
		screenResolutionLabel = new JLabel("");
		screenResolutionTextField = new JTextField();
		screenResolutionTextField.setEditable(false);

		// LINE 2
		certificateLabel = new JLabel("Selecione um certificado");
		certificateComboBox = new JComboBox<>();
		certificateComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					passwordTextField.setText("");
					customerComboBox.removeAllItems();
					customerDocumentTextField.setText("");
					profileContribuinte.doClick();
					// profileTypeComboBox.setSelectedIndex(-1); // CNPJ
					profileTypeValueTextField.setValue("");
				}
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
				certificateComboBox.removeAllItems();
				certificateComboBox
						.setModel(FileUtils.getModelOfCertificates(chooser.getSelectedFile().getAbsolutePath()));
			}
		});

		// LINE 3
		passwordLabel = new JLabel("Senha do certificado");
		passwordTextField = new JTextField();
		passwordCheckButton = new JButton("Validar");
		passwordCheckButton.setPreferredSize(new Dimension(80, 20));
		passwordCheckButton.addActionListener(e -> {
			if (certificateComboBox.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(this, "Para validar um certificado é necessário selecioná-lo primeiro",
						"Informação", JOptionPane.INFORMATION_MESSAGE);
			} else if (passwordTextField.getText().isBlank()) {
				JOptionPane.showMessageDialog(this, "Informe a senha do certificado antes de tentar validá-lo",
						"Informação", JOptionPane.INFORMATION_MESSAGE);
			} else {
				Ref<Certificate> CERTIFICADO = new Ref<>((Certificate) certificateComboBox.getSelectedItem());
				try {
					ValidatePfx.load(CERTIFICADO, passwordTextField.getText());
//					ValidatePfx.print();

					customerComboBox.removeAllItems();
					customerComboBox.setModel(getProcuratorModel(CERTIFICADO.get()));
					customerComboBox.setSelectedIndex(0);

					JOptionPane.showMessageDialog(this, "O certificado foi validado com sucesso.", "Informação",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (InvalidCertificate e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// LINE 4
//		JSeparator horizontalLine = new JSeparator(JSeparator.HORIZONTAL);

		// LINE 5
		customerLabel = new JLabel("Nome do cliente");

//		customerTextField = new JTextField();
//		customerTextField.setEditable(false);

		customerComboBox = new JComboBox<>();
		customerComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Procurator obj = (Procurator) customerComboBox.getSelectedItem();
					customerDocumentTextField.setText(obj.CLIENTE_DOC());
					if (obj.VALIDADE() != null) {
						profileProcurador.doClick();
						profileTypeComboBox.setSelectedIndex(1); // CNPJ
						profileTypeValueTextField.setText(obj.CLIENTE_DOC());
					}
				}
			}
		});

		// LINE 6
		customerDocumentLabel = new JLabel("CNPJ do cliente");
		customerDocumentTextField = new JTextField();
		customerDocumentTextField.setEditable(false);

		// LINE 7
//		JSeparator horizontalLine = new JSeparator(JSeparator.HORIZONTAL);

		// LINE 8
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

					if (profileTypeComboBox.getSelectedItem().toString().equals("CNPJ")) {
						profileTypeValueTextField.setFormatterFactory(new DefaultFormatterFactory(cnpjMask));
						profileTypeValueTextField.setColumns(11);
					} else {
						profileTypeValueTextField.setFormatterFactory(new DefaultFormatterFactory(cpfMask));
						profileTypeValueTextField.setColumns(9);
					}

					profileTypeValueTextField.revalidate();
					profileTypeValueTextField.repaint();

					// IMPORTANT:
					profileTypeValueTextField.getParent().revalidate();
					profileTypeValueTextField.getParent().repaint();
				}
			}
		});

		profileTypeValueTextField = new JFormattedTextField(cpfMask);
		profileTypeValueTextField.setVisible(false);
		profileTypeValueTextField.setColumns(9);

		// LINE 9
		systemLabel = new JLabel("Selecione um sistema");
		systemComboBox = new JComboBox<String>(SpedUtils.getSystemList());
		systemComboBox.setSelectedIndex(-1);
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

		// LINE 10
		systemFileTypeLabel = new JLabel("Selecione um tipo de arquivo");
		systemFileTypeComboBox = new JComboBox<String>(SpedUtils.getSystemFileType(Sped.CONTRIBUICOES));
		systemFileTypeComboBox.setSelectedIndex(-1);
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

		// LINE 11
		systemSearchTypeLabel = new JLabel("Selecione um tipo de pesquisa");
		systemSearchTypeComboBox = new JComboBox<String>(SpedUtils.getSystemSearchType(Sped.CONTRIBUICOES, ""));
		systemSearchTypeComboBox.setSelectedIndex(-1);
		systemSearchTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {

					Sped system = Sped.getSped(systemComboBox.getSelectedItem().toString());
					String systemFileType = systemFileTypeComboBox.getSelectedItem().toString();
					String systemSearchType = e.getItem().toString();

					if (systemSearchFieldsPanel != null && systemSearchFieldsPanel.isShowing()) {
						panelMain.remove(systemSearchFieldsPanel);
						systemSearchFieldsPanel = SpedUtils.getSearchFields(system, systemFileType, systemSearchType, getItem());
						panelMain.add(systemSearchFieldsPanel, "cell 0 13, span, grow, wrap");
						panelMain.revalidate();
						panelMain.repaint();
					}
				}
			}
		});

		// LINE 12
//		JSeparator horizontalLine = new JSeparator(JSeparator.HORIZONTAL);

		// LINE 13
		systemSearchFieldsPanel = SpedUtils.getSearchFields(Sped.CONTRIBUICOES, "", "", receitaBx);

		// LINE 14 - Adicionar button
		addButton = new JButton("Adicionar");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = validateFormFields();
				if (result.equals("")) {
					if (isTableAlreadyPopulated()) {
						String[] options = { "Sim", "Não" };

						int choice = JOptionPane.showOptionDialog(MainForm.this, // Parent component
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
					JOptionPane.showMessageDialog(MainForm.this, result, "Atenção", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// LINE 15 - Grid (table) to hold the queued documents
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
						label.setToolTipText(Status.PENDING.getDescription());
						break;

					case SUCCESS:
						label.setIcon(successIcon);
						label.setToolTipText(Status.SUCCESS.getDescription());
						break;

					case ERROR:
						label.setIcon(errorIcon);
						label.setToolTipText(Status.ERROR.getDescription());
						break;

					case WARNING:
						label.setIcon(warningIcon);
						label.setToolTipText(Status.WARNING.getDescription());
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

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem detailsItem = new JMenuItem("Detalhes");
		detailsItem.addActionListener(e -> {
			int row = itemsTable.getSelectedRow();
			ReceitaBx obj = getItem(row);
			if (obj == null) {
				JOptionPane.showMessageDialog(this,
						"Este item ainda não foi processado. Após seu processamento seus detalhes estarão disponíveis.",
						"Atenção", JOptionPane.WARNING_MESSAGE);
			} else {
				DetailsForm form = new DetailsForm(MainForm.this, Menu.DETAILS, obj);
				form.addObjectListener(new Listenable() {
					@Override
					public void value(Object... objs) {
						String action = (String) objs[0];
						if (action.equals(Menu.CLOSE.getValue())) {
							form.close();
						}
					}
				});
				form.load();
			}
		});

		JMenuItem retryItem = new JMenuItem("Reprocessar");
		retryItem.addActionListener(e -> {
			int row = itemsTable.getSelectedRow();
			if (row != -1) {
				row = itemsTable.convertRowIndexToModel(row);
			}
			tableModel.setValueAt(Status.PENDING, row, getModelColumnIndex("Status"));
		});

		JMenuItem deleteItem = new JMenuItem("Remover");
		deleteItem.addActionListener(e -> {
			int row = itemsTable.getSelectedRow();
			if (confirmDeletion()) {
				tableModel.removeRow(row);
				if (row < rowDynamicData.size()) {
					rowDynamicData.remove(row);
				}
			}
		});

		popupMenu.add(detailsItem);
		popupMenu.add(retryItem);
		popupMenu.add(deleteItem);

		itemsTable.setRowHeight(24); // important for icons
		itemsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		itemsTable.getTableHeader().setReorderingAllowed(false);
		// Activate the button editor on a single click instead of double-click
		itemsTable.putClientProperty("JTable.autoStartsEdit", false);
		itemsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int viewCol = itemsTable.columnAtPoint(e.getPoint());
				int row = itemsTable.rowAtPoint(e.getPoint());
				if (row >= 0 && viewCol >= 0) {
					int modelCol = itemsTable.convertColumnIndexToModel(viewCol);
					if (tableModel.getColumnName(modelCol).equals("")) {
						itemsTable.editCellAt(row, viewCol);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				handlePopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handlePopup(e);
			}

			private void handlePopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					int row = itemsTable.rowAtPoint(e.getPoint());

					retryItem.setEnabled(false);

					if (row >= 0) {

						itemsTable.setRowSelectionInterval(row, row);

						Status status = (Status) tableModel.getValueAt(row, getModelColumnIndex("Status"));

						if (status == Status.SUCCESS) {
							retryItem.setEnabled(true);
						}

					}

					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemsTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int row = itemsTable.getSelectedRow();
				if (row != -1) {
					int colCount = itemsTable.getColumnCount();
					for (int col = 0; col < colCount; col++) {
						String columnName = itemsTable.getColumnName(col);
						Object value = itemsTable.getValueAt(row, col);

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
							if (c instanceof JFormattedTextField) {
								JFormattedTextField textField = (JFormattedTextField) c;
								if (textField.getName().equals(columnName)) {
									textField.setValue((String) value);
									break;
								}
							}
							if (c instanceof JTextField) {
								JTextField textField = (JTextField) c;
								if (textField.getName().equals(columnName)) {
									textField.setText((String) value);
									break;
								}
							}
							if (c instanceof JComboBox) {
								JComboBox<?> comboBox = (JComboBox<?>) c;
								if (comboBox.getName().equals(columnName)) {
									comboBox.setSelectedItem((String) value);
									break;
								}
							}
							if (c instanceof JCheckBox) {
								JCheckBox checkBox = (JCheckBox) c;
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

				if (isConfigurationOkay()) {
					
					if (isTheProgramClosed()) {

						if (tableModel.getRowCount() == 0) {
							JOptionPane.showMessageDialog(MainForm.this,
									"Adicione pelo menos um item à lista antes de iniciar o processamento.", "Atenção",
									JOptionPane.WARNING_MESSAGE);
							return;
						} else if (!isThereItemToBeProcessed()) {
							JOptionPane.showMessageDialog(MainForm.this,
									"Não existe items para serem processados. Somente itens com status diferente de 'Sucesso' serão processados. Caso queira reprocessar um item pressione a tecla direita do mouse sobre ele e escolha a opção reprocessar.",
									"Atenção", JOptionPane.WARNING_MESSAGE);
							return;
						}

						startButton.setEnabled(false);

						Ref<List<ReceitaBx>> list = null;

						try {

							list = new Ref<>(getListOfFiles());

							saveListOfFiles(list.get());

							controller.startRobot(list);

							StringBuilder message = new StringBuilder(
									"O processo foi concluido. Veja o resultado abaixo.\n\n");

							for (ReceitaBx entry : list.get()) {
								message.append(entry.SISTEMA()).append("/").append(entry.TIPO_ARQUIVO()).append("/")
										.append(entry.TIPO_PESQUISA()).append("\n")
										.append(entry.MENSAGEM_CONCLUSAO_PROCESSAMENTO()).append("\n");

								if (entry.DATA_HORA_CONCLUSAO_PROCESSAMENTO() != null
										&& entry.DATA_HORA_CONCLUSAO_PROCESSAMENTO().length() > 0) {

									if (entry.TOTAL_PERIODOS_FALTANDO() == 0)
										message.append(" [Nenhum período esta faltando]").append("\n\n");
									else {
										message.append(
												" [" + entry.TOTAL_PERIODOS_FALTANDO() + "] período(s) faltando.\n");
										message.append(" [" + entry.PERIODOS_FALTANDO() + "]").append("\n\n");
									}
								}
							}

							logger.info(message.toString());
							JOptionPane.showMessageDialog(MainForm.this, message.toString(), "Informação",
									JOptionPane.INFORMATION_MESSAGE);

						} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
								| NoSuchPaddingException | InvalidKeySpecException | InvalidCertificate | IOException
								| InterruptedException | ExecutionException e1) {
							logger.error(e1.getMessage(), e1);
							JOptionPane.showMessageDialog(MainForm.this, e1.getMessage(), "Erro",
									JOptionPane.ERROR_MESSAGE);
						} finally {
							if (list != null) {
								updateGridStatusColumn(list.get());
								saveListOfFiles(list.get());
							}
						}

						startButton.setEnabled(true);
					}
				}
			}
		});

		exitButton = new JButton("Sair");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.value(Menu.EXIT.getValue());
			}
		});

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
		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap, gapy 5 5");
		panelMain.add(customerLabel, "left, sg 1");
		panelMain.add(customerComboBox, "pushx, growx, wrap");
		panelMain.add(customerDocumentLabel, "left, sg 1");
		panelMain.add(customerDocumentTextField, "pushx, growx, wrap");
		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap, gapy 5 5");
		panelMain.add(profileLabel, "left, sg 1");
		panelMain.add(profileContribuinte, "split");
		panelMain.add(profileProcurador);
		panelMain.add(profileTypeComboBox);
		panelMain.add(profileTypeValueTextField, "wrap");
		panelMain.add(systemLabel, "left, sg 1");
		panelMain.add(systemComboBox, "wrap");
		panelMain.add(systemFileTypeLabel, "left, sg 1");
		panelMain.add(systemFileTypeComboBox, "wrap");
		panelMain.add(systemSearchTypeLabel, "left, sg 1");
		panelMain.add(systemSearchTypeComboBox, "wrap");
		// panelMain.add(horizontalLine, "span, grow, wrap");
		panelMain.add(systemSearchFieldsPanel, "cell 0 13, span, grow, wrap");
		panelMain.add(addButton, "cell 0 14, wrap");
		panelMain.add(tableScrollPane, "cell 0 15, span, grow, wrap");
		panelMain.add(startButton, "cell 0 16");
		panelMain.add(exitButton, "cell 2 16, left, wrap");

//		TitledBorder title;
//		title = BorderFactory.createTitledBorder("Pesquisa de Arquivos");
//		panelMain.setBorder(title);

		this.add(panelMain);

		setSize(1200, 810);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public void init() {

		receitaBxList = Main.getAppData().getLastListAdded(ReceitaBx.class);

		if (receitaBxList.size() > 0) {
			receitaBx = receitaBxList.get(0);
		}

//		logger.debug("ReceitaBx: {}", receitaBx);
//		FileUtils.removeCertificatePathChosen();

		screenResolutionLabel.setText(ScreenResolution.getDisplayLabel(MainForm.this));
		screenResolutionTextField.setText(ScreenResolution.getResolution(lastMonitor));

		certificateComboBox.removeAllItems();
		certificateComboBox.setModel(FileUtils.getModelOfCertificates());
		certificateComboBox.setSelectedIndex(FileUtils.getCertificateIndex(receitaBx.CERTIFICADO().toString()));

		passwordTextField.setText(receitaBx.CERTIFICADO().PASS());

		customerComboBox.removeAllItems();
		if (receitaBx.PROCURADOR() != null) {
			customerComboBox.setModel(getProcuratorModel((Certificate) certificateComboBox.getSelectedItem()));
			customerComboBox.setSelectedItem(receitaBx.PROCURADOR());
			customerDocumentTextField.setText(receitaBx.PROCURADOR().CLIENTE_DOC());
		}

		profileTypeValueTextField.setVisible(false);

		if (receitaBx.PERFIL() != null && receitaBx.PERFIL().equals(profileProcurador.getText())) {
			profileProcurador.setSelected(true);
			profileTypeComboBox.setVisible(true);
			profileTypeValueTextField.setVisible(true);
			profileTypeComboBox.setSelectedItem(receitaBx.PERFIL_TYPE());
			profileTypeValueTextField.setValue(receitaBx.PERFIL_VALUE());
		}

		populateGrid();

		systemComboBox.setSelectedItem(receitaBx.SISTEMA());
		systemFileTypeComboBox.setSelectedItem(receitaBx.TIPO_ARQUIVO());
		systemSearchTypeComboBox.setSelectedItem(receitaBx.TIPO_PESQUISA());

		isConfigurationOkay();
	}

	private boolean isConfigurationOkay() {
		boolean found = false;

		List<Setting> list = Main.getAppData().getLastListAdded(Setting.class);

		if (list.size() > 0) {

			Setting obj = list.getFirst();

			if ((obj.SOFTWARE_NAME() != null && !obj.SOFTWARE_NAME().isBlank())
					&& (obj.SOFTWARE_PATH() != null && !obj.SOFTWARE_PATH().isBlank())
					&& (obj.SOFTWARE_PROGRAM() != null && !obj.SOFTWARE_PROGRAM().isBlank())
					&& (obj.DOWNLOAD_FOLDER() != null && !obj.DOWNLOAD_FOLDER().isBlank())
					&& (obj.LOG_FOLDER() != null && !obj.LOG_FOLDER().isBlank())
					&& (obj.SAVE_FOLDER() != null && !obj.SAVE_FOLDER().isBlank()))
				found = true;
		}

		if (!found) {
			JOptionPane.showMessageDialog(MainForm.this,
					"Os dados da configuração não foram encontrados.\nFavor informá-los.", "Atenção",
					JOptionPane.WARNING_MESSAGE);
			setting.doClick();
		}

		return found;
	}

	private boolean isTheProgramClosed() {
		boolean result = true;

		String userHome = System.getProperty("user.home");

		String derbyHiddenFolder = userHome + "\\ReceitanetBX\\db.lck";
		String derbyHiddenFolderRenamed = derbyHiddenFolder + "1";
		
		Path source = Paths.get(derbyHiddenFolder);
		Path target = Paths.get(derbyHiddenFolderRenamed);

		try {
			Files.move(source, target);
			//If no error happens it means the program is closed, so we must revert it back to the original name
			Files.move(target, source);
		} catch (IOException e) {
			logger.warn("{}", e.getMessage());
			if (e.getMessage().contains("it is being used")) {
				JOptionPane.showMessageDialog(MainForm.this,
						"O programa Receitanet BX esta aberto! Feche-o.\nO sistema será reponsável por abrir ele.",
						"Atenção", JOptionPane.WARNING_MESSAGE);
				result = false;
				;
			}
		}

		return result;
	}

	private DefaultComboBoxModel<Procurator> getProcuratorModel(Certificate certificate) {

		List<Procurator> result = new ArrayList<>();

		if (certificate != null) {

			List<Procurator> list = Main.getAppData().getLastListAdded(Procurator.class);

			if (list.size() > 0) {
				for (var obj : list) {
					if (obj.CERTIFICADO().equals(certificate.NAME())) {
						result.add(obj);
					}
				}
			}

			if (result.size() == 0) {
				result.add(new Procurator(certificate.NAME(), null, null, certificate.CN(), certificate.CNDOC(), null));
			}

		}

		DefaultComboBoxModel<Procurator> model = new DefaultComboBoxModel<>();
		model.addAll(result);

		return model;
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

		if (customerComboBox.getSelectedIndex() == -1)
			result.append("Favor selecionar um cliente.\n");
		else {
			Procurator PROCURADOR = (Procurator) customerComboBox.getSelectedItem();
			if (PROCURADOR.VALIDADE() != null) {
				if (!ValidateDate.isGraterThanToday(ValidateDate.convertDateToString(PROCURADOR.VALIDADE())))
					result.append("A procuração para este cliente esta vencida! ["
							+ ValidateDate.convertDateToString(PROCURADOR.VALIDADE()) + "].\n");
			}
		}

		if (customerDocumentTextField.getText().isBlank())
			result.append("Favor informar o cnpj do cliente.\n");

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
			if (c instanceof JFormattedTextField) {
				JFormattedTextField formattedTextField = (JFormattedTextField) c;
				if (formattedTextField.getName().equals(SpedSearchField.DATA_INICIO.getValue())) {
					DATA_INICIO = Objects.requireNonNullElse(formattedTextField.getValue(), "").toString();
					if (!ValidateDate.isValidDate(DATA_INICIO)) {
						result.append("Favor informar uma data inicial válida. [" + DATA_INICIO + "]\n");
					}
					else if (!ValidateDate.isLessOrEqualThanToday(DATA_INICIO)) {
						result.append("Favor informar uma data inicial que seja menor ou igual a data atual.\n");
					}
				} else if (formattedTextField.getName().equals(SpedSearchField.DATA_FIM.getValue())) {
					DATA_FIM = Objects.requireNonNullElse(formattedTextField.getValue(), "").toString();
					if (!ValidateDate.isValidDate(DATA_FIM)) {
						result.append("Favor informar uma data final válida. [" + DATA_FIM + "]\n");
					} else {
						if (!ValidateDate.isValidRange(DATA_INICIO, DATA_FIM)) {
							result.append("Favor informar uma data inicial que seja anterior a data final.\n");
						}
						else if (!ValidateDate.isLessOrEqualThanToday(DATA_FIM)) {
							result.append("Favor informar uma data final que seja menor ou igual a data atual.\n");
						}
					}
				} else if (formattedTextField.getName().equals(SpedSearchField.CNPJ_INCORPORADORA.getValue())) {
					if (formattedTextField.getValue() == null
							|| !ValidateCpfCnpj.isCnpjValid(formattedTextField.getValue().toString())) {
						result.append("Favor informar um cnpj válido para a Incorporadora.\n");
					}
				} else if (formattedTextField.getName().equals(SpedSearchField.CNPJ_ESTABELECIMENTO.getValue())) {
					if (formattedTextField.getValue() == null
							|| !ValidateCpfCnpj.isCnpjValid(formattedTextField.getValue().toString())) {
						result.append("Favor informar um cnpj válido para o Estabelecimento.\n");
					}
				}
//			}
//			else if (c instanceof JTextField) {
//				JTextField textField = (JTextField) c;
//				if (textField.getName().equals(SpedSearchField.INSCRICAO_ESTADUAL.getValue())) {
//					String INSCRICAO_ESTADUAL = textField.getText();
//					if (INSCRICAO_ESTADUAL.isBlank()) {
//						result.append("Favor informar uma inscrição estadual válida.\n");
//					}
//				}
			} else if (c instanceof JComboBox) {
				JComboBox<?> comboBox = (JComboBox<?>) c;
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
//				if (checkBox.getName().equals(SpedSearchField.BUSCAR_TODOS_ESTABLECIMENTOS.getValue())) {
//					BUSCAR_TODOS_ESTABLECIMENTOS = checkBox.isSelected();
//				} else if (checkBox.getName().equals(SpedSearchField.ULTIMO_ARQUIVO_TRANSMITIDO.getValue())) {
//					ULTIMO_ARQUIVO_TRANSMITIDO = checkBox.isSelected();
//				}
			}
		}

		return result.toString();
	}

	private boolean isThereItemToBeProcessed() {
		boolean result = false;
		for (int row = 0; row < tableModel.getRowCount(); row++) {
			Status status = (Status) tableModel.getValueAt(row, getModelColumnIndex("Status"));
			if (status != Status.SUCCESS) {
				result = true;
				break;
			}
		}
		return result;
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

				Map<String, String> dynamic = rowDynamicData.get(row);

				for (Component c : systemSearchFieldsPanel.getComponents()) {
					if (c instanceof JTextField) {
						JTextField textField = (JTextField) c;
						tableModel.setValueAt(textField.getText(), row, getModelColumnIndex(textField.getName()));
						dynamic.put(textField.getName(), textField.getText());
					}
					if (c instanceof JComboBox) {
						JComboBox<?> comboBox = (JComboBox<?>) c;
						tableModel.setValueAt(comboBox.getSelectedItem(), row, getModelColumnIndex(comboBox.getName()));
						dynamic.put(comboBox.getName(), comboBox.getSelectedItem().toString());
					}
					if (c instanceof JCheckBox) {
						JCheckBox checkBox = (JCheckBox) c;
						tableModel.setValueAt(checkBox.isSelected(), row, getModelColumnIndex(checkBox.getName()));
						dynamic.put(checkBox.getName(), String.valueOf(checkBox.isSelected()));
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

					tableModel.setValueAt(status, row, getModelColumnIndex("Status"));
					break;
				}
			}
		}
	}

	private ReceitaBx getItem(int row) {

		ReceitaBx result = null;

		String sistema = tableModel.getValueAt(row, getModelColumnIndex("Sistema")).toString();
		String tipoArquivo = tableModel.getValueAt(row, getModelColumnIndex("Tipo de Arquivo")).toString();
		String tipoPesquisa = tableModel.getValueAt(row, getModelColumnIndex("Tipo de Pesquisa")).toString();

		for (ReceitaBx obj : receitaBxList) {

			if (obj.SISTEMA().equals(sistema) && obj.TIPO_ARQUIVO().equals(tipoArquivo)
					&& obj.TIPO_PESQUISA().equals(tipoPesquisa)) {
				result = obj;
				break;
			}
		}

		return result;
	}
	
	private ReceitaBx getItem() {
		
		ReceitaBx result = null;
		
		String sistema = systemComboBox.getSelectedItem().toString();
		String tipoArquivo = systemFileTypeComboBox.getSelectedItem().toString();
		String tipoPesquisa = systemSearchTypeComboBox.getSelectedItem().toString();
		
		for (int row = 0; row < tableModel.getRowCount(); row++) {

			if (tableModel.getValueAt(row, getModelColumnIndex("Sistema")).toString().equals(sistema)
					&& tableModel.getValueAt(row, getModelColumnIndex("Tipo de Arquivo")).toString()
							.equals(tipoArquivo)
					&& tableModel.getValueAt(row, getModelColumnIndex("Tipo de Pesquisa")).toString()
							.equals(tipoPesquisa)) {

				result = receitaBxList.get(row);
				break;
			}
		}
		
		if (result == null) {

			for (int row = 0; row < tableModel.getRowCount(); row++) {

				if (tableModel.getValueAt(row, getModelColumnIndex("Sistema")).toString().equals(sistema)
						&& tableModel.getValueAt(row, getModelColumnIndex("Tipo de Arquivo")).toString()
								.equals(tipoArquivo)) {

					result = receitaBxList.get(row);
					break;
				}
			}
			
		}
		
		return result;
	}

	private void emptyGrid() {
		for (int row = 0; row < tableModel.getRowCount(); row++) {
			tableModel.removeRow(row);
		}
		rowDynamicData.clear();
	}

	private void populateGrid() {

		emptyGrid();

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
					if (confirmDeletion()) {
						tableModel.removeRow(currentRow);
						if (currentRow < rowDynamicData.size()) {
							rowDynamicData.remove(currentRow);
						}
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

	private boolean confirmDeletion() {
		boolean result = false;
		String[] options = { "Sim", "Não" };
		int choice = JOptionPane.showOptionDialog(MainForm.this, // Parent component
				"Deseja remover este item?", // Message
				"Confirmação", // Title
				JOptionPane.YES_NO_OPTION, // Option type
				JOptionPane.QUESTION_MESSAGE, // Message type
				null, // Icon (null for default)
				options, // Custom button labels
				options[1] // Default button focused
		);
		if (choice == 0) { // Sim
			result = true;
		}
		return result;
	}

	private void wireRemoverColumn() {
		javax.swing.table.TableColumn removerCol = itemsTable.getColumn("");
		removerCol.setPreferredWidth(85);
		removerCol.setMaxWidth(85);
	}

	private int getModelColumnIndex(String columnName) {
		for (int c = 0; c < tableModel.getColumnCount(); c++) {
			if (tableModel.getColumnName(c).equals(columnName)) {
				return c;
			}
		}
		return -1;
	}

	private List<ReceitaBx> getListOfFiles()
			throws InvalidCertificate, InvalidKeyException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IOException {

		List<ReceitaBx> result = new ArrayList<>();

		String SCREEN = screenResolutionTextField.getText();

		List<Setting> list = Main.getAppData().getLastListAdded(Setting.class);
		Setting CONFIGURACAO = list.getFirst();

		Ref<Certificate> CERTIFICADO = new Ref<>((Certificate) certificateComboBox.getSelectedItem());
		String SENHA = passwordTextField.getText();

		ValidatePfx.load(CERTIFICADO, SENHA);

		Procurator PROCURADOR = ((Procurator) customerComboBox.getSelectedItem());

		String PERFIL = profileContribuinte.getText();
		String PERFIL_TYPE = "";
		String PERFIL_VALUE = "";

		if (profileProcurador.isSelected()) {
			PERFIL = profileProcurador.getText();
			PERFIL_TYPE = profileTypeComboBox.getSelectedItem().toString();
			PERFIL_VALUE = profileTypeValueTextField.getText();
		}

		for (int row = 0; row < tableModel.getRowCount(); row++) {

			Status STATUS = (Status) tableModel.getValueAt(row, getModelColumnIndex("Status"));

			if (STATUS != Status.SUCCESS) {

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

				// Dynamic fields — read from the stored map for this row
				Map<String, String> dynamic = rowDynamicData.get(row);
				for (Map.Entry<String, String> entry : dynamic.entrySet()) {
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

				result.add(new ReceitaBx(SCREEN, CONFIGURACAO, CERTIFICADO.get(), PROCURADOR, PERFIL,
						PERFIL_TYPE, PERFIL_VALUE, SISTEMA, TIPO_ARQUIVO, TIPO_PESQUISA, DATA_INICIO, DATA_FIM,
						CNPJ_INCORPORADORA, TIPO_EVENTO, BAIXAR_ARQUIVO_ASSINADO, CNPJ_ESTABELECIMENTO,
						BUSCAR_TODOS_ESTABLECIMENTOS, INSCRICAO_ESTADUAL, ULTIMO_ARQUIVO_TRANSMITIDO,
						ULTIMO_PEDIDO_SOLICITADO, DATA_HORA_CONCLUSAO_PROCESSAMENTO, MENSAGEM_CONCLUSAO_PROCESSAMENTO,
						PERIODOS_FALTANDO, TOTAL_PERIODOS_FALTANDO, STATUS));
			}
		}

		// saveListOfFiles(result);

		return result;
	}

	private void saveListOfFiles(List<ReceitaBx> list) {

		logger.info("Saving customer data...");

		if (Main.getAppData().getSequence(ReceitaBx.class) == 0) {
			Main.getAppData().setSequence(ReceitaBx.class, Main.getAppData().nextSequence(ReceitaBx.class));
		}

		Main.getAppData().addList(ReceitaBx.class, Main.getAppData().getSequence(ReceitaBx.class), list);

		Main.saveAppData();

		logger.info("Customer data was saved with success.");
		
		receitaBxList = Main.getAppData().getLastListAdded(ReceitaBx.class);
		
		if (receitaBxList.getLast().CONFIGURACAO().DATA_UPDATED() == false) {
			
			logger.info("Saving setting data...");
			
			List<Setting> settingList = Main.getAppData().getLastListAdded(Setting.class);
			
			Setting setting = settingList.getLast();
			
			settingList.set(settingList.size() - 1, new Setting(setting.SOFTWARE_NAME(), setting.SOFTWARE_PATH(), setting.SOFTWARE_PROGRAM(), setting.DOWNLOAD_FOLDER(),
					setting.SAVE_FOLDER(), setting.LOG_FOLDER(), setting.SAVE_LOG(), setting.MAKE_SUBFOLDER(), setting.AUTO_DOWNLOAD(), setting.NUMBER_DOWNLOAD_SIMULTANEOUS(),
					setting.MINUTES_FOR_NEXT_ORDER_UPDATE(), setting.KEEP_WHICH_FILES(), false));
			
			Main.getAppData().addList(Setting.class, Main.getAppData().getSequence(Setting.class), settingList);

			Main.saveAppData();
			
			logger.info("Setting data was saved with success.");

		}
	}

	private JMenuBar createMenuBar() {

		JMenuItem newCustomer = new JMenuItem(Menu.NEW.getValue());
		newCustomer.setMnemonic(KeyEvent.VK_N);
		newCustomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				logger.info("Creating a new customer data...");

				certificateComboBox.setSelectedIndex(-1);
				passwordTextField.setText("");
				customerComboBox.removeAllItems();
				customerDocumentTextField.setText("");
				profileContribuinte.setSelected(true);
				systemSearchTypeComboBox.setSelectedIndex(-1);
				systemFileTypeComboBox.setSelectedIndex(-1);
				systemComboBox.setSelectedIndex(-1);

				for (Component c : systemSearchFieldsPanel.getComponents()) {
					if (c instanceof JFormattedTextField) {
						JFormattedTextField textField = (JFormattedTextField) c;
						textField.setValue("");
					} else if (c instanceof JTextField) {
						JTextField textField = (JTextField) c;
						textField.setText("");
					}
					if (c instanceof JComboBox) {
						JComboBox<?> comboBox = (JComboBox<?>) c;
						comboBox.setSelectedIndex(-1);
					}
					if (c instanceof JCheckBox) {
						JCheckBox checkBox = (JCheckBox) c;
						checkBox.setSelected(false);
					}
				}

				emptyGrid();

				Main.getAppData().setSequence(ReceitaBx.class, Main.getAppData().nextSequence(ReceitaBx.class));

				Main.getAppData().addList(ReceitaBx.class, Main.getAppData().getSequence(ReceitaBx.class),
						new ArrayList<>());

				receitaBxList = Main.getAppData().getLastListAdded(ReceitaBx.class);

				receitaBx = new ReceitaBx();

			}
		});

		JMenuItem historic = new JMenuItem(Menu.HISTORIC.getValue());
		historic.setMnemonic(KeyEvent.VK_H);
		historic.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		historic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HistoricForm form = new HistoricForm(MainForm.this, Menu.HISTORIC);
				form.addObjectListener(new Listenable() {
					@Override
					public void value(Object... objs) {
						// performActions(form, objs);
					}
				});
				form.load();
			}
		});

		JMenuItem procurator = new JMenuItem(Menu.PROCURATOR.getValue());
		procurator.setMnemonic(KeyEvent.VK_P);
		procurator.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		procurator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProcuratorForm form = new ProcuratorForm(MainForm.this, Menu.PROCURATOR);
				form.addObjectListener(new Listenable() {
					@Override
					public void value(Object... objs) {
						String action = (String) objs[0];
						if (action.equals(Menu.CLOSE.getValue())) {
							form.close();
						}
					}
				});
				form.load();
			}
		});

		JMenuItem restart = new JMenuItem(Menu.RESTART.getValue());
		restart.setMnemonic(KeyEvent.VK_R);
		restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.value(Menu.RESTART.getValue());
			}
		});

		setting = new JMenuItem(Menu.SETTING.getValue());
		setting.setMnemonic(KeyEvent.VK_C);
		setting.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		setting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingForm form = new SettingForm(MainForm.this, Menu.SETTING);
				form.addObjectListener(new Listenable() {
					@Override
					public void value(Object... objs) {
						String action = (String) objs[0];
						if (action.equals(Menu.CLOSE.getValue())) {
							form.close();
						}
					}
				});
				form.load();
			}
		});

		JMenuItem exit = new JMenuItem(Menu.EXIT.getValue());
		exit.setMnemonic(KeyEvent.VK_S);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.value(Menu.EXIT.getValue());
			}
		});

		newCustomer.setEnabled(false);
		historic.setEnabled(false);
		
		JMenu fileMenu = new JMenu("Menu");
		fileMenu.setMnemonic(KeyEvent.VK_M);
		fileMenu.add(newCustomer);
		fileMenu.add(historic);
		fileMenu.add(procurator);
		fileMenu.add(restart);
		fileMenu.addSeparator();
		fileMenu.add(setting);
		fileMenu.addSeparator();
		fileMenu.add(exit);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);

		return menuBar;
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