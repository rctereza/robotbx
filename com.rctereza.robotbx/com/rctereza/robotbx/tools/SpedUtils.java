package com.rctereza.robotbx.tools;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.rctereza.robotbx.enums.Sped;

import net.miginfocom.swing.MigLayout;

public class SpedUtils {

	public static String[] getSystemList() {
		return Arrays.stream(Sped.values()).map(Sped::getValue).toArray(String[]::new);
	}

	public static String[] getSystemFileType(Sped value) {
		ArrayList<String> list = getFileType(value);
		return list.toArray(new String[0]);
	}

	public static ArrayList<String> getSystemFileTypeList(Sped value) {
		return getFileType(value);
	}

	private static ArrayList<String> getFileType(Sped value) {
		ArrayList<String> list = new ArrayList<>();
		if (value.equals(Sped.CONTRIBUICOES)) {
			list.add("Escrituração");
		} else if (value.equals(Sped.CONTABIL)) {
			list.add("Escrituração Contábil Digital");
			list.add("Dados Agregados de Escrituração Contábil Digital");
			list.add("Por Período da Escrituração");
		} else if (value.equals(Sped.ECF)) {
			list.add("Escrituração");
		} else if (value.equals(Sped.EFD)) {
			list.add("Eventos de Tabelas");
		} else if (value.equals(Sped.FISCAL)) {
			list.add("Escrituração Fiscal Digital");
		}
		return list;
	}

	public static String[] getSystemSearchType(Sped value, String fileType) {
		ArrayList<String> list = getSearchType(value, fileType);
		return list.toArray(new String[0]);
	}

	public static ArrayList<String> getSystemSearchTypeList(Sped value, String fileType) {
		return getSearchType(value, fileType);
	}

	private static ArrayList<String> getSearchType(Sped value, String fileType) {
		ArrayList<String> list = new ArrayList<>();

		if (value.equals(Sped.CONTRIBUICOES) && (fileType.equals("Escrituração") || fileType.equals(""))) {
			list.add("Período de Entrega");
			list.add("Período de Entrega da Incorporada");
			list.add("Período da Escrituração");
			list.add("Período da Escrituração da Incorporada");
		} else if (value.equals(Sped.CONTABIL)
				&& (fileType.equals("Escrituração Contábil Digital") || fileType.equals(""))) {
			list.add("Por Período da Escrituração");
		} else if (value.equals(Sped.CONTABIL)
				&& (fileType.equals("Dados Agregados de Escrituração Contábil Digital") || fileType.equals(""))) {
			list.add("Por Período da Escrituração");
		} else if (value.equals(Sped.CONTABIL)
				&& (fileType.equals("Por Período da Escrituração") || fileType.equals(""))) {
			list.add("Por Período da Escrituração");
		} else if (value.equals(Sped.ECF) && (fileType.equals("Escrituração") || fileType.equals(""))) {
			list.add("Período de Entrega");
			list.add("Período da Escrituração");
		} else if (value.equals(Sped.EFD) && (fileType.equals("Eventos de Tabelas") || fileType.equals(""))) {
			list.add("Baixar Eventos de Tabelas");
		} else if (value.equals(Sped.FISCAL)
				&& (fileType.equals("Escrituração Fiscal Digital") || fileType.equals(""))) {
			list.add("Por Período da Escrituração");
		}

		return list;
	}

