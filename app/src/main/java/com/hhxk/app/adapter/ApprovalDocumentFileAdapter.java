package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.pojo.ApprovalDocumentFilePojo;

import java.util.List;

/**
 * @title  发起会议-审批文件-文件recyclerview适配器
 * @date   2019/02/21
 * @author enmaoFu
 */
public class ApprovalDocumentFileAdapter extends BaseQuickAdapter<ApprovalDocumentFilePojo,BaseViewHolder> {
    public ApprovalDocumentFileAdapter(int layoutResId, List<ApprovalDocumentFilePojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApprovalDocumentFilePojo item) {

    }
}
