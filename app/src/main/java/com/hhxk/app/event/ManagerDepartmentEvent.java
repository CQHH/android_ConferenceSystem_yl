package com.hhxk.app.event;

/**
 * @title  管理操作-部门管理职务通知
 * @date   2019/02/27
 * @author enmaoFu
 */
public class ManagerDepartmentEvent {

    private String code;

    private String departmentId;

    public ManagerDepartmentEvent(String code, String departmentId) {
        this.code = code;
        this.departmentId = departmentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
