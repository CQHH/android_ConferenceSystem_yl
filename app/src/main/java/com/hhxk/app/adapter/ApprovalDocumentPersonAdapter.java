package com.hhxk.app.adapter;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.pojo.ApprovalDocumentPersonPojo;

import java.util.List;

/**
 * @title  发起会议-审批文件-审批人recyclerview适配器
 * @date   2019/02/21
 * @author enmaoFu
 */
public class ApprovalDocumentPersonAdapter extends BaseQuickAdapter<ApprovalDocumentPersonPojo,BaseViewHolder> {
    public ApprovalDocumentPersonAdapter(int layoutResId, List<ApprovalDocumentPersonPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApprovalDocumentPersonPojo item) {

    }
}
