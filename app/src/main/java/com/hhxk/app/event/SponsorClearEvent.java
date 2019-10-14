package com.hhxk.app.event;

/**
 * @title  发起会议完成通知
 * @date   2019/03/06
 * @author enmaoFu
 */
public class SponsorClearEvent {

    private String Clear;

    public SponsorClearEvent(String clear) {
        Clear = clear;
    }

    public String getClear() {
        return Clear;
    }

    public void setClear(String clear) {
        Clear = clear;
    }
}
