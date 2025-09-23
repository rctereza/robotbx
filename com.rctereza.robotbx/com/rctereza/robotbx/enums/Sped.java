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
    
    public static Sped getSped(String value) {
    	
    	Sped result = null;
    	
    	if (value.equals(CONTRIBUICOES.getValue())) {
    		result = Sped.CONTRIBUICOES;
    	}
    	else if (value.equals(CONTABIL.getValue())) {
    		result = Sped.CONTABIL;
    	}
    	else if (value.equals(ECF.getValue())) {
    		result = Sped.ECF;
    	}
    	else if (value.equals(EFD.getValue())) {
    		result = Sped.EFD;
    	}
    	else if (value.equals(FISCAL.getValue())) {
    		result = Sped.FISCAL;
    	}
    	
    	return result;
    }
}
