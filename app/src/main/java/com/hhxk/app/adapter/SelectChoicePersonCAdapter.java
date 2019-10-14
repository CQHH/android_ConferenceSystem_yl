package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.SelectChoicePersonCPojo;

import java.util.List;

/**
 * @title  选择参与人员view适配器
 * @date   2019/03/01
 * @author enmaoFu
 */
public class SelectChoicePersonCAdapter extends BaseQuickAdapter<SelectChoicePersonCPojo,BaseViewHolder> {
    public SelectChoicePersonCAdapter(int layoutResId, List<SelectChoicePersonCPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectChoicePersonCPojo item) {
        helper.setText(R.id.name,item.getUser_name());
        helper.setText(R.id.post,item.getPosition_name());
        helper.setText(R.id.department,item.getDepartment_name());
    }
}
