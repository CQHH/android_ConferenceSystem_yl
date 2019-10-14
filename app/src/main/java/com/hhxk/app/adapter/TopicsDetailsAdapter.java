package com.hhxk.app.adapter;

import android.view.View;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.TopicsDetailsPojo;
import com.hhxk.app.pojo.TopicsPojo;

import java.util.List;

/**
 * @title  会议详情-会议议题recyclerview适配器
 * @date   2019/02/25
 * @author enmaoFu
 */
public class TopicsDetailsAdapter extends BaseQuickAdapter<TopicsDetailsPojo,BaseViewHolder> {

    public TopicsDetailsAdapter(int layoutResId, List<TopicsDetailsPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicsDetailsPojo item) {

        switch (item.getRole_id()){
            case "1":
                helper.setVisible(R.id.up_img,true);
                helper.setVisible(R.id.down_img,true);
                helper.setVisible(R.id.up,true);
                helper.setVisible(R.id.updata,true);
                helper.setVisible(R.id.delete,true);
                helper.setVisible(R.id.show,false);
                break;
            case "2":
                helper.setVisible(R.id.up_img,false);
                helper.setVisible(R.id.down_img,false);
                helper.setVisible(R.id.up,false);
                helper.setVisible(R.id.updata,false);
                helper.setVisible(R.id.delete,false);
                helper.setVisible(R.id.show,true);
                break;
        }

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
