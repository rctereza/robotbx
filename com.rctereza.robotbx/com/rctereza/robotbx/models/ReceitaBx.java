package com.rctereza.robotbx.models;

import java.io.Serializable;

import com.rctereza.robotbx.enums.Status;
import com.rctereza.robotbx.interfaces.Wrappable;

public record ReceitaBx(String RESOLUCAO_TELA, Setting CONFIGURACAO, Certificate CERTIFICADO, Procurator PROCURADOR,
		String PERFIL, String PERFIL_TYPE, String PERFIL_VALUE, String SISTEMA, String TIPO_ARQUIVO,
		String TIPO_PESQUISA, String DATA_INICIO, String DATA_FIM, String CNPJ_INCORPORADORA, String TIPO_EVENTO,
		String BAIXAR_ARQUIVO_ASSINADO, String CNPJ_ESTABELECIMENTO, Boolean BUSCAR_TODOS_ESTABLECIMENTOS,
		String INSCRICAO_ESTADUAL, Boolean ULTIMO_ARQUIVO_TRANSMITIDO, String ULTIMO_PEDIDO_SOLICITADO,
		String DATA_HORA_CONCLUSAO_PROCESSAMENTO, String MENSAGEM_CONCLUSAO_PROCESSAMENTO, String PERIODOS_FALTANDO,
		Integer TOTAL_PERIODOS_FALTANDO, Status STATUS) implements Serializable, Wrappable {

	public ReceitaBx() {
		this(null, new Setting(), new Certificate(), null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null);
	}

	@Override
	public Integer getObjectId() {
		return null;
	}

}