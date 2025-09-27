package com.rctereza.robotbx.tools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import com.rctereza.robotbx.Constants;
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

	public static JPanel getSearchFields(Sped value, String fileType, String searchType) throws ParseException {
		JPanel panel = null; // = new JPanel(new MigLayout("", "[][]", "[]"));

		MaskFormatter cnpjMask = new MaskFormatter("##.###.###/####-##");
		MaskFormatter dateMask = new MaskFormatter("##/##/####");

		cnpjMask.setPlaceholderCharacter('_');
		dateMask.setPlaceholderCharacter('_');
		
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
			JFormattedTextField inicioDateTextField = new JFormattedTextField(dateMask);
			inicioDateTextField.setName("DATA_INICIO");
			inicioDateTextField.setColumns(8);
			inicioDateTextField.setValue(Constants.PROGRAM_PERIOD_START);

			JLabel fimDateLabel = new JLabel("Data de fim *");
			JFormattedTextField fimDateTextField = new JFormattedTextField(dateMask);
			fimDateTextField.setName("DATA_FIM");
			fimDateTextField.setColumns(8);
			fimDateTextField.setValue(Constants.PROGRAM_PERIOD_END);
			
			panel.add(inicioDateLabel, "sg 1");
			panel.add(inicioDateTextField, "wrap");
			panel.add(fimDateLabel, "sg 1");
			panel.add(fimDateTextField, "wrap");
			
		}
		else if ((value.equals(Sped.CONTRIBUICOES) && fileType.equals("Escrituração") && searchType.equals("Período de Entrega da Incorporada"))
				|| (value.equals(Sped.CONTRIBUICOES) && fileType.equals("Escrituração") && searchType.equals("Período da Escrituração da Incorporada"))) {
			
			panel = new JPanel(new MigLayout("", "[][]", "[][][]"));

			JLabel inicioDateLabel = new JLabel("Data de início *");
			JFormattedTextField inicioDateTextField = new JFormattedTextField(dateMask);
			inicioDateTextField.setName("DATA_INICIO");
			inicioDateTextField.setColumns(8);
			inicioDateTextField.setValue(Constants.PROGRAM_PERIOD_START);
			
			JLabel fimDateLabel = new JLabel("Data de fim *");
			JFormattedTextField fimDateTextField = new JFormattedTextField(dateMask);
			fimDateTextField.setName("DATA_FIM");
			fimDateTextField.setColumns(8);
			fimDateTextField.setValue(Constants.PROGRAM_PERIOD_END);
			
			JLabel incorporadoraCnpjLabel = new JLabel("CNPJ da Incorporada *");
			JFormattedTextField incorporadoraCnpjTextField = new JFormattedTextField(cnpjMask);	
			incorporadoraCnpjTextField.setName("CNPJ_INCORPORADORA");
			incorporadoraCnpjTextField.setColumns(12);
			
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
			eventoTypeComboBox.setName("TIPO_EVENTO");
			
			JLabel baixarFileLabel = new JLabel("Baixar arquivo com Assinatura Digital *");
			String[] baixarFileOptions = {"NÃO","SIM"};
			JComboBox<String> baixarFileComboBox = new JComboBox<String>(baixarFileOptions);
			baixarFileComboBox.setName("BAIXAR_ARQUIVO_ASSINADO");
			
			panel.add(eventoTypeLabel, "sg 1");
			panel.add(eventoTypeComboBox, "wrap");
			panel.add(baixarFileLabel, "sg 1");
			panel.add(baixarFileComboBox, "wrap");
		}
		else if (value.equals(Sped.FISCAL) && fileType.equals("Escrituração Fiscal Digital") && searchType.equals("Por Período da Escrituração")) {
			panel = new JPanel(new MigLayout("", "[][]", "[][][][][][]"));
			
			JLabel cnpjEstabelecimentoLabel = new JLabel("CNPJ do Estabelecimento");
			JFormattedTextField cnpjEstabelecimentoTextField = new JFormattedTextField(cnpjMask);
			cnpjEstabelecimentoTextField.setName("CNPJ_ESTABELECIMENTO");
			cnpjEstabelecimentoTextField.setColumns(12);
			
			JLabel buscarArquivosLabel = new JLabel("Buscar Arquivos de Todos os Estabelecimentos *");
			JCheckBox buscarArquivosCheckBox = new JCheckBox("");
			buscarArquivosCheckBox.setName("BUSCAR_TODOS_ESTABLECIMENTOS");
			
			JLabel incricaoEstadualLabel = new JLabel("Inscrição Estadual");
			JTextField incricaoEstadualTextField = new JTextField(10);
			incricaoEstadualTextField.setName("INSCRICAO_ESTADUAL");
			
			JLabel inicioDateLabel = new JLabel("Data de início *");
			JFormattedTextField inicioDateTextField = new JFormattedTextField(dateMask);
			inicioDateTextField.setName("DATA_INICIO");
			inicioDateTextField.setColumns(8);
			inicioDateTextField.setValue(Constants.PROGRAM_PERIOD_START);
			
			JLabel fimDateLabel = new JLabel("Data de fim *");
			JFormattedTextField fimDateTextField = new JFormattedTextField(dateMask);
			fimDateTextField.setName("DATA_FIM");
			fimDateTextField.setColumns(8);
			fimDateTextField.setValue(Constants.PROGRAM_PERIOD_END);
			
			JLabel ultimoArquivoLabel = new JLabel("Ultimo arquivo transmitido *");
			JCheckBox ultimoArquivoCheckBox = new JCheckBox("");
			incricaoEstadualTextField.setName("ULTIMO_ARQUIVO_TRANSMITIDO");

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
