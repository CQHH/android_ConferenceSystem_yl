package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.ManagerDepartmentListPojo;

import java.util.List;

public class ManagerDepartmentListAdapter extends BaseQuickAdapter<ManagerDepartmentListPojo,BaseViewHolder> {

    public ManagerDepartmentListAdapter(int layoutResId, List<ManagerDepartmentListPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ManagerDepartmentListPojo item) {
        helper.setText(R.id.name,item.getDepartmentName());
        switch (item.getIs()){
            case 0:
                helper.setImageResource(R.id.select,R.drawable.select_n);
                break;
            case 1:
                helper.setImageResource(R.id.select,R.drawable.select_y);
                break;
        }
    }
}
