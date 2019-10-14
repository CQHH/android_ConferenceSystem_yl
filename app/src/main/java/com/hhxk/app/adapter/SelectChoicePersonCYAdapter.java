package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.SelectChoicePersonCYPojo;

import java.util.List;

/**
 * @title  已选择参与人员view适配器
 * @date   2019/03/01
 * @author enmaoFu
 */
public class SelectChoicePersonCYAdapter extends BaseQuickAdapter<SelectChoicePersonCYPojo,BaseViewHolder> {

    public SelectChoicePersonCYAdapter(int layoutResId, List<SelectChoicePersonCYPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectChoicePersonCYPojo item) {
        helper.addOnClickListener(R.id.name);
        helper.setText(R.id.name,item.getUser_name());
    }
}
