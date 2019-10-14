package com.hhxk.app.adapter;

import android.net.Uri;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.DateTool;
import com.hhxk.app.R;
import com.hhxk.app.interfaces.BaseHttp;
import com.hhxk.app.pojo.TopicsFilePojo;

import java.util.List;

/**
 * @title  发起会议-会议议题添加附件适配器
 * @date   2019/03/06
 * @author enmaoFu
 */
public class TopicsFileAdapter extends BaseQuickAdapter<TopicsFilePojo,BaseViewHolder> implements BaseHttp {
    public TopicsFileAdapter(int layoutResId, List<TopicsFilePojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicsFilePojo item) {

        if(item.getIsSee().equals("1")){
            helper.setVisible(R.id.identification,true);
        }else{
            helper.setVisible(R.id.identification,false);
        }

        if(item.getType().equals("doc")){
            helper.setVisible(R.id.img_fresco,false);
            helper.setVisible(R.id.img_re,true);
            helper.setImageResource(R.id.img,R.drawable.word);
        }else if(item.getType().equals("docx")){
            helper.setVisible(R.id.img_fresco,false);
            helper.setVisible(R.id.img_re,true);
            helper.setImageResource(R.id.img,R.drawable.word);
        }else if(item.getType().equals("ppt")){
            helper.setVisible(R.id.img_fresco,false);
            helper.setVisible(R.id.img_re,true);
            helper.setImageResource(R.id.img,R.drawable.ppt);
        }else if(item.getType().equals("pptx")){
            helper.setVisible(R.id.img_fresco,false);
            helper.setVisible(R.id.img_re,true);
            helper.setImageResource(R.id.img,R.drawable.ppt);
        }else if(item.getType().equals("xls")){
            helper.setVisible(R.id.img_fresco,false);
            helper.setVisible(R.id.img_re,true);
            helper.setImageResource(R.id.img,R.drawable.xls);
        }else if(item.getType().equals("xlsx")){
            helper.setVisible(R.id.img_fresco,false);
            helper.setVisible(R.id.img_re,true);
            helper.setImageResource(R.id.img,R.drawable.xls);
        }else if(item.getType().equals("pdf")){
            helper.setVisible(R.id.img_fresco,false);
            helper.setVisible(R.id.img_re,true);
            helper.setImageResource(R.id.img,R.drawable.pdf);
        }else if(item.getType().equals("jpg") || item.getType().equals("png")){
            helper.setVisible(R.id.img_fresco,true);
            helper.setVisible(R.id.img_re,false);
            helper.setImageByUrl(R.id.img_fresco,BASEHTTP + item.getLssusefile_path());
        }else if(item.getType().equals("mp4")){
            helper.setVisible(R.id.img_fresco,false);
            helper.setVisible(R.id.img_re,true);
            helper.setImageResource(R.id.img,R.drawable.video);
        }else{
            helper.setVisible(R.id.img_fresco,false);
            helper.setVisible(R.id.img_re,true);
            helper.setImageResource(R.id.img,R.drawable.file);
        }
        helper.setText(R.id.title,item.getLssusefile_name());
        helper.setText(R.id.date,DateTool.timeStamp2Date(item.getLssusefile_date(),"yyyy-MM-dd HH:mm"));
        helper.addOnClickListener(R.id.delete);
    }
}
