package com.rctereza.robotbx.views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.interfaces.Listenable;
import com.rctereza.robotbx.models.ReceitaBx;

import net.miginfocom.swing.MigLayout;

public class DetailsForm extends JDialog {

	private static final long serialVersionUID = -8935336038076796989L;
	
	private JTextField customerTextField;
	private JTextField customerDocumentTextField;
	private JTextField systemTextField;
	private JTextField systemFileTypeTextField;
	private JTextField systemSearchTypeTextField;
	private	JTextField startDateTextField;
	private JTextField endDateTextField;
	private JTextField dateTimeProcessingTextField;
	private JTextField messageProcessingTextField;
	private JTextField totalPeriodMissingTextField;
	private JTextArea periodMissingTextArea;
	private JTextField statusTextField;

	private Listenable listener;

	public DetailsForm(Window parent, Menu menu, ReceitaBx receitaBx) {
		super(parent, menu.getValue(), ModalityType.APPLICATION_MODAL);

		// setIconImage(Resources.getImage(Constants.SOFTWARE_ICON, null));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				listener.value(Menu.CLOSE.getValue());
			}
		});

		// LINE 1
		JLabel customerLabel = new JLabel("Nome do Cliente");
		customerTextField = new JTextField();
		customerTextField.setText(receitaBx.PROCURADOR().CLIENTE());
		customerTextField.setEditable(false);

		// LINE 2
		JLabel customerDocumentLabel = new JLabel("CNPJ do Cliente");
		customerDocumentTextField = new JTextField();
		customerDocumentTextField.setText(receitaBx.PROCURADOR().CLIENTE_DOC());
		customerDocumentTextField.setEditable(false);

		// LINE 3
		JLabel systemLabel = new JLabel("Sistema selecionado");
		systemTextField = new JTextField();
		systemTextField.setText(receitaBx.SISTEMA());
		systemTextField.setEditable(false);

		JLabel systemFileTypeLabel = new JLabel("Tipo de Arquivo selecionado");
		systemFileTypeTextField = new JTextField();
		systemFileTypeTextField.setText(receitaBx.TIPO_ARQUIVO());
		systemFileTypeTextField.setEditable(false);

		JLabel systemSearchTypeLabel = new JLabel("Tipo de Pesquisa selecionada");
		systemSearchTypeTextField = new JTextField();
		systemSearchTypeTextField.setText(receitaBx.TIPO_PESQUISA());
		systemSearchTypeTextField.setEditable(false);

		JLabel startDateLabel = new JLabel("Data de início");
		startDateTextField = new JTextField();
		startDateTextField.setText(receitaBx.DATA_INICIO());
		startDateTextField.setEditable(false);

		JLabel endDateLabel = new JLabel("Data de fim");
		endDateTextField = new JTextField();
		endDateTextField.setText(receitaBx.DATA_FIM());
		endDateTextField.setEditable(false);

		JLabel dateTimeProcessingLabel = new JLabel("Data do Processamento");
		dateTimeProcessingTextField = new JTextField();
		dateTimeProcessingTextField.setText(receitaBx.DATA_HORA_CONCLUSAO_PROCESSAMENTO());
		dateTimeProcessingTextField.setEditable(false);

		JLabel messageProcessingLabel = new JLabel("Resultado do Processamento");
		messageProcessingTextField = new JTextField();
		messageProcessingTextField.setText(receitaBx.MENSAGEM_CONCLUSAO_PROCESSAMENTO());
		messageProcessingTextField.setEditable(false);

		JLabel totalPeriodMissingLabel = new JLabel("Total de Período(s) faltando");
		totalPeriodMissingTextField = new JTextField();
		totalPeriodMissingTextField.setText(receitaBx.TOTAL_PERIODOS_FALTANDO().toString());
		totalPeriodMissingTextField.setEditable(false);

		JLabel periodMissingLabel = new JLabel("Período(s) faltando");
		periodMissingTextArea = new JTextArea(9, 20);
		periodMissingTextArea.setEditable(false);
		periodMissingTextArea.setText(receitaBx.PERIODOS_FALTANDO());
		periodMissingTextArea.setLineWrap(true);
		periodMissingTextArea.setWrapStyleWord(true);
		JScrollPane periodMissingScrollPane = new JScrollPane(periodMissingTextArea);

		JLabel statusLabel = new JLabel("Status");
		statusTextField = new JTextField();
		statusTextField.setText(receitaBx.STATUS().getDescription());
		statusTextField.setEditable(false);

		// LINE 4
		JButton copyButton = new JButton("Copiar");
		copyButton.setPreferredSize(new Dimension(100, 20));
		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copyDetailsToWindowsClipboard();
				JOptionPane.showMessageDialog(null,
						"Os dados dessa janela foram copiados para a Área de Transfêrencia do Windows.\nPressione CTRL+V para colá-los.",
						"Informação", JOptionPane.INFORMATION_MESSAGE);
				listener.value(Menu.CLOSE.getValue());
			}
		});

		JButton closeButton = new JButton("Fechar");
		closeButton.setPreferredSize(new Dimension(100, 20));
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.value(Menu.CLOSE.getValue());
			}
		});

		// JPanel panelMain = new JPanel(new MigLayout("wrap, insets 10, debug",
		// "[]10[]", "[]10[]"));
		JPanel panelMain = new JPanel(new MigLayout("", "[]10[]", "[] []"));

		panelMain.add(customerLabel, "left, sg 1");
		panelMain.add(customerTextField, "pushx, growx, wrap");

		panelMain.add(customerDocumentLabel, "left, sg 1");
		panelMain.add(customerDocumentTextField, "pushx, growx, wrap 10");

		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap 10");

		panelMain.add(systemLabel, "left, sg 1");
		panelMain.add(systemTextField, "pushx, growx, wrap");

		panelMain.add(systemFileTypeLabel, "left, sg 1");
		panelMain.add(systemFileTypeTextField, "pushx, growx, wrap");

		panelMain.add(systemSearchTypeLabel, "left, sg 1");
		panelMain.add(systemSearchTypeTextField, "pushx, growx, wrap 10");

		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap 10");

		panelMain.add(startDateLabel, "left, sg 1");
		panelMain.add(startDateTextField, "pushx, growx, wrap");

		panelMain.add(endDateLabel, "left, sg 1");
		panelMain.add(endDateTextField, "pushx, growx, wrap 10");

		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap 10");

		panelMain.add(dateTimeProcessingLabel, "left, sg 1");
		panelMain.add(dateTimeProcessingTextField, "pushx, growx, wrap");

		panelMain.add(messageProcessingLabel, "left, sg 1");
		panelMain.add(messageProcessingTextField, "pushx, growx, wrap");

		panelMain.add(totalPeriodMissingLabel, "left, sg 1");
		panelMain.add(totalPeriodMissingTextField, "pushx, growx, wrap");

		panelMain.add(periodMissingLabel, "left, sg 1");
		panelMain.add(periodMissingScrollPane, "pushx, growx, wrap");
		// Add to panel (MigLayout or any layout)
		// panelMain.add(periodMissingScrollPane, "width 200!, height 100!, wrap");

		panelMain.add(statusLabel, "left, sg 1");
		panelMain.add(statusTextField, "pushx, growx, wrap 10");

		panelMain.add(new JSeparator(JSeparator.HORIZONTAL), "span, grow, wrap 10");
		panelMain.add(copyButton, "grow 0, left");
		panelMain.add(closeButton, "grow 0, right");

		this.add(panelMain);

		setSize(800, 600);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	protected void copyDetailsToWindowsClipboard() {
		StringBuilder sb = new StringBuilder("");

		sb.append("Nome do Cliente..............: ").append(customerTextField.getText()).append("\n");
		sb.append("CNPJ do Cliente..............: ").append(customerDocumentTextField.getText()).append("\n");
		
		sb.append("Sistema selecionado..........: ").append(systemTextField.getText()).append("\n");
		sb.append("Tipo de Arquivo selecionado..: ").append(systemFileTypeTextField.getText()).append("\n");
		sb.append("Tipo de Pesquisa selecionada.: ").append(systemSearchTypeTextField.getText()).append("\n");
		
		sb.append("Data de início...............: ").append(startDateTextField.getText()).append("\n");
		sb.append("Data de fim..................: ").append(endDateTextField.getText()).append("\n");
		
		sb.append("Data do Processamento........: ").append(dateTimeProcessingTextField.getText()).append("\n");
		sb.append("Resultado do Processamento...: ").append(messageProcessingTextField.getText()).append("\n");
		sb.append("Total de Período(s) faltando.: ").append(totalPeriodMissingTextField.getText()).append("\n");
		sb.append("Período(s) faltando..........: ").append(periodMissingTextArea.getText()).append("\n");
		sb.append("Status.......................: ").append(statusTextField.getText()).append("\n");
		
		StringSelection selection = new StringSelection(sb.toString());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
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
}