	public static JPanel getSearchFields(Sped value, String fileType, String searchType) {
		JPanel panel = null; // = new JPanel(new MigLayout("", "[][]", "[]"));

		if ((value.equals(Sped.CONTRIBUICOES) && (fileType.equals("Escrituração") || fileType.equals("")) && (searchType.equals("Período de Entrega") || searchType.equals("")))
				|| (value.equals(Sped.CONTRIBUICOES) && fileType.equals("Escrituração") && searchType.equals("Período da Escrituração"))
				|| (value.equals(Sped.CONTABIL) && fileType.equals("Escrituração Contábil Digital") && searchType.equals("Por Período da Escrituração"))
				|| (value.equals(Sped.CONTABIL) && fileType.equals("Dados Agregados de Escrituração Contábil Digital") && searchType.equals("Por Período da Escrituração"))
				|| (value.equals(Sped.CONTABIL) && fileType.equals("Por Período da Escrituração") && searchType.equals("Por Período da Escrituração"))
				|| (value.equals(Sped.ECF) && fileType.equals("Escrituração") && searchType.equals("Período de Entrega"))
				|| (value.equals(Sped.ECF) && fileType.equals("Escrituração") && searchType.equals("Período da Escrituração"))
				) {
			
			panel = new JPanel(new MigLayout("", "[][]", "[][]"));

			JLabel inicioDateLabel = new JLabel("Data de início *");
			JTextField inicioDateTextField = new JTextField(10);

			JLabel fimDateLabel = new JLabel("Data de fim *");
			JTextField fimDateTextField = new JTextField(10);
			
			panel.add(inicioDateLabel, "sg 1");
			panel.add(inicioDateTextField, "wrap");
			panel.add(fimDateLabel, "sg 1");
			panel.add(fimDateTextField, "wrap");
			
		}
		else if ((value.equals(Sped.CONTRIBUICOES) && fileType.equals("Escrituração") && searchType.equals("Período de Entrega da Incorporada"))
				|| (value.equals(Sped.CONTRIBUICOES) && fileType.equals("Escrituração") && searchType.equals("Período da Escrituração da Incorporada"))) {
			
			panel = new JPanel(new MigLayout("", "[][]", "[][][]"));

			JLabel inicioDateLabel = new JLabel("Data de início *");
			JTextField inicioDateTextField = new JTextField(10);

			JLabel fimDateLabel = new JLabel("Data de fim *");
			JTextField fimDateTextField = new JTextField(10);
			
			JLabel incorporadoraCnpjLabel = new JLabel("CNPJ da Incorporada *");
			JTextField incorporadoraCnpjTextField = new JTextField(20);	
			
			panel.add(inicioDateLabel, "sg 1");
			panel.add(inicioDateTextField, "wrap");
			panel.add(fimDateLabel, "sg 1");
			panel.add(fimDateTextField, "wrap");
			panel.add(incorporadoraCnpjLabel, "sg 1");
			panel.add(incorporadoraCnpjTextField, "wrap");
		}
		else if (value.equals(Sped.EFD) && fileType.equals("Eventos de Tabelas") && searchType.equals("Baixar Eventos de Tabelas")) {
			panel = new JPanel(new MigLayout("", "[][]", "[][]"));
			
			JLabel eventoTypeLabel = new JLabel("Tipo de Evento *");
			String[] eventoTypes = { "Evento R-1000 Informações do Contribuinte", "Evento R-1050 Tabela de Entidades Ligadas", "Evento R-1070 Tabela de Processos Administrativos/Judiciais"};
			JComboBox<String> eventoTypeComboBox = new JComboBox<String>(eventoTypes);
			
			JLabel baixarFileLabel = new JLabel("Baixar arquivo com Assinatura Digital *");
			String[] baixarFileOptions = {"NÃO","SIM"};
			JComboBox<String> baixarFileComboBox = new JComboBox<String>(baixarFileOptions);
			
			panel.add(eventoTypeLabel, "sg 1");
			panel.add(eventoTypeComboBox, "wrap");
			panel.add(baixarFileLabel, "sg 1");
			panel.add(baixarFileComboBox, "wrap");
		}
		else if (value.equals(Sped.FISCAL) && fileType.equals("Escrituração Fiscal Digital") && searchType.equals("Por Período da Escrituração")) {
			panel = new JPanel(new MigLayout("", "[][]", "[][][][][][]"));
			
			JLabel cnpjEstabelecimentoLabel = new JLabel("CNPJ do Estabelecimento");
			JTextField cnpjEstabelecimentoTextField = new JTextField(20);
			
			JLabel buscarArquivosLabel = new JLabel("Buscar Arquivos de Todos os Estabelecimentos *");
			JCheckBox buscarArquivosCheckBox = new JCheckBox("");
			
			JLabel incricaoEstadualLabel = new JLabel("Inscrição Estadual");
			JTextField incricaoEstadualTextField = new JTextField(15);
			
			JLabel inicioDateLabel = new JLabel("Data de início *");
			JTextField inicioDateTextField = new JTextField(10);

			JLabel fimDateLabel = new JLabel("Data de fim *");
			JTextField fimDateTextField = new JTextField(10);
			
			JLabel ultimoArquivoLabel = new JLabel("Ultimo arquivo transmitido *");
			JCheckBox ultimoArquivoCheckBox = new JCheckBox("");

			panel.add(cnpjEstabelecimentoLabel, "sg 1");
			panel.add(cnpjEstabelecimentoTextField, "wrap");
			panel.add(buscarArquivosLabel, "sg 1");
			panel.add(buscarArquivosCheckBox, "wrap");
			panel.add(incricaoEstadualLabel, "sg 1");
			panel.add(incricaoEstadualTextField, "wrap");
			panel.add(inicioDateLabel, "sg 1");
			panel.add(inicioDateTextField, "wrap");
			panel.add(fimDateLabel, "sg 1");
			panel.add(fimDateTextField, "wrap");
			panel.add(ultimoArquivoLabel, "sg 1");
			panel.add(ultimoArquivoCheckBox, "wrap");

		}
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Preencha os campos para refinar a busca (os campos marcados com * são obrigatórios)");
		panel.setBorder(title);

		return panel;
	}
}
