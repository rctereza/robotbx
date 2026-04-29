package com.rctereza.robotbx.enums;

public enum Menu {
	NEW("Novo"),
	DETAILS("Detalhes"),
	HISTORIC("Histórico"),
	SETTING("Configuração"),
	ABOUT("Sobre"),
	CLOSE("Fechar"),
	DONE("Feito"),
	MINIMIZE("Minimizar"),
	RESTART("Reiniciar"),
	EXIT("Sair");

    private String value;

    Menu (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
