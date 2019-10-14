package com.hhxk.app.pojo;

/**
 * @title  部门管理实体类
 * @date   2019/02/27
 * @author enmaoFu
 */
public class ManagerDepartmentPojo {

    /**
     * departmentDate : 1551164573000
     * departmentId : 1
     * departmentName : 财政局
     * departmentPosition : 1
     * departmentStatus : 1
     */

    private long departmentDate;
    private int departmentId;
    private String departmentName;
    private String departmentPosition;
    private String departmentStatus;

    public long getDepartmentDate() {
        return departmentDate;
    }

    public void setDepartmentDate(long departmentDate) {
        this.departmentDate = departmentDate;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentPosition() {
        return departmentPosition;
    }

    public void setDepartmentPosition(String departmentPosition) {
        this.departmentPosition = departmentPosition;
    }

    public String getDepartmentStatus() {
        return departmentStatus;
    }

    public void setDepartmentStatus(String departmentStatus) {
        this.departmentStatus = departmentStatus;
    }
}
