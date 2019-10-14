package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.ManagePersonPojo;

import java.util.List;

/**
 * @title  成员管理实体类
 * @date   2019/02/26
 * @author enmaoFu
 */
public class ManagePersonAdapter extends BaseQuickAdapter<ManagePersonPojo,BaseViewHolder> {

    public ManagePersonAdapter(int layoutResId, List<ManagePersonPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ManagePersonPojo item) {

        helper.setText(R.id.xh,String.valueOf(item.getUser_id()));
        helper.setText(R.id.code,item.getUser_account());
        helper.setText(R.id.name,item.getUser_name());
        helper.setText(R.id.title,item.getPosition_name());
        helper.setText(R.id.title_one,item.getDepartment_name());
        helper.addOnClickListener(R.id.uppwd);
        helper.addOnClickListener(R.id.delete);
        helper.addOnClickListener(R.id.updata);
    }
}
