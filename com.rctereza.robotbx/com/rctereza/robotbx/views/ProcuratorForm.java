package com.rctereza.robotbx.views;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Main;
import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.exceptions.InvalidCertificate;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.models.Procurator;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.TableUtils;
import com.rctereza.robotbx.tools.ValidateCpfCnpj;
import com.rctereza.robotbx.tools.ValidateDate;
import com.rctereza.robotbx.tools.ValidatePfx;
import com.rctereza.robotbx.wrappers.Ref;

import net.miginfocom.swing.MigLayout;

public class ProcuratorForm extends JDialog {

	private static final long serialVersionUID = -8935336038076796989L;

	private static final Logger logger = LoggerFactory.getLogger(ProcuratorForm.class);

	private JPanel panelMain;

	private JLabel certificateLabel;
	private JComboBox<Certificate> certificateComboBox;
	private JButton certificateLoadButton;

	private JLabel procuratorLabel;
	private JTextField procuratorTextField;

	private JLabel procuratorDocumentLabel;
	private JFormattedTextField procuratorDocumentTextField;

	private JLabel customerLabel;
	private JTextField customerTextField;

	private JLabel customerDocumentLabel;
	private JFormattedTextField customerDocumentTextField;

	private JLabel expirationDateLabel;
	private JFormattedTextField expirationDateTextField;

	private JButton addButton;
	private JButton delButton;

	private JModel model;
	private JTable table;
	private JScrollPane tableScrollPane;

	private JButton closeButton;
	private JButton saveButton;

	private Listenable listener;

	public ProcuratorForm(Window parent, Menu menu) {
		super(parent, menu.getValue(), ModalityType.APPLICATION_MODAL);

		// setIconImage(Resources.getImage(Constants.SOFTWARE_ICON, null));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				listener.value(Menu.CLOSE.getValue());
			}
		});

		MaskFormatter cnpjMask = null;
		MaskFormatter dateMask = null;

		try {
			cnpjMask = new MaskFormatter("##.###.###/####-##");
			cnpjMask.setValueContainsLiteralCharacters(false);
			cnpjMask.setPlaceholderCharacter('_');

			dateMask = new MaskFormatter("##/##/####");
			// dateMask.setValueContainsLiteralCharacters(false);
			dateMask.setPlaceholderCharacter('_');
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}

		// LINE 1
		certificateLabel = new JLabel("Selecione um certificado");
		certificateComboBox = new JComboBox<>();
		certificateComboBox.setModel(loadCertificateComboBoxValue());
		certificateComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Ref<Certificate> certificate = new Ref<>((Certificate) e.getItem());
					String PROCURATOR_NAME = certificate.get().CN();
					String PROCURATOR_DOCUMENT = certificate.get().CNDOC();
					if (PROCURATOR_NAME == null || PROCURATOR_NAME.equals("")) {
						if (validateCertificate(certificate)) {
							PROCURATOR_NAME = certificate.get().CN();
							PROCURATOR_DOCUMENT = certificate.get().CNDOC();
							updateCertificateComboBoxValue(certificate.get());
						}
					}
					procuratorTextField.setText(PROCURATOR_NAME);
					procuratorDocumentTextField.setValue(PROCURATOR_DOCUMENT);
					customerTextField.requestFocusInWindow();
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

		// LINE 2
		procuratorLabel = new JLabel("Nome do procurador");
		procuratorTextField = new JTextField();
		procuratorTextField.setEnabled(false);

		// LINE 3
		procuratorDocumentLabel = new JLabel("CNPJ do procurador");
		procuratorDocumentTextField = new JFormattedTextField(cnpjMask);
		procuratorDocumentTextField.setColumns(12);
		procuratorDocumentTextField.setEnabled(false);

		// LINE 4
		customerLabel = new JLabel("Nome do cliente *");
		customerTextField = new JTextField();

		// LINE 5
		customerDocumentLabel = new JLabel("CNPJ do cliente *");
		customerDocumentTextField = new JFormattedTextField(cnpjMask);
		customerDocumentTextField.setColumns(12);

		// LINE 6
		expirationDateLabel = new JLabel("Data de validade *");
		expirationDateTextField = new JFormattedTextField(dateMask);
		expirationDateTextField.setColumns(8);

		// LINE 7
		addButton = new JButton("Incluir");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = validateFormFields();
				if (result.equals("")) {
					String CERTIFICATE_NAME = ((Certificate) certificateComboBox.getSelectedItem()).toString();
					String PROCURATOR_NAME = procuratorTextField.getText();
					String PROCURATOR_DOC = (String) procuratorDocumentTextField.getValue();
					String CUSTOMER_NAME = customerTextField.getText();
					String CUSTOMER_DOC = (String) customerDocumentTextField.getValue();
					Date EXPIRATION_DATE = ValidateDate.convertStringToDate(expirationDateTextField.getText());

					if (addButton.getText().equals("Incluir")) {
						model.addObject(new Procurator(CERTIFICATE_NAME, PROCURATOR_NAME, PROCURATOR_DOC, CUSTOMER_NAME,
								CUSTOMER_DOC, EXPIRATION_DATE));
					} else {
						model.updObject(new Procurator(CERTIFICATE_NAME, PROCURATOR_NAME, PROCURATOR_DOC, CUSTOMER_NAME,
								CUSTOMER_DOC, EXPIRATION_DATE), table.getSelectedRow());
					}
					resetFormFields();
				} else {
					if (!result.equals("PASSWORDOKAY")) {
						JOptionPane.showMessageDialog(ProcuratorForm.this, result, "Atenção",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		// LINE 8
		delButton = new JButton("Excluir");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (delButton.getText().equals("Cancelar")) {
					resetFormFields();
				} else {
					int selectedRow = table.getSelectedRow();
					if (selectedRow >= 0) {
						String[] options = { "Sim", "Não" };
						int choice = JOptionPane.showOptionDialog(ProcuratorForm.this, // Parent component
								"Deseja remover este item?", // Message
								"Confirmação", // Title
								JOptionPane.YES_NO_OPTION, // Option type
								JOptionPane.QUESTION_MESSAGE, // Message type
								null, // Icon (null for default)
								options, // Custom button labels
								options[1] // Default button focused
						);
						if (choice == 0) { // Sim
							model.removeObject(selectedRow);
						}
						table.clearSelection();
					}
				}
			}
		});

		model = new JModel(getTableList());

		table = new JTable(model);
		table.setRowHeight(24); // important for icons
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Activate the button editor on a single click instead of double-click
		table.putClientProperty("JTable.autoStartsEdit", false);

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem changeItem = new JMenuItem("Alterar");
		changeItem.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row != -1) {
				int colCount = table.getColumnCount();
				for (int col = 0; col < colCount; col++) {
					String columnName = table.getColumnName(col);
					Object value = table.getValueAt(row, col);
					if (columnName.equals("Procurador")) {
						certificateComboBox.setSelectedIndex(FileUtils.getCertificateIndex(value.toString()));
					}
					if (columnName.equals("Cliente")) {
						customerTextField.setText(value.toString());
					}
					if (columnName.equals("CNPJ")) {
						customerDocumentTextField.setValue(value);
					}
					if (columnName.equals("Validade")) {
						expirationDateTextField.setValue(ValidateDate.convertDateToString((Date) value));
					}
				}
				addButton.setText("Alterar");
				delButton.setText("Cancelar");
			}
		});

		popupMenu.add(changeItem);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				handlePopup(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				handlePopup(e);
			}

			private void handlePopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					int row = table.rowAtPoint(e.getPoint());
					if (row >= 0) {
						table.setRowSelectionInterval(row, row);
					}
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		// CNPJ
		table.getColumnModel().getColumn(2).setMaxWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(2).setCellRenderer(TableUtils.getDocumentRenderer());

		// Expire Date
		table.getColumnModel().getColumn(3).setMaxWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(3).setCellRenderer(TableUtils.getDateRenderer());

		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new java.awt.Dimension(0, 300));

		saveButton = new JButton("Salvar");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!model.isModified()) {

					JOptionPane.showMessageDialog(ProcuratorForm.this,
							"Não há a necessidade de salvar os dados. Nenhuma operação foi executada.", "Atenção",
							JOptionPane.WARNING_MESSAGE);

				} else {

					logger.info("Saving procurator data...");

					if (Main.getAppData().getSequence(Procurator.class) == 0) {
						Main.getAppData().setSequence(Procurator.class,
								Main.getAppData().nextSequence(Procurator.class));
					}

					Main.getAppData().addList(Procurator.class, Main.getAppData().getSequence(Procurator.class),
							model.getList());

					if (Main.getAppData().getSequence(Certificate.class) == 0) {
						Main.getAppData().setSequence(Certificate.class,
								Main.getAppData().nextSequence(Certificate.class));
					}

					Main.getAppData().addList(Certificate.class, Main.getAppData().getSequence(Certificate.class),
							getCertificateComboBoxListValidated());

					Main.saveAppData();

					logger.info("Procurator data was saved with success.");

					model.resetModifiedFlag();

					JOptionPane.showMessageDialog(ProcuratorForm.this, "Os dados foram salvos com sucesso.",
							"informação", JOptionPane.INFORMATION_MESSAGE);

				}
			}
		});

		closeButton = new JButton("Fechar");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.isModified()) {
					String[] options = { "Sim", "Não" };
					int choice = JOptionPane.showOptionDialog(ProcuratorForm.this, // Parent component
							"Houveram alterações. Deseja sair sem salvá-las?", // Message
							"Confirmação", // Title
							JOptionPane.YES_NO_OPTION, // Option type
							JOptionPane.QUESTION_MESSAGE, // Message type
							null, // Icon (null for default)
							options, // Custom button labels
							options[1] // Default button focused
					);
					if (choice == 0) { // Sim
						listener.value(Menu.CLOSE.getValue());
					}
				} else {
					listener.value(Menu.CLOSE.getValue());
				}
			}
		});

		panelMain = new JPanel(new MigLayout("", "[]10[]10[]", "[] [] []"));

		panelMain.add(certificateLabel, "left, sg 1");
		panelMain.add(certificateComboBox, "pushx, growx");
		panelMain.add(certificateLoadButton, "left, wrap");

		panelMain.add(procuratorLabel, "left, sg 1");
		panelMain.add(procuratorTextField, "pushx, growx, wrap");

		panelMain.add(procuratorDocumentLabel, "left, sg 1");
		panelMain.add(procuratorDocumentTextField, "wrap");

		panelMain.add(customerLabel, "left, sg 1");
		panelMain.add(customerTextField, "pushx, growx, wrap");

		panelMain.add(customerDocumentLabel, "left, sg 1");
		panelMain.add(customerDocumentTextField, "wrap");

		panelMain.add(expirationDateLabel, "left, sg 1");
		panelMain.add(expirationDateTextField, "wrap");

		panelMain.add(addButton, "left");
		panelMain.add(delButton, "span2, right, wrap");
		panelMain.add(tableScrollPane, "span3, grow, wrap");
		panelMain.add(saveButton, "left");
		panelMain.add(closeButton, "span2, right, wrap");

		this.add(panelMain);

		setSize(1000, 500);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private boolean validateCertificate(Ref<Certificate> certificate) {
		boolean result = false;

		JPasswordField passwordField = new JPasswordField();

		SwingUtilities.invokeLater(() -> passwordField.requestFocusInWindow());

		int option = JOptionPane.showConfirmDialog(null, passwordField, "Informe a senha do certificado",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			String password = new String(passwordField.getPassword());
			try {
				ValidatePfx.load(certificate, password);
				JOptionPane.showMessageDialog(this, "O certificado foi validado com sucesso.", "Informação",
						JOptionPane.INFORMATION_MESSAGE);
				result = true;
			} catch (InvalidCertificate e1) {
				JOptionPane.showMessageDialog(ProcuratorForm.this, e1.getMessage(), "Atenção",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		return result;
	}

	private List<Procurator> getTableList() {
		List<Procurator> list = Main.getAppData().getLastListAdded(Procurator.class);

		List<Procurator> deepCopy = list.stream().map(p -> new Procurator(p.CERTIFICADO(), p.PROCURADOR(),
				p.PROCURADOR_DOC(), p.CLIENTE(), p.CLIENTE_DOC(), p.VALIDADE()))
				.collect(Collectors.toCollection(ArrayList::new)); // collect() generates a list that can be changed
																	// (mutable)
		// .toList(); // toList() generates a list that cannot be changed (immutable)
		return deepCopy;
	}

	private List<Certificate> getCertificateList() {
		List<Certificate> list = Main.getAppData().getLastListAdded(Certificate.class);

		List<Certificate> deepCopy = list.stream()
				.map(p -> new Certificate(p.ID(), p.NAME(), p.PATH(), p.PASS(), p.ALIAS(), p.SUBJECT(), p.ISSUER(),
						p.VALIDFROM(), p.VALIDTO(), p.CN(), p.CNDOC()))
				.collect(Collectors.toCollection(ArrayList::new)); // collect() generates a list that can be changed
																	// (mutable)
		// .toList(); // toList() generates a list that cannot be changed (immutable)
		return deepCopy;
	}

	private String validateFormFields() {
		StringBuilder result = new StringBuilder("");

		if (certificateComboBox.getSelectedIndex() == -1)
			result.append("Favor selecionar um certificado.\n");

		if (customerTextField.getText().isBlank())
			result.append("Favor informar o nome do cliente.\n");

		if (customerDocumentTextField.getValue() == null)
			result.append("Favor informar o cnpj do cliente.\n");
		else if (!ValidateCpfCnpj.isCnpjValid(customerDocumentTextField.getValue().toString()))
			result.append("Favor informar um cnpj válido para o cliente.\n");

		if (expirationDateTextField.getValue() == null)
			result.append("Favor informar uma data de validade.\n");
		else if (!ValidateDate.isValidDate(expirationDateTextField.getValue().toString()))
			result.append("Favor informar uma data de validade valida.\n");
		else if (!ValidateDate.isGraterThanToday(expirationDateTextField.getValue().toString()))
			result.append("Favor informar um data de validade maior que a data de hoje.\n");

		if (result.toString().equals("")) {

			if (procuratorTextField.getText().isBlank()) {

				JOptionPane.showMessageDialog(ProcuratorForm.this,
						"Para prosseguir será necessário informar a senha do certificado.", "Atenção",
						JOptionPane.WARNING_MESSAGE);

				Ref<Certificate> certificate = new Ref<>((Certificate) certificateComboBox.getSelectedItem());
				if (validateCertificate(certificate)) {
					procuratorTextField.setText(certificate.get().CN());
					procuratorDocumentTextField.setValue(certificate.get().CNDOC());
					updateCertificateComboBoxValue(certificate.get());
					result.append("PASSWORDOKAY");
				} else {
					result.append("Não será possivel prosseguir...\n");
				}
			}
		}

		return result.toString();

	}

	private void resetFormFields() {
		certificateComboBox.setSelectedIndex(-1);
		procuratorTextField.setText("");
		procuratorDocumentTextField.setValue("");
		customerTextField.setText("");
		customerDocumentTextField.setValue("");
		expirationDateTextField.setValue("");
		addButton.setText("Incluir");
		delButton.setText("Excluir");
		table.clearSelection();
	}

	private DefaultComboBoxModel<Certificate> loadCertificateComboBoxValue() {
		List<Certificate> result = new ArrayList<>();
		List<Certificate> list = getCertificateList();
		DefaultComboBoxModel<Certificate> model = FileUtils.getModelOfCertificates();
		for (int i = 0; i < model.getSize(); i++) {
			
			boolean found = false;
			
			for (Certificate obj : list) {
				if (model.getElementAt(i).NAME().equals(obj.NAME())) {
					result.add(obj);
					found = true;
					break;
				}
			}

			if (!found)
				result.add(model.getElementAt(i));
		}
		
		model.removeAllElements();
		model.addAll(result);
		return model;
	}

	private void updateCertificateComboBoxValue(Certificate certificate) {
		int newItemIndex = -1;
		List<Certificate> list = new ArrayList<>();
		DefaultComboBoxModel<Certificate> model = (DefaultComboBoxModel<Certificate>) certificateComboBox.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			if (model.getElementAt(i).NAME().equals(certificate.NAME())) {
				list.add(certificate);
				newItemIndex = i;
			} else {
				list.add(model.getElementAt(i));
			}
		}
		certificateComboBox.removeAllItems();
		model.addAll(list);
		certificateComboBox.setSelectedIndex(newItemIndex);
	}

	private List<Certificate> getCertificateComboBoxListValidated() {
		List<Certificate> list = new ArrayList<>();
		DefaultComboBoxModel<Certificate> model = (DefaultComboBoxModel<Certificate>) certificateComboBox.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			if (model.getElementAt(i).CN() != null && !model.getElementAt(i).CN().equals(""))
				list.add(model.getElementAt(i));
		}
		return list;
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

	// ==========================================
	// CUSTOM TABLE MODEL
	// ==========================================
	static class JModel extends AbstractTableModel {

		private static final long serialVersionUID = 5508898942706645608L;

		private final String[] columns = { "Procurador", "Cliente", "CNPJ", "Validade" };

		private List<Procurator> list;

		private boolean modified = false;

		public JModel(List<Procurator> list) {
			this.list = list;
		}

		@Override
		public int getRowCount() {
			return list.size();
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public String getColumnName(int column) {
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {

			Procurator obj = list.get(rowIndex);

			switch (columnIndex) {

			case 0:
				return obj.CERTIFICADO();

			case 1:
				return obj.CLIENTE();

			case 2:
				return obj.CLIENTE_DOC();

			case 3:
				return obj.VALIDADE();

			default:
				return null;
			}
		}

		// Makes cells not editable
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		// Add new row
		public void addObject(Procurator obj) {
			modified = true;
			list.add(obj);
			int lastRow = list.size() - 1;
			fireTableRowsInserted(lastRow, lastRow);
		}

		// Update rows
		public void updObject(Procurator obj, int rowIndex) {
			modified = true;
			list.set(rowIndex, obj);
			fireTableRowsUpdated(rowIndex, rowIndex);
		}

		// Remove row
		public void removeObject(int rowIndex) {
			modified = true;
			list.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}

		// Access original object
		public Procurator getObject(int rowIndex) {
			return list.get(rowIndex);
		}

		public List<Procurator> getList() {
			return this.list;
		}

		public boolean isModified() {
			return modified;
		}

		public void resetModifiedFlag() {
			modified = false;
		}
	}
}
