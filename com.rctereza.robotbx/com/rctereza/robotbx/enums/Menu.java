package com.rctereza.robotbx.enums;

public enum Menu {
	HISTORIC("Histórico"),
	SETTING("Configuração"),
	ABOUT("Sobre"),
	CLOSE("Fechar"),
	DONE("Feito"),
	MINIMIZE("Minimizar");

    private String value;

    Menu (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
