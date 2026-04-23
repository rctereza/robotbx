package com.rctereza.robotbx.enums;

public enum SpedSearchField {
	DATA_INICIO("Data de Início"),
	DATA_FIM("Data de Fim"),
	CNPJ_INCORPORADORA("CNPJ da Incorporadora"),
	CNPJ_ESTABELECIMENTO("CNPJ do Estabelecimento"),
	INSCRICAO_ESTADUAL("Inscrição Estadual"),
	TIPO_EVENTO("Tipo de Evento"),
	BAIXAR_ARQUIVO_ASSINADO("Arquivo com Assinatura"),
	BUSCAR_TODOS_ESTABLECIMENTOS("Todos os Estabelecimentos"),
	ULTIMO_ARQUIVO_TRANSMITIDO("Ultimo Arquivo Transmitido");

    private String value;

    SpedSearchField (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
}
