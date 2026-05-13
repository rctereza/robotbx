package com.rctereza.robotbx.views;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Main;
import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.models.Procurator;
import com.rctereza.robotbx.tools.FileUtils;
import com.rctereza.robotbx.tools.ValidateCpfCnpj;
import com.rctereza.robotbx.tools.ValidateDate;

import net.miginfocom.swing.MigLayout;

public class ProcuratorForm extends JDialog {

	private static final long serialVersionUID = -8935336038076796989L;

	private static final Logger logger = LoggerFactory.getLogger(ProcuratorForm.class);

	private JPanel panelMain;

	private JLabel certificateLabel;
	private JComboBox<Certificate> certificateComboBox;
	private JButton certificateLoadButton;

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
			cnpjMask.setPlaceholderCharacter('_');

			dateMask = new MaskFormatter("##/##/####");
			dateMask.setPlaceholderCharacter('_');

		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}

		// LINE 1
		certificateLabel = new JLabel("Selecione um certificado");
		certificateComboBox = new JComboBox<>();
		certificateComboBox.setModel(FileUtils.getModelOfCertificates());

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
		customerLabel = new JLabel("Nome do cliente *");
		customerTextField = new JTextField();

		// LINE 3
		customerDocumentLabel = new JLabel("CNPJ do cliente *");
		customerDocumentTextField = new JFormattedTextField(cnpjMask);
		customerDocumentTextField.setColumns(12);

		// LINE 4
		expirationDateLabel = new JLabel("Data de validade *");
		expirationDateTextField = new JFormattedTextField(dateMask);
		expirationDateTextField.setColumns(8);

		// LINE 5
		addButton = new JButton("Adicionar");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = validateFormFields();
				if (result.equals("")) {
					String CERTIFICATE_NAME = ((Certificate) certificateComboBox.getSelectedItem()).toString();
					String CUSTOMER_NAME = customerTextField.getText();
					String CUSTOMER_DOC = customerDocumentTextField.getValue().toString();
					String EXPIRATION_DATE = expirationDateTextField.getValue().toString();
					model.addObject(new Procurator(CERTIFICATE_NAME, CUSTOMER_NAME, CUSTOMER_DOC, EXPIRATION_DATE));
				} else {
					JOptionPane.showMessageDialog(ProcuratorForm.this, result, "Atenção", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// LINE 5
		delButton = new JButton("Excluir");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
				}
			}
		});

		model = new JModel(getList());

		table = new JTable(model);
		table.setRowHeight(24); // important for icons
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		// Activate the button editor on a single click instead of double-click
		table.putClientProperty("JTable.autoStartsEdit", false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new java.awt.Dimension(0, 300));

		saveButton = new JButton("Salvar");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("Saving procurator data...");

				if (Main.getAppData().getSequence(Procurator.class) == 0) {
					Main.getAppData().setSequence(Procurator.class, Main.getAppData().nextSequence(Procurator.class));
				}

				Main.getAppData().addList(Procurator.class, Main.getAppData().getSequence(Procurator.class), model.getList());

				Main.saveAppData();

				model.resetModifiedFlag();
				
				JOptionPane.showMessageDialog(ProcuratorForm.this, "Os dados foram salvos com sucesso.", "informação", JOptionPane.INFORMATION_MESSAGE);

				logger.info("Procurator data was saved with success.");
			}
		});

		closeButton = new JButton("Fechar");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.isModified()) {
					String[] options = { "Sim", "Não" };
					int choice = JOptionPane.showOptionDialog(ProcuratorForm.this, // Parent component
							"Houveram modificações. Deseja sair sem salvá-las?", // Message
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

	private List<Procurator> getList() {
		List<Procurator> list = Main.getAppData().getLastListAdded(Procurator.class);
		
		List<Procurator> deepCopy = list.stream()
			    .map(p -> new Procurator(p.CERTIFICATE_NAME(), p.CUSTOMER_NAME(), p.CUSTOMER_DOC(), p.EXPIRE_DATE()))
			    .collect(Collectors.toCollection(ArrayList::new)); //collect() generates a list that can be changed (mutable) 
			    // .toList(); // toList() generates a list that cannot be changed (immutable)
		
		return deepCopy;
	}
	
	private String validateFormFields() {
		StringBuilder result = new StringBuilder("");

		if (certificateComboBox.getSelectedIndex() == -1)
			result.append("Favor selecionar um certificado.\n");

		if (customerTextField.getText().isBlank())
			result.append("Favor informar o nome do cliente.\n");

		if (customerDocumentTextField.getText().isBlank())
			result.append("Favor informar o cnpj do cliente.\n");
		else if (!ValidateCpfCnpj.isCnpjValid(customerDocumentTextField.getValue().toString()))
			result.append("Favor informar um cnpj válido para o cliente.\n");

		if (!ValidateDate.isValidDate(expirationDateTextField.getValue().toString()))
			result.append("Favor informar uma data de validade.\n");
		else if (!ValidateDate.isGraterThanToday(expirationDateTextField.getValue().toString()))
			result.append("Favor informar um data de validade maior que a data de hoje.\n");

		return result.toString();
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

		private final String[] columns = { "Procurador", "Cliente", "CNPJ", "Data de Validade" };

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
				return obj.CERTIFICATE_NAME();

			case 1:
				return obj.CUSTOMER_NAME();

			case 2:
				return obj.CUSTOMER_DOC();

			case 3:
				return obj.EXPIRE_DATE();

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
