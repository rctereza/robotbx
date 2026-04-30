package com.rctereza.robotbx.tools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.enums.Sped;
import com.rctereza.robotbx.enums.SpedSearchField;
import com.rctereza.robotbx.models.ReceitaBx;

import net.miginfocom.swing.MigLayout;

public class SpedUtils {

	private static final Logger logger = LoggerFactory.getLogger(SpedUtils.class);
	
	public static String[] contabilFileTypes = { "Escrituração Contábil Digital",
			"Dados Agregados de Escrituração Contábil Digital", "Termos Emitidios pelas Juntas Comerciais" };

	public static String[] contribuicoesSearchTypes = { "Período de Entrega", "Período de Entrega da Incorporada",
			"Período da Escrituração", "Período da Escrituração da Incorporada" };
	
	public static String[] ecfSearchTypes = { "Período de Entrega", "Período da Escrituração" } ;

	public static String[] efdEventTypes = { "Evento R-1000 Informações do Contribuinte",
			"Evento R-1050 Tabela de Entidades Ligadas",
			"Evento R-1070 Tabela de Processos Administrativos/Judiciais" };

	public static String[] efdDownloadSignedFiles = { "NÃO", "SIM" };

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
		
		if (value.equals(Sped.CONTRIBUICOES) || value.equals(Sped.ECF)) {
			list.add("Escrituração");
			
		} else if (value.equals(Sped.CONTABIL)) {
			Collections.addAll(list, contabilFileTypes);
			
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
			Collections.addAll(list, contribuicoesSearchTypes);
			
		} else if (value.equals(Sped.CONTABIL) && (Arrays.asList(contabilFileTypes).contains(fileType) || fileType.equals(""))) {
			list.add("Por Período da Escrituração");
			
		} else if (value.equals(Sped.ECF) && (fileType.equals("Escrituração") || fileType.equals(""))) {
			Collections.addAll(list, ecfSearchTypes);
			
		} else if (value.equals(Sped.EFD) && (fileType.equals("Eventos de Tabelas") || fileType.equals(""))) {
			list.add("Baixar Eventos de Tabelas");
			
		} else if (value.equals(Sped.FISCAL) && (fileType.equals("Escrituração Fiscal Digital") || fileType.equals(""))) {
			list.add("Por Período da Escrituração");
		}

