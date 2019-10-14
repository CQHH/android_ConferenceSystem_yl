package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.ManagerDepartmentPojo;

import java.util.List;

/**
 * @title  部门管理适配器
 * @date   2019/02/27
 * @author enmaoFu
 */
public class ManagerDepartmentAdapter extends BaseQuickAdapter<ManagerDepartmentPojo,BaseViewHolder> {
    public ManagerDepartmentAdapter(int layoutResId, List<ManagerDepartmentPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ManagerDepartmentPojo item) {
        helper.setText(R.id.name,item.getDepartmentName());
        helper.addOnClickListener(R.id.updata);
        helper.addOnClickListener(R.id.post);
        helper.addOnClickListener(R.id.delete);
    }
}
