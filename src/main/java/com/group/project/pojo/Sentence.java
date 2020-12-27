package com.group.project.pojo;

import java.util.Date;

public class Sentence {
    private int id;
    private String username;
    private String production;
    private Date date;
    private int goodnum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getGoodnum() {
        return goodnum;
    }

    public void setGoodnum(int goodnum) {
        this.goodnum = goodnum;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", production='" + production + '\'' +
                ", date=" + date +
                ", goodnum=" + goodnum +
                '}';
    }
}
