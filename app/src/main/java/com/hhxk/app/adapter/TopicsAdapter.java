package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.TopicsPojo;

import java.util.List;

/**
 * @title  发起会议-会议议题recyclerview适配器
 * @date   2019/02/20
 * @author enmaoFu
 */
public class TopicsAdapter extends BaseQuickAdapter<TopicsPojo,BaseViewHolder> {

    public TopicsAdapter(int layoutResId, List<TopicsPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicsPojo item) {
        helper.setText(R.id.title,item.getLssue_name());
        helper.setText(R.id.name,"汇报人：" + item.getUser_name());
        helper.setText(R.id.po,"职务：" + item.getPosition());
        helper.addOnClickListener(R.id.delete);
        helper.addOnClickListener(R.id.updata);
        helper.addOnClickListener(R.id.up_img);
        helper.addOnClickListener(R.id.down_img);
        helper.addOnClickListener(R.id.up);
    }
}
