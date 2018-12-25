package com.houxiang.wenbiserver.stateEnum;

public enum  IsLoginEnum {
    LOGINED(1),
    NOTLOGIN(-1);

    private final int name;

    IsLoginEnum(int name) {
        this.name = name;
    }

    public int getName() {
        return name;
    }
}
