package com.houxiang.wenbiserver.dto;

//普通信息格式，结果加信息中间数据结构
public class CommonMessage {
    private boolean state;
    private String msg;

    public CommonMessage(boolean state,String msg) {
        this.state = state;
        this.msg = msg;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
