package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.ManagerPostPojo;

import java.util.List;

/**
 * @title  管理操作-部门管理职务嵌套的适配器
 * @date   2019/02/27
 * @author enmaoFu
 */
public class ManagerPostAdapter extends BaseQuickAdapter<ManagerPostPojo,BaseViewHolder> {
    public ManagerPostAdapter(int layoutResId, List<ManagerPostPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ManagerPostPojo item) {
        helper.setText(R.id.name,item.getPositionName());
        helper.addOnClickListener(R.id.delete);
        helper.addOnClickListener(R.id.updata);
    }
}
