package com.houxiang.wenbiserver.stateEnum;

public enum EssayState {
    EXAMING((short)1),
    SHOW((short)2);

    private final short name;

    EssayState(short name) {
        this.name = name;
    }

    public short getName() {
        return name;
    }
}
