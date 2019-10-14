package com.hhxk.app.application;

import com.em.baseframe.application.BaseApplication;
import com.tencent.mmkv.MMKV;

/**
 * @title  全局类
 * @date   2017/06/17
 * @author enmaoFu
 */
public class MyApplication extends BaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
    }
}
