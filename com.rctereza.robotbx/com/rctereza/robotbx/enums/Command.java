package com.rctereza.robotbx.enums;

public enum Command {
	NONE(0),
	WAIT(1),
    MOVE(2),
    CLICK(3),
    PASTE(4),
    TYPE(5),
    TYPE_ONLY_NUMBERS(6),
    TAB(7),
    ENTER(8),
    SPACEBAR(9),
    ALT_ARROW_DOWN(10),
    ARROW_DOWN(11),
    ARROW_UP(12);

    private int type;

    Command (int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
