package com.rctereza.robotbx.models;

import java.io.Serializable;

public record ReceitaBx(  
	  String SCREEN
	, Certificate CERTIFICADO
	, String PERFIL
	, String PERFIL_TYPE
	, String PERFIL_VALUE
	, String SISTEMA
	, String TIPO_ARQUIVO
	, String TIPO_PESQUISA
	, String DATA_INICIO
	, String DATA_FIM
	, String CNPJ_INCORPORADORA
	, String TIPO_EVENTO
	, String BAIXAR_ARQUIVO_ASSINADO
	, String CNPJ_ESTABELECIMENTO
	, Boolean BUSCAR_TODOS_ESTABLECIMENTOS
	, String INSCRICAO_ESTADUAL
	, Boolean ULTIMO_ARQUIVO_TRANSMITIDO
	, String ULTIMO_PEDIDO_SOLICITADO
	, String DATA_HORA_CONCLUSAO_PROCESSAMENTO
) implements Serializable {
	private static final long serialVersionUID = 1L;
	public ReceitaBx() {
		this(null,new Certificate(),null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
	}
}