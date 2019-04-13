package com.houxiang.wenbiserver.esModel;

import java.util.Date;

public class Esessay {
    private Integer id;
    private String title;
    private String author;
    private String content;
    private String img;
    private Date date;


    public Esessay(Integer id, String title, String author, String content, String img, Date date) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.img = img;
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formatDate = format1.format(date);
        return "{" +
                "\"title\":\""+title+"\"," +
                "\"author\":\""+author+"\"," +
                "\"content\":\""+content+"\"," +
                "\"img\":\""+img+"\"," +
                "\"date\":\""+formatDate+"\"" +
                "}";
    }
}