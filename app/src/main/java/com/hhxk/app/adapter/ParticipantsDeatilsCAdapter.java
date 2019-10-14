package com.hhxk.app.adapter;

import android.content.Context;
import android.view.View;

import com.em.baseframe.adapter.listview.CommonAdapter;
import com.em.baseframe.adapter.listview.ViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.ParticipantsDeatilsCPojo;
import com.hhxk.app.pojo.ParticipantsDeatilsZPojo;

import java.util.List;

/**
 * @title  会议详情-会议详情嵌套的参会人员适配器
 * @date   2019/02/25
 * @author enmaoFu
 */
public class ParticipantsDeatilsCAdapter extends CommonAdapter<ParticipantsDeatilsCPojo> {

    /**
     * 监听
     */
    private ParticipantsDeatilsCAdapter.OnDeleteItemListener onDeleteItemListener;

    public ParticipantsDeatilsCAdapter(Context context, List<ParticipantsDeatilsCPojo> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, final ParticipantsDeatilsCPojo item, final int positon) {

        switch (item.getUser_role_id()){
            case "1":
                holder.setViewVisibility(R.id.delete,View.VISIBLE);
                break;
            case "2":
                holder.setViewVisibility(R.id.delete,View.GONE);
                break;
        }

        switch (item.getSign_in_status()){
            case "1":
                holder.setTextViewText(R.id.sign,"缺席");
                break;
            case "2":
                holder.setTextViewText(R.id.sign,"已签到");
                break;
        }

        holder.setTextViewText(R.id.code,String.valueOf(item.getUser_id()));
        holder.setTextViewText(R.id.name,item.getUser_name());
        holder.setTextViewText(R.id.de,item.getDepartment_name());
        holder.setTextViewText(R.id.post,item.getPosition_name());
        holder.setOnClick(R.id.delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteItemListener.onItemClick(item, positon);
            }
        });
    }

    public void setOnDeleteItemListener(ParticipantsDeatilsCAdapter.OnDeleteItemListener onDeleteItemListener) {
        this.onDeleteItemListener = onDeleteItemListener;
    }

    public interface OnDeleteItemListener {
        void onItemClick(ParticipantsDeatilsCPojo participantsDeatilsCPojo, int positon);
    }

}
