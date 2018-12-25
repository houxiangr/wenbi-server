package com.houxiang.wenbiserver.dto;

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
