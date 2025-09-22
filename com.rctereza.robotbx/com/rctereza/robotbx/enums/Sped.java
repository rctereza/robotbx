package com.rctereza.robotbx.enums;

public enum Sped {
	CONTRIBUICOES("SPED Contribuições"),
	CONTABIL("SPED Contabil"),
	ECF("SPED ECF"),
	EFD("SPED EFD-Reinf"),
	FISCAL("SPED Fiscal-EFD ICMS IPI");

    private String value;

    Sped (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
