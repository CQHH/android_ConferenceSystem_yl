package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.DateTool;
import com.hhxk.app.R;
import com.hhxk.app.pojo.ArchivesPojo;

import java.util.List;

/**
 * @title  会议档案适配器
 * @date   2019/02/21
 * @author enmaoFu
 */
public class ArchivesAdapter extends BaseQuickAdapter<ArchivesPojo,BaseViewHolder> {
    public ArchivesAdapter(int layoutResId, List<ArchivesPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArchivesPojo item) {
        helper.setText(R.id.title,item.getMeeting_name());
        if(item.getMeeting_start() != 0 && item.getMeeting_end() != 0){
            helper.setText(R.id.date,DateTool.timesToStrTime(String.valueOf(item.getMeeting_start()),"yyyy-MM-dd HH:mm") + "\n至\n"  +
                DateTool.timesToStrTime(String.valueOf(item.getMeeting_end()),"yyyy-MM-dd HH:mm"));
        }else{
            helper.setText(R.id.date,"无数据");
        }
        helper.setText(R.id.content,item.getMeeting_info());
        if(null != item.getUserList() && item.getUserList().size() > 0){
            helper.setText(R.id.host,item.getUserList().get(0) + "...");
        }else{
            helper.setText(R.id.host,"无数据");
        }
        helper.addOnClickListener(R.id.delete);
    }
}
