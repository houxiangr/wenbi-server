package com.houxiang.wenbiserver.dto;

import com.houxiang.wenbiserver.model.Essay;

import java.util.List;

public class EssaysDto {
    private List<Essay> essays;
    private String msg;
    private Boolean state;

    public EssaysDto(String msg, Boolean state) {
        this.msg = msg;
        this.state = state;
    }

    public EssaysDto(List<Essay> essays, String msg, Boolean state) {
        this.essays = essays;
        this.msg = msg;
        this.state = state;
    }

    public List<Essay> getEssays() {
        return essays;
    }

    public void setEssays(List<Essay> essays) {
        this.essays = essays;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
