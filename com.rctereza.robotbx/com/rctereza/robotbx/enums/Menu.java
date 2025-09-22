package com.rctereza.robotbx.enums;

public enum Menu {
	CLOSE("Close"),
	MINIMIZE("Minimize");

    private String value;

    Menu (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
