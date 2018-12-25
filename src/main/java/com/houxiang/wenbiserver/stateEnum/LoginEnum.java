package com.houxiang.wenbiserver.stateEnum;

public enum LoginEnum {
    SUCCESS("登陆成功，马上跳转到主页"),
    INPUTEMPTY("输入不能为空"),
    PHONEFORMATWRONG("手机号码格式不符合"),
    ERROR("登陆失败");

    private final String name;

    LoginEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
