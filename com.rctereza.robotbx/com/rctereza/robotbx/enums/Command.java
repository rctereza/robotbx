package com.rctereza.robotbx.enums;

public enum Command {
	NONE(0),
	WAIT(1),
    MOVE(2),
    CLICK(3),
    PASTE(4),
    TYPE(5),
    TAB(6),
    ENTER(7),
    SPACEBAR(8),
    ALTARROWDOWN(9),
    ARROWDOWN(10),
    ARROWUP(11);

    private int type;

    Command (int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
