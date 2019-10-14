package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.SelectChoicePersonCPojo;
import com.hhxk.app.pojo.SelectChoicePersonZPojo;

import java.util.List;

/**
 * @title  选择参与人员view适配器
 * @date   2019/02/25
 * @author enmaoFu
 */
public class SelectChoicePersonZAdapter extends BaseQuickAdapter<SelectChoicePersonZPojo,BaseViewHolder> {
    public SelectChoicePersonZAdapter(int layoutResId, List<SelectChoicePersonZPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectChoicePersonZPojo item) {
        helper.setText(R.id.name,item.getUser_name());
        helper.setText(R.id.post,item.getPosition_name());
        helper.setText(R.id.department,item.getDepartment_name());
    }
}
