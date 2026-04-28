package com.rctereza.robotbx.enums;

public enum Status {
	SUCCESS("Sucesso"), //"Processed successfully"
	ERROR("Erro"), //"Processing failed"
    WARNING("Atenção"), //"Processed with warnings"
    PENDING("Pendente"); //"Not processed yet")

    private String value;

    Status (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public Status getStatus(String value) {
    	
    	Status result = null;
    	
    	if (value.equals(SUCCESS.getValue())) {
    		result = Status.SUCCESS;
    	}
    	else if (value.equals(ERROR.getValue())) {
    		result = Status.ERROR;
    	}
    	else if (value.equals(WARNING.getValue())) {
    		result = Status.WARNING;
    	}
    	else if (value.equals(PENDING.getValue())) {
    		result = Status.PENDING;
    	}
    	
    	return result;
    }
}
