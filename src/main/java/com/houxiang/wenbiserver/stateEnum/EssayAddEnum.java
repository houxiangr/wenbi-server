package com.houxiang.wenbiserver.stateEnum;

public enum EssayAddEnum {
    SUCCESS("添加成功"),
    INPUTEMPTY("输入为空"),
    TITLETOOLONG("标题长度大于二十"),
    NOTLOGIN("鉴权失败"),
    ESSAYEXIST("你当前已经有这个标题的作品了，请更换标题"),
    ERROR("添加失败");

    private final String name;

    EssayAddEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
