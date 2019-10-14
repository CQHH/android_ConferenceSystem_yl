package com.hhxk.app.pojo;

/**
 * @title  发起会议-会议议题添加附件实体类
 * @date   2019/03/06
 * @author enmaoFu
 */
public class TopicsFilePojo {

    private String isSee;

    private String lssusefile_path;

    private String lssue_id;

    private String lssusefile_status;

    private int lssusefile_id;

    private String lssusefile_name;

    private long lssusefile_date;

    private String type;

    public String getIsSee() {
        return isSee;
    }

    public void setIsSee(String isSee) {
        this.isSee = isSee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLssusefile_path() {
        return lssusefile_path;
    }

    public void setLssusefile_path(String lssusefile_path) {
        this.lssusefile_path = lssusefile_path;
    }

    public String getLssue_id() {
        return lssue_id;
    }

    public void setLssue_id(String lssue_id) {
        this.lssue_id = lssue_id;
    }

    public String getLssusefile_status() {
        return lssusefile_status;
    }

    public void setLssusefile_status(String lssusefile_status) {
        this.lssusefile_status = lssusefile_status;
    }

    public int getLssusefile_id() {
        return lssusefile_id;
    }

    public void setLssusefile_id(int lssusefile_id) {
        this.lssusefile_id = lssusefile_id;
    }

    public String getLssusefile_name() {
        return lssusefile_name;
    }

    public void setLssusefile_name(String lssusefile_name) {
        this.lssusefile_name = lssusefile_name;
    }

    public long getLssusefile_date() {
        return lssusefile_date;
    }

    public void setLssusefile_date(long lssusefile_date) {
        this.lssusefile_date = lssusefile_date;
    }
}
