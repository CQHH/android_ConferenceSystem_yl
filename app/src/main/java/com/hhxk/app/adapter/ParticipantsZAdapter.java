package com.hhxk.app.adapter;

import android.content.Context;
import android.view.View;

import com.em.baseframe.adapter.listview.CommonAdapter;
import com.em.baseframe.adapter.listview.ViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.ParticipantsCPojo;
import com.hhxk.app.pojo.ParticipantsZPojo;

import java.util.List;

/**
 * @title  发起会议-参会人员适配器
 * @date   2019/03/01
 * @author enmaoFu
 */
public class ParticipantsZAdapter extends CommonAdapter<ParticipantsZPojo> {

    /**
     * 监听
     */
    private ParticipantsZAdapter.OnDeleteItemListener onDeleteItemListener;

    public ParticipantsZAdapter(Context context, List<ParticipantsZPojo> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, final ParticipantsZPojo item, final int positon) {
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
    public void setOnDeleteItemListener(ParticipantsZAdapter.OnDeleteItemListener onDeleteItemListener) {
        this.onDeleteItemListener = onDeleteItemListener;
    }

    public interface OnDeleteItemListener {
        void onItemClick(ParticipantsZPojo participantsZPojo, int positon);
    }
}
