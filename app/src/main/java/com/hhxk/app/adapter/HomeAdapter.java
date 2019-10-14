package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseSectionQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.HomeHeadPojo;

import java.util.List;

/**
 * @title  首页recyclerview适配器
 * @date   2019/02/20
 * @author enmaoFu
 */
public class HomeAdapter extends BaseSectionQuickAdapter<HomeHeadPojo, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public HomeAdapter(int layoutResId, int sectionHeadResId, List<HomeHeadPojo> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, HomeHeadPojo item) {
        helper.setText(R.id.title,item.getStrTitle());
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeHeadPojo item) {
        helper.setText(R.id.title,item.t.getTitle());
        helper.setText(R.id.start_time,"开始时间：" + item.t.getDate());
        helper.setText(R.id.host,"主持人：" + item.t.getPerson());
        helper.setText(R.id.text,item.t.getIntroduction());
        helper.setText(R.id.text_y,"应到：" + item.t.getStrY());
        helper.setText(R.id.text_q,"签到：" + item.t.getStrQ());
        helper.setText(R.id.text_d,"缺席：" + item.t.getStrD());
    }
}
