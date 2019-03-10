package com.houxiang.wenbiserver.stateEnum;

public enum CommentEnum {
    SUCCESS("成功"),
    FAIL("失败");

    private final String name;

    CommentEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
