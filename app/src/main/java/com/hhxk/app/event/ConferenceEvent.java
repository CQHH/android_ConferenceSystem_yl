package com.hhxk.app.event;

/**
 * @title  我的会议-事件总线通知
 * @date   2019/02/21
 * @author enmaoFu
 */
public class ConferenceEvent {

    public ConferenceEvent(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
