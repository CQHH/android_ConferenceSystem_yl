package com.hhxk.app.event;

/**
 * @title  发起会议-下一步通知
 * @date   2019/02/28
 * @author enmaoFu
 */
public class SponsorEvent {

    private String code;

    /**
     * 会议Id
     */
    private int meetingId;

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
