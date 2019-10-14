package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.SelectChoicePersonZYPojo;

import java.util.List;

/**
 * @title  已选择主持人员view适配器
 * @date   2019/03/01
 * @author enmaoFu
 */
public class SelectChoicePersonZYAdapter extends BaseQuickAdapter<SelectChoicePersonZYPojo,BaseViewHolder> {
    public SelectChoicePersonZYAdapter(int layoutResId, List<SelectChoicePersonZYPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectChoicePersonZYPojo item) {
        helper.addOnClickListener(R.id.name);
        helper.setText(R.id.name,item.getUser_name());
    }
}
