package com.rctereza.robotbx.ztest;

import java.awt.Container;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

public class TestingJFormattedTextField extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		TestingJFormattedTextField field = new TestingJFormattedTextField();
		field.testaJFormattedTextField();
	}

	private void testaJFormattedTextField() {
		Container janela = getContentPane();
		setLayout(null);

		// Define os rótulos dos botões
		JLabel labelCep = new JLabel("CEP: ");
		JLabel labelTel = new JLabel("Telefone: ");
		JLabel labelCpf = new JLabel("CPF: ");
		JLabel labelData = new JLabel("Data: ");
		JLabel labelPreco = new JLabel("Preço: ");
		labelCep.setBounds(50, 40, 100, 20);
		labelTel.setBounds(50, 80, 100, 20);
		labelCpf.setBounds(50, 120, 100, 20);
		labelData.setBounds(50, 160, 100, 20);
		labelPreco.setBounds(50, 200, 100, 20);

		// Define as máscaras
		MaskFormatter mascaraCep = null;
		MaskFormatter mascaraTel = null;
		MaskFormatter mascaraCpf = null;
		MaskFormatter mascaraData = null;
		
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        NumberFormatter currencyFormatter = new NumberFormatter(currencyFormat);
        currencyFormatter.setAllowsInvalid(false); // Do not allow invalid input

		try {
			mascaraCep = new MaskFormatter("#####-###");
			mascaraTel = new MaskFormatter("(##)####-####");
			mascaraCpf = new MaskFormatter("#########-##");
			mascaraData = new MaskFormatter("##/##/####");
			mascaraCep.setPlaceholderCharacter('_');
			mascaraTel.setPlaceholderCharacter('_');
			mascaraCpf.setPlaceholderCharacter('_');
			mascaraData.setPlaceholderCharacter('_');
		} catch (ParseException excp) {
			System.err.println("Erro na formatação: " + excp.getMessage());
			System.exit(-1);
		}

		// Seta as máscaras nos objetos JFormattedTextField
		JFormattedTextField jFormattedTextCep = new JFormattedTextField(mascaraCep);
		JFormattedTextField jFormattedTextTel = new JFormattedTextField(mascaraTel);
		JFormattedTextField jFormattedTextCpf = new JFormattedTextField(mascaraCpf);
		JFormattedTextField jFormattedTextData = new JFormattedTextField(mascaraData);
		JFormattedTextField jFormattedTextPreco = new JFormattedTextField(currencyFormatter);
		jFormattedTextPreco.setValue(0.00); // Setting a Double value
		
		jFormattedTextCep.setBounds(150, 40, 100, 20);
		jFormattedTextTel.setBounds(150, 80, 100, 20);
		jFormattedTextCpf.setBounds(150, 120, 100, 20);
		jFormattedTextData.setBounds(150, 160, 100, 20);
		jFormattedTextPreco.setBounds(150, 200, 100, 20);

		// Adiciona os rótulos e os campos de textos com máscaras na tela
		janela.add(labelCep);
		janela.add(labelTel);
		janela.add(labelCpf);
		janela.add(labelData);
		janela.add(labelPreco);
		janela.add(jFormattedTextCep);
		janela.add(jFormattedTextTel);
		janela.add(jFormattedTextCpf);
		janela.add(jFormattedTextData);
		janela.add(jFormattedTextPreco);
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