		return list;
	}

	public static JPanel getSearchFields(Sped value, String fileType, String searchType, ReceitaBx receitaBx) {
		
		String DATA_INICIO = "";
		String DATA_FIM = "";
		String CNPJ_INCORPORADORA = "";
		String TIPO_EVENTO = "";
		String BAIXAR_ARQUIVO_ASSINADO = "";
		String CNPJ_ESTABELECIMENTO = "";
		Boolean BUSCAR_TODOS_ESTABLECIMENTOS = false;
		String INSCRICAO_ESTADUAL = "";
		Boolean ULTIMO_ARQUIVO_TRANSMITIDO = false;
		
		if (receitaBx != null) {
			DATA_INICIO = receitaBx.DATA_INICIO();
			DATA_FIM = receitaBx.DATA_FIM();
			CNPJ_INCORPORADORA = receitaBx.CNPJ_INCORPORADORA();
			TIPO_EVENTO = receitaBx.TIPO_EVENTO();
			BAIXAR_ARQUIVO_ASSINADO = receitaBx.BAIXAR_ARQUIVO_ASSINADO();
			CNPJ_ESTABELECIMENTO = receitaBx.CNPJ_ESTABELECIMENTO();
			BUSCAR_TODOS_ESTABLECIMENTOS = receitaBx.BUSCAR_TODOS_ESTABLECIMENTOS();
			INSCRICAO_ESTADUAL = receitaBx.INSCRICAO_ESTADUAL();
			ULTIMO_ARQUIVO_TRANSMITIDO = receitaBx.ULTIMO_ARQUIVO_TRANSMITIDO();
		}
			
		JPanel panel = null; // = new JPanel(new MigLayout("", "[][]", "[]"));

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

		if ((value.equals(Sped.CONTRIBUICOES) && (fileType.equals("Escrituração") || fileType.equals(""))
				&& (searchType.equals("Período de Entrega") || searchType.equals("")))
				
				|| (value.equals(Sped.CONTRIBUICOES) && fileType.equals("Escrituração")
						&& searchType.equals("Período da Escrituração"))
				
				|| (value.equals(Sped.CONTABIL) && Arrays.asList(contabilFileTypes).contains(fileType) 
						&& searchType.equals("Por Período da Escrituração"))
				
				|| (value.equals(Sped.ECF) && fileType.equals("Escrituração")
						&& Arrays.asList(ecfSearchTypes).contains(searchType))) {

			panel = new JPanel(new MigLayout("", "[][]", "[][]"));

			JLabel inicioDateLabel = new JLabel("Data de início *");
			JFormattedTextField inicioDateTextField = new JFormattedTextField(dateMask);
			inicioDateTextField.setName(SpedSearchField.DATA_INICIO.getValue());
			inicioDateTextField.setColumns(8);
			inicioDateTextField.setValue(DATA_INICIO);

			JLabel fimDateLabel = new JLabel("Data de fim *");
			JFormattedTextField fimDateTextField = new JFormattedTextField(dateMask);
			fimDateTextField.setName(SpedSearchField.DATA_FIM.getValue());
			fimDateTextField.setColumns(8);
			fimDateTextField.setValue(DATA_FIM);

			panel.add(inicioDateLabel, "sg 1");
			panel.add(inicioDateTextField, "wrap");
			panel.add(fimDateLabel, "sg 1");
			panel.add(fimDateTextField, "wrap");

		} else if ((value.equals(Sped.CONTRIBUICOES) && fileType.equals("Escrituração")
				&& searchType.equals("Período de Entrega da Incorporada"))
				
				|| (value.equals(Sped.CONTRIBUICOES) && fileType.equals("Escrituração")
						&& searchType.equals("Período da Escrituração da Incorporada"))) {

			panel = new JPanel(new MigLayout("", "[][]", "[][][]"));

			JLabel inicioDateLabel = new JLabel("Data de início *");
			JFormattedTextField inicioDateTextField = new JFormattedTextField(dateMask);
			inicioDateTextField.setName(SpedSearchField.DATA_INICIO.getValue());
			inicioDateTextField.setColumns(8);
			inicioDateTextField.setValue(DATA_INICIO);

			JLabel fimDateLabel = new JLabel("Data de fim *");
			JFormattedTextField fimDateTextField = new JFormattedTextField(dateMask);
			fimDateTextField.setName(SpedSearchField.DATA_FIM.getValue());
			fimDateTextField.setColumns(8);
			fimDateTextField.setValue(DATA_FIM);

			JLabel incorporadoraCnpjLabel = new JLabel("CNPJ da Incorporada *");
			JFormattedTextField incorporadoraCnpjTextField = new JFormattedTextField(cnpjMask);
			incorporadoraCnpjTextField.setName(SpedSearchField.CNPJ_INCORPORADORA.getValue());
			incorporadoraCnpjTextField.setColumns(12);
			incorporadoraCnpjTextField.setValue(CNPJ_INCORPORADORA);

			panel.add(inicioDateLabel, "sg 1");
			panel.add(inicioDateTextField, "wrap");
			panel.add(fimDateLabel, "sg 1");
			panel.add(fimDateTextField, "wrap");
			panel.add(incorporadoraCnpjLabel, "sg 1");
			panel.add(incorporadoraCnpjTextField, "wrap");
			
		} else if (value.equals(Sped.EFD) && fileType.equals("Eventos de Tabelas")
				&& searchType.equals("Baixar Eventos de Tabelas")) {
			
			panel = new JPanel(new MigLayout("", "[][]", "[][]"));

			JLabel eventoTypeLabel = new JLabel("Tipo de Evento *");
			// String[] eventoTypes = { "Evento R-1000 Informações do Contribuinte", "Evento
			// R-1050 Tabela de Entidades Ligadas", "Evento R-1070 Tabela de Processos
			// Administrativos/Judiciais"};
			JComboBox<String> eventoTypeComboBox = new JComboBox<String>(efdEventTypes);
			eventoTypeComboBox.setName(SpedSearchField.TIPO_EVENTO.getValue());
			eventoTypeComboBox.setSelectedItem(TIPO_EVENTO);

			JLabel baixarFileLabel = new JLabel("Baixar arquivo com Assinatura Digital *");
			// String[] baixarFileOptions = {"NÃO","SIM"};
			JComboBox<String> baixarFileComboBox = new JComboBox<String>(efdDownloadSignedFiles);
			baixarFileComboBox.setName(SpedSearchField.BAIXAR_ARQUIVO_ASSINADO.getValue());
			baixarFileComboBox.setSelectedItem(BAIXAR_ARQUIVO_ASSINADO);

			panel.add(eventoTypeLabel, "sg 1");
			panel.add(eventoTypeComboBox, "wrap");
			panel.add(baixarFileLabel, "sg 1");
			panel.add(baixarFileComboBox, "wrap");
			
		} else if (value.equals(Sped.FISCAL) && fileType.equals("Escrituração Fiscal Digital")
				&& searchType.equals("Por Período da Escrituração")) {
			
			panel = new JPanel(new MigLayout("", "[][]", "[][][][][][]"));

			JLabel cnpjEstabelecimentoLabel = new JLabel("CNPJ do Estabelecimento");
			JFormattedTextField cnpjEstabelecimentoTextField = new JFormattedTextField(cnpjMask);
			cnpjEstabelecimentoTextField.setName(SpedSearchField.CNPJ_ESTABELECIMENTO.getValue());
			cnpjEstabelecimentoTextField.setColumns(12);
			cnpjEstabelecimentoTextField.setValue(CNPJ_ESTABELECIMENTO);

			JLabel buscarArquivosLabel = new JLabel("Buscar Arquivos de Todos os Estabelecimentos *");
			JCheckBox buscarArquivosCheckBox = new JCheckBox("");
			buscarArquivosCheckBox.setName(SpedSearchField.BUSCAR_TODOS_ESTABLECIMENTOS.getValue());
			buscarArquivosCheckBox.setSelected(BUSCAR_TODOS_ESTABLECIMENTOS);

			JLabel incricaoEstadualLabel = new JLabel("Inscrição Estadual");
			JTextField incricaoEstadualTextField = new JTextField(10);
			incricaoEstadualTextField.setName(SpedSearchField.INSCRICAO_ESTADUAL.getValue());
			incricaoEstadualTextField.setText(INSCRICAO_ESTADUAL);

			JLabel inicioDateLabel = new JLabel("Data de início *");
			JFormattedTextField inicioDateTextField = new JFormattedTextField(dateMask);
			inicioDateTextField.setName(SpedSearchField.DATA_INICIO.getValue());
			inicioDateTextField.setColumns(8);
			inicioDateTextField.setValue(DATA_INICIO);

			JLabel fimDateLabel = new JLabel("Data de fim *");
			JFormattedTextField fimDateTextField = new JFormattedTextField(dateMask);
			fimDateTextField.setName(SpedSearchField.DATA_FIM.getValue());
			fimDateTextField.setColumns(8);
			fimDateTextField.setValue(DATA_FIM);

			JLabel ultimoArquivoLabel = new JLabel("Ultimo arquivo transmitido *");
			JCheckBox ultimoArquivoCheckBox = new JCheckBox("");
			ultimoArquivoCheckBox.setName(SpedSearchField.ULTIMO_ARQUIVO_TRANSMITIDO.getValue());
			ultimoArquivoCheckBox.setSelected(ULTIMO_ARQUIVO_TRANSMITIDO);

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
		title = BorderFactory.createTitledBorder(
				"Preencha os campos para refinar a busca (os campos marcados com * são obrigatórios)");
		panel.setBorder(title);

		return panel;
	}
	
//	private String getInicioDate() {
//		String result = Constants.PROGRAM_PERIOD_START;
//		
//		return result;
//	}
}
