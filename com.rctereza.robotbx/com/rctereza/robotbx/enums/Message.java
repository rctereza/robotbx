package com.rctereza.robotbx.enums;

public enum Message {
	ERROR(0),
	WARNING(1),
	WAITING(2),
	VALIDATION(3),
    CONFIRMATION(4);

    private int type;

    Message (int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
