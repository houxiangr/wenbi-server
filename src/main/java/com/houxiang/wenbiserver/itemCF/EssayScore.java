package com.houxiang.wenbiserver.itemCF;

import com.houxiang.wenbiserver.model.Essay;

public class EssayScore implements Comparable<EssayScore> {
    private Integer essayID;
    private Double score;

    public EssayScore(Integer essayID, Double score) {
        this.essayID = essayID;
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getEssayID() {
        return essayID;
    }

    public void setEssayID(Integer essayID) {
        this.essayID = essayID;
    }


    @Override
    public int compareTo(EssayScore obj) {
        if(obj.score>score){
            return -1;
        }else{
            return 1;
        }
    }
}
