package com.hhxk.app.pojo;

/**
 * @title  会议详情-会议详情嵌套的基本信息实体类
 * @date   2019/02/25
 * @author enmaoFu
 */
public class EssentialinformationDetailsPojo {


    /**
     * meeting_info : 黄裳： 我没提防住，看见了这个问题。 才意识到人类的自大已经达到了这种程度了。 太阳在那里亮着。浪不浪费。与你何干？ 试想以下的场景： 澳大利亚的牧场上，有两…
     * meeting_start : 1551110400000
     * meeting_status : 2
     * meeting_id : 1
     * meeting_user_id : 1
     * meeting_end : 1551801600000
     * meeting_name : 第一次会议
     * meeting_date : 1551424807000
     */

    private String meeting_info;
    private long meeting_start;
    private String meeting_status;
    private int meeting_id;
    private String meeting_user_id;
    private long meeting_end;
    private String meeting_name;
    private long meeting_date;
    private String meeting_address;

    public String getMeeting_address() {
        return meeting_address;
    }

    public void setMeeting_address(String meeting_address) {
        this.meeting_address = meeting_address;
    }

    public String getMeeting_info() {
        return meeting_info;
    }

    public void setMeeting_info(String meeting_info) {
        this.meeting_info = meeting_info;
    }

    public long getMeeting_start() {
        return meeting_start;
    }

    public void setMeeting_start(long meeting_start) {
        this.meeting_start = meeting_start;
    }

    public String getMeeting_status() {
        return meeting_status;
    }

    public void setMeeting_status(String meeting_status) {
        this.meeting_status = meeting_status;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getMeeting_user_id() {
        return meeting_user_id;
    }

    public void setMeeting_user_id(String meeting_user_id) {
        this.meeting_user_id = meeting_user_id;
    }

    public long getMeeting_end() {
        return meeting_end;
    }

    public void setMeeting_end(long meeting_end) {
        this.meeting_end = meeting_end;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public long getMeeting_date() {
        return meeting_date;
    }

    public void setMeeting_date(long meeting_date) {
        this.meeting_date = meeting_date;
    }
}
