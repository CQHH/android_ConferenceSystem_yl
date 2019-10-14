package com.hhxk.app.util.record;

/**
 * @author mysong
 * created on: 2018/12/17 17:23
 * description:
 */

public class SpeechError extends Exception {

    private int mErrorCode;
    private String mDescription;

    public SpeechError(int errorCode) {
        this.mErrorCode = errorCode;
        if (mErrorCode == 20006) {
            mDescription = "启动录音失败";
        }
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public String getErrorDescription() {
        return this.mDescription;
    }

}
