package com.hhxk.app.adapter;

import android.content.Context;
import android.view.View;

import com.em.baseframe.adapter.listview.CommonAdapter;
import com.em.baseframe.adapter.listview.ViewHolder;
import com.hhxk.app.R;
import com.hhxk.app.pojo.ParticipantsCPojo;

import java.util.List;

/**
 * @title  发起会议-参会人员适配器
 * @date   2019/03/01
 * @author enmaoFu
 */
public class ParticipantsCAdapter extends CommonAdapter<ParticipantsCPojo> {

    /**
     * 监听
     */
    private OnDeleteItemListener onDeleteItemListener;

    public ParticipantsCAdapter(Context context, List<ParticipantsCPojo> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, final ParticipantsCPojo item, final int positon) {
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

    public void setOnDeleteItemListener(OnDeleteItemListener onDeleteItemListener) {
        this.onDeleteItemListener = onDeleteItemListener;
    }

    public interface OnDeleteItemListener {
        void onItemClick(ParticipantsCPojo medicalAddCasePojo, int positon);
    }
}
