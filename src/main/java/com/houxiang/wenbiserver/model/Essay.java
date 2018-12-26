package com.houxiang.wenbiserver.model;

import java.sql.Timestamp;

public class Essay {

    private Integer essayId;
    private Integer authorId;
    private String authorName;
    private String essayTitle;
    private String essayContent;
    private Timestamp essayDate;
    private short essayState;
    private String essayCover;


    public Integer getEssayId() {
        return essayId;
    }

    public void setEssayId(Integer essayId) {
        this.essayId = essayId;
    }

    public String getEssayTitle() {
        return essayTitle;
    }

    public void setEssayTitle(String essayTitle) {
        this.essayTitle = essayTitle;
    }

    public String getEssayContent() {
        return essayContent;
    }

    public void setEssayContent(String essayContent) {
        this.essayContent = essayContent;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Timestamp getEssayDate() {
        return essayDate;
    }

    public void setEssayDate(Timestamp essayDate) {
        this.essayDate = essayDate;
    }

    public short getEssayState() {
        return essayState;
    }

    public void setEssayState(short essayState) {
        this.essayState = essayState;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getEssayCover() {
        return essayCover;
    }

    public void setEssayCover(String essayCover) {
        this.essayCover = essayCover;
    }
}
