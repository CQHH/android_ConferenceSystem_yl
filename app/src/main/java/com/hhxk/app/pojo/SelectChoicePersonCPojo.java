package com.hhxk.app.pojo;

/**
 * @title  选择参与人员view实体类
 * @date   2019/02/25
 * @author enmaoFu
 */
public class SelectChoicePersonCPojo {

    /**
     * department_id : 2
     * user_id : 6
     * user_name : 用户
     * department_name : 公安局
     * position_name : 保安
     * user_account : 2
     * position_id : 5
     */

    private String department_id;
    private int user_id;
    private String user_name;
    private String department_name;
    private String position_name;
    private String user_account;
    private String position_id;

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getPosition_id() {
        return position_id;
    }

    public void setPosition_id(String position_id) {
        this.position_id = position_id;
    }
}
