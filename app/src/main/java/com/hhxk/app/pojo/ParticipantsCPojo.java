package com.hhxk.app.pojo;

/**
 * @title  发起会议-参会人员实体类
 * @date   2019/03/01
 * @author enmaoFu
 */
public class ParticipantsCPojo {

    private int isSelect = 0;

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    /**
     * meeting_info : 3
     * user_status : 1
     * meeting_start : 1551671580000
     * meeting_status : 1
     * department_id : 1
     * user_name : aaa
     * meeting_id : 37
     * department_name : 111444
     * meeting_user_id : 70
     * position_name : 局长
     * meeting_end : 1551708000000
     * meeting_user_status : 1
     * user_role_id : 2
     * user_id : 11
     * user_account : aaa
     * meeting_name : 3
     * position_id : 1
     */

    private String meeting_info;
    private String user_status;
    private long meeting_start;
    private String meeting_status;
    private int department_id;
    private String user_name;
    private int meeting_id;
    private String department_name;
    private int meeting_user_id;
    private String position_name;
    private long meeting_end;
    private String meeting_user_status;
    private String user_role_id;
    private int user_id;
    private String user_account;
    private String meeting_name;
    private int position_id;

    public String getMeeting_info() {
        return meeting_info;
    }

    public void setMeeting_info(String meeting_info) {
        this.meeting_info = meeting_info;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
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

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public int getMeeting_user_id() {
        return meeting_user_id;
    }

    public void setMeeting_user_id(int meeting_user_id) {
        this.meeting_user_id = meeting_user_id;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public long getMeeting_end() {
        return meeting_end;
    }

    public void setMeeting_end(long meeting_end) {
        this.meeting_end = meeting_end;
    }

    public String getMeeting_user_status() {
        return meeting_user_status;
    }

    public void setMeeting_user_status(String meeting_user_status) {
        this.meeting_user_status = meeting_user_status;
    }

    public String getUser_role_id() {
        return user_role_id;
    }

    public void setUser_role_id(String user_role_id) {
        this.user_role_id = user_role_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
    }
}
