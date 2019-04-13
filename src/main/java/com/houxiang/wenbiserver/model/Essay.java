package com.houxiang.wenbiserver.model;

import java.sql.Timestamp;
import java.util.Date;

public class Essay implements Comparable<Essay> {

    private Integer essayId;
    private Integer authorId;
    private String authorName;
    private String essayTitle;
    private String essayContent;
    private Timestamp essayDate;
    private short essayState;
    private String essayCover;
    private Integer isCollect;
    private Integer visitCount;
    private Integer collectCount;


    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

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

    @Override
    public int compareTo(Essay obj) {
        if(obj.collectCount*0.7+obj.visitCount*0.3>collectCount*0.7+visitCount*0.3){
            return -1;
        }else{
            return 1;
        }
    }
    @Override
    public String toString() {
        return "Essay{" +
                "essayId=" + essayId +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", essayTitle='" + essayTitle + '\'' +
                ", essayContent='" + essayContent + '\'' +
                ", essayDate=" + essayDate +
                ", essayState=" + essayState +
                ", essayCover='" + essayCover + '\'' +
                ", isCollect=" + isCollect +
                ", visitCount=" + visitCount +
                ", collectCount=" + collectCount +
                '}';
    }
}
