package com.em.baseframe.config;

import android.os.Environment;

/**
 * @title  设置文件保存路径,如SD卡等缓存
 * @date   2017/06/17
 * @author enmaoFu
 */
public class SavePath {
    public static final String SAVE_PATH = Environment.getExternalStorageDirectory()+ "/transfer/";
}
