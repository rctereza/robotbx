package com.rctereza.robotbx.enums;

public enum Command {
	WAIT(1),
    MOVE(2),
    CLICK(3),
    PASTE(4),
    TYPE(5),
    TAB(6);

    private int type;

    Command (int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
