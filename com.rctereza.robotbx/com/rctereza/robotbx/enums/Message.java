package com.rctereza.robotbx.enums;

public enum Message {
	NONE(0),
	ERROR(1),
	WARNING(2),
	WAITING(3),
	VALIDATION(4),
    CONFIRMATION(5);

    private int type;

    Message (int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
