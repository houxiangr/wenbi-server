package com.houxiang.wenbiserver.stateEnum;

/**
 * 注册时返回的几种状态
 */
public enum RegisterEnum {
    INPUTEMPTY("输入不能为空"),
    SUCCESS("注册成功"),
    NAMETOOLONG("昵称长度不能超过二十"),
    PHONEFORMATWRONG("该手机号格式不对"),
    PHONEISEXITS("该手机号已被注册"),
    ERROR("注册失败");

    private final String name;

    RegisterEnum(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
