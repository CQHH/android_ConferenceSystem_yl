package com.hhxk.app.pojo;

/**
 * @title  系统首页recyclerview-分组组体实体类
 * @date   2019/02/20
 * @author enmaoFu
 */
public class HomeItemPojo {

    private int po;

    private String title;

    private String date;

    private String person;

    private String introduction;

    private String strY;

    private String strQ;

    private String strD;

    private int meeting_id;

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public int getPo() {
        return po;
    }

    public void setPo(int po) {
        this.po = po;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStrY() {
        return strY;
    }

    public void setStrY(String strY) {
        this.strY = strY;
    }

    public String getStrQ() {
        return strQ;
    }

    public void setStrQ(String strQ) {
        this.strQ = strQ;
    }

    public String getStrD() {
        return strD;
    }

    public void setStrD(String strD) {
        this.strD = strD;
    }
}
