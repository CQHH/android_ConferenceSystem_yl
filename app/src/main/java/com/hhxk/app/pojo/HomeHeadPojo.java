package com.hhxk.app.pojo;

import com.em.baseframe.adapter.recyclerview.entity.SectionEntity;

/**
 * @title  系统首页recyclerview-分组头部实体类
 * @date   2019/02/20
 * @author enmaoFu
 */
public class HomeHeadPojo extends SectionEntity<HomeItemPojo> {

    private String strTitle;

    public HomeHeadPojo(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public HomeHeadPojo(HomeItemPojo homeItemPojo) {
        super(homeItemPojo);
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }
}
