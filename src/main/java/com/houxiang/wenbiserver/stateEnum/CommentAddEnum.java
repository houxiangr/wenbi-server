package com.houxiang.wenbiserver.stateEnum;

public enum CommentAddEnum {
    SUCCESS("添加成功"),
    INPUTEMPTY("输入为空"),
    NOTLOGIN("鉴权失败"),
    ERROR("添加失败");

    private final String name;

    CommentAddEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
