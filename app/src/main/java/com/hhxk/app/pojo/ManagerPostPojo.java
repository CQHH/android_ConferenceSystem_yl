package com.hhxk.app.pojo;

/**
 * @title  管理操作-部门管理职务嵌套的实体类
 * @date   2019/02/27
 * @author enmaoFu
 */
public class ManagerPostPojo {


    /**
     * departmentId : 1
     * positionDate : 1551249718000
     * positionId : 1
     * positionName : 局长
     * positionStatus : 1
     */

    private String departmentId;
    private long positionDate;
    private int positionId;
    private String positionName;
    private String positionStatus;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public long getPositionDate() {
        return positionDate;
    }

    public void setPositionDate(long positionDate) {
        this.positionDate = positionDate;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }
}
