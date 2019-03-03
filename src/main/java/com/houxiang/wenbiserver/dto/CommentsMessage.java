package com.houxiang.wenbiserver.dto;

import com.houxiang.wenbiserver.model.Comment;

import java.util.List;

public class CommentsMessage {
    private boolean state;
    private String msg;
    private List<Comment> comments;

    public CommentsMessage(boolean state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public CommentsMessage(boolean state, List<Comment> comments) {
        this.state = state;
        this.comments = comments;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
