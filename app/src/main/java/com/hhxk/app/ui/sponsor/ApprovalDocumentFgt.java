package com.hhxk.app.ui.sponsor;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.ApprovalDocumentFileAdapter;
import com.hhxk.app.adapter.ApprovalDocumentPersonAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.pojo.ApprovalDocumentFilePojo;
import com.hhxk.app.pojo.ApprovalDocumentPersonPojo;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @title  发起会议-审批文件fragemnt
 * @date   2019/02/20
 * @author enmaoFu
 */
public class ApprovalDocumentFgt extends BaseLazyFgt {

    @BindView(R.id.refresh)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;

    @BindView(R.id.refresh_two)
    PtrFrameLayout ptrFrameLayoutTwo;
    @BindView(R.id.recyvlerview_two)
    RecyclerView recyclerViewTwo;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 文件适配器
     */
    private ApprovalDocumentFileAdapter approvalDocumentFileAdapter;
    /**
     * 文件数据源
     */
    private List<ApprovalDocumentFilePojo> approvalDocumentFilePojos;

    /**
     * 文件适配器
     */
    private ApprovalDocumentPersonAdapter approvalDocumentPersonAdapter;
    /**
     * 文件数据源
     */
    private List<ApprovalDocumentPersonPojo> approvalDocumentPersonPojos;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_approval_document;
    }

    @Override
    protected void initData() {

        /**
         * 下拉刷新
         */
        PtrInitHelper.initPtr(getActivity(), ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.refreshComplete();
            }
        });

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        approvalDocumentFileAdapter = new ApprovalDocumentFileAdapter(R.layout.item_approval_document_file, setFileData());
        //设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        //大小不受适配器影响
        recyclerView.setHasFixedSize(true);
        //设置间隔样式
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor("#00E8E8E8"))
                        .sizeResId(R.dimen.recyclerview_item_hr_12)
                        .build());
        //设置adapter
        recyclerView.setAdapter(approvalDocumentFileAdapter);

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        approvalDocumentPersonAdapter = new ApprovalDocumentPersonAdapter(R.layout.item_approval_document_person, setPersonData());
        //设置布局管理器
        recyclerViewTwo.setLayoutManager(mLayoutManager);
        //大小不受适配器影响
        recyclerViewTwo.setHasFixedSize(true);
        //设置间隔样式
        recyclerViewTwo.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor("#E8E8E8"))
                        .sizeResId(R.dimen.recyclerview_item_hr)
                        .build());
        //设置adapter
        recyclerViewTwo.setAdapter(approvalDocumentPersonAdapter);

    }

    @Override
    protected void requestData() {

    }

    public List<ApprovalDocumentFilePojo> setFileData(){
        approvalDocumentFilePojos = new ArrayList<>();
        ApprovalDocumentFilePojo approvalDocumentFilePojo = null;
        for(int i = 0; i < 20; i++){
            approvalDocumentFilePojo = new ApprovalDocumentFilePojo();
            approvalDocumentFilePojos.add(approvalDocumentFilePojo);
        }
        return approvalDocumentFilePojos;
    }

    public List<ApprovalDocumentPersonPojo> setPersonData(){
        approvalDocumentPersonPojos = new ArrayList<>();
        ApprovalDocumentPersonPojo approvalDocumentPersonPojo = null;
        for(int i = 0; i < 20; i++){
            approvalDocumentPersonPojo = new ApprovalDocumentPersonPojo();
            approvalDocumentPersonPojos.add(approvalDocumentPersonPojo);
        }
        return approvalDocumentPersonPojos;
    }

}
