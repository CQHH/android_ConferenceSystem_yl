package com.hhxk.app.ui.details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.TopicsDetailsAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.TopicsDetailsPojo;
import com.hhxk.app.ui.FileListActivity;
import com.hhxk.app.view.AddIssueDialog;
import com.tencent.mmkv.MMKV;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  会议详情-会议详情嵌套的会议议题fragemnt
 * @date   2019/02/25
 * @author enmaoFu
 */
public class TopicsDetailsFgt extends BaseLazyFgt {

    @BindView(R.id.refresh)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;
    @BindView(R.id.hr)
    View hr;
    @BindView(R.id.down_re)
    RelativeLayout downRe;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private TopicsDetailsAdapter topicsDetailsAdapter;

    /**
     * 数据源
     */
    private List<TopicsDetailsPojo> topicsDetailsPojos;

    /**
     * 删除弹框
     */
    private DialogInterface deleteDialog;

    private AddIssueDialog edIssueDialog;

    /**
     * 删除下标
     */
    private int deletePo;

    /**
     * 修改下班
     */
    private int uppo;

    /**
     * 修改时的内容
     */
    private String title;
    private String name;
    private String po;

    private MMKV kv;

    private AddIssueDialog addIssueDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query_details_topics;
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();

        if("2".equals(kv.decodeString("role_id"))){
            hr.setVisibility(View.GONE);
            downRe.setVisibility(View.GONE);
        }else {
            hr.setVisibility(View.VISIBLE);
            downRe.setVisibility(View.VISIBLE);
        }

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
        topicsDetailsAdapter = new TopicsDetailsAdapter(R.layout.item_topics_details, setData());
        //设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        //大小不受适配器影响
        recyclerView.setHasFixedSize(true);
        //设置间隔样式
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor("#E8E8E8"))
                        .sizeResId(R.dimen.recyclerview_item_hr)
                        .build());
        //设置空数据页面
        setEmptyView(topicsDetailsAdapter);
        //设置adapter
        recyclerView.setAdapter(topicsDetailsAdapter);

        topicsDetailsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<TopicsDetailsPojo>() {
            @Override
            public void onItemClick(BaseQuickAdapter<TopicsDetailsPojo, ? extends BaseViewHolder> adapter, View view, int position) {
                String lssusId = String.valueOf(topicsDetailsAdapter.getItem(position).getLssue_id());
                Bundle bundle = new Bundle();
                bundle.putString("lssusId",lssusId);
                bundle.putString("roleId",topicsDetailsAdapter.getItem(position).getRole_id());
                startActivity(FileListActivity.class,bundle);
            }
        });
        topicsDetailsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<TopicsDetailsPojo>() {
            @Override
            public void onItemChildClick(BaseQuickAdapter<TopicsDetailsPojo, ? extends BaseViewHolder> adapter, View view, final int position) {
                switch (view.getId()){
                    case R.id.delete:
                        deletePo = position;
                        new AlertDialog.Builder(getActivity()).setTitle("是否删除该议题?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteDialog = dialog;
                                        int lssueId = topicsDetailsAdapter.getItem(position).getLssue_id();
                                        showLoadingDialog(null);
                                        doHttp(RetrofitUtils.createApi(Http.class).deleteLssue(lssueId),2);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();//在按键响应事件中显示此对话框
                        break;
                    case R.id.updata:
                        uppo = position;
                        final String titleRv = topicsDetailsAdapter.getItem(position).getLssue_name();
                        final String nameRv = topicsDetailsAdapter.getItem(position).getUser_name();
                        final String poRv = topicsDetailsAdapter.getItem(position).getPosition();
                        edIssueDialog = new AddIssueDialog(getActivity());
                        edIssueDialog.show();
                        edIssueDialog.setDialogAnimation();
                        edIssueDialog.setDialogTitle("修改会议议题");
                        edIssueDialog.setTitle(titleRv);
                        edIssueDialog.setName(nameRv);
                        edIssueDialog.setPo(poRv);
                        edIssueDialog.setOnclick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                title = edIssueDialog.getTitlle();
                                name = edIssueDialog.getName();
                                po = edIssueDialog.getPo();

                                if(titleRv.equals(title) && nameRv.equals(name) && poRv.equals(po)){
                                    edIssueDialog.dismiss();
                                    showErrorToast("没有信息改动");
                                }else{
                                    showLoadingDialog(null);
                                    doHttp(RetrofitUtils.createApi(Http.class).changeLssue(topicsDetailsAdapter.getItem(position).getLssue_id(),
                                            title,name,po),3);
                                }
                            }
                        });
                        break;
                    case R.id.up_img:
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Http.class).moveUpward(String.valueOf(EssentialinformationDetailsFgt.meetingId),
                                topicsDetailsAdapter.getItem(position).getLssue_id(),topicsDetailsAdapter.getItem(position).getLssue_order()),4);
                        break;
                    case R.id.down_img:
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Http.class).moveDown(String.valueOf(EssentialinformationDetailsFgt.meetingId),
                                topicsDetailsAdapter.getItem(position).getLssue_id(),topicsDetailsAdapter.getItem(position).getLssue_order()),5);
                        break;
                    case R.id.up:
                        String lssusId = String.valueOf(topicsDetailsAdapter.getItem(position).getLssue_id());
                        Bundle bundle = new Bundle();
                        bundle.putString("lssusId",lssusId);
                        bundle.putString("roleId",topicsDetailsAdapter.getItem(position).getRole_id());
                        startActivity(FileListActivity.class,bundle);
                        break;
                }
            }
        });
    }

    @Override
    protected boolean setIsInitRequestData() {
        return false;
    }

    @Override
    protected void requestData() {

    }

    @OnClick({R.id.down_re})
    @Override
    public void btnClick(View view) {
       switch (view.getId()){
           case R.id.down_re:
               addIssueDialog = new AddIssueDialog(getActivity());
               addIssueDialog.show();
               addIssueDialog.setDialogAnimation();
               addIssueDialog.setDialogTitle("添加会议议题");
               addIssueDialog.setOnclick(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String title = addIssueDialog.getTitlle();
                       String name = addIssueDialog.getName();
                       String po = addIssueDialog.getPo();
                       if(title.length() == 0){
                           showErrorToast("请输入议题标题");
                       }else if(name.length() == 0){
                           showErrorToast("请输入汇报人名称");
                       }else if(po.length() == 0){
                           showErrorToast("请输入汇报人职务");
                       }else{
                           showLoadingDialog(null);
                           doHttp(RetrofitUtils.createApi(Http.class).addLssue(kv.decodeInt("meeting_id_details"),
                                   title,name,po),6);
                       }
                   }
               });
               break;
       }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 2:
                topicsDetailsAdapter.remove(deletePo);
                deleteDialog.dismiss();
                break;
            case 3:
                TopicsDetailsPojo topicsDetailsPojo = topicsDetailsAdapter.getItem(uppo);
                topicsDetailsPojo.setLssue_name(title);
                topicsDetailsPojo.setUser_name(name);
                topicsDetailsPojo.setPosition(po);
                topicsDetailsAdapter.setData(uppo,topicsDetailsPojo);
                showToast("修改成功");
                edIssueDialog.dismiss();
                break;
            case 4:
                topicsDetailsPojos = AppJsonUtil.getArrayList(result,TopicsDetailsPojo.class);
                for(TopicsDetailsPojo tdp:topicsDetailsPojos){
                    tdp.setRole_id(kv.decodeString("role_id"));
                }
                topicsDetailsAdapter.setNewData(topicsDetailsPojos);
                showToast("更改议题顺序成功");
                break;
            case 5:
                topicsDetailsPojos = AppJsonUtil.getArrayList(result,TopicsDetailsPojo.class);
                for(TopicsDetailsPojo tdp:topicsDetailsPojos){
                    tdp.setRole_id(kv.decodeString("role_id"));
                }
                topicsDetailsAdapter.setNewData(topicsDetailsPojos);
                showToast("更改议题顺序成功");
                break;
            case 6:
                topicsDetailsPojos = AppJsonUtil.getArrayList(result,TopicsDetailsPojo.class);
                for(TopicsDetailsPojo tdp:topicsDetailsPojos){
                    tdp.setRole_id(kv.decodeString("role_id"));
                }
                topicsDetailsAdapter.setNewData(topicsDetailsPojos);
                showToast("添加成功");
                addIssueDialog.dismiss();
                break;
        }
    }

    public List<TopicsDetailsPojo> setData(){
        if(null != EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos){
            if(EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos.size() != 0){
                topicsDetailsPojos = new ArrayList<>();
                TopicsDetailsPojo topicsDetailsPojo = null;
                for(int i = 0; i < EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos.size(); i++){
                    topicsDetailsPojo = new TopicsDetailsPojo();
                    topicsDetailsPojo.setLssue_id(EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos.get(i).getLssue_id());
                    topicsDetailsPojo.setLssue_isend(EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos.get(i).getLssue_isend());
                    topicsDetailsPojo.setLssue_name(EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos.get(i).getLssue_name());
                    topicsDetailsPojo.setLssue_order(EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos.get(i).getLssue_order());
                    topicsDetailsPojo.setMeeting_id(EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos.get(i).getMeeting_id());
                    topicsDetailsPojo.setPosition(EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos.get(i).getPosition());
                    topicsDetailsPojo.setUser_name(EssentialinformationDetailsFgt.essentialinformationDetailsLssuePojos.get(i).getUser_name());
                    topicsDetailsPojo.setRole_id(kv.decodeString("role_id"));
                    topicsDetailsPojos.add(topicsDetailsPojo);
                }
            }else{
                return topicsDetailsPojos;
            }
        }
        return topicsDetailsPojos;
    }

    /**
     * 设置RecyclerView空数据页面
     * @param quickAdapter
     */
    public void setEmptyView(BaseQuickAdapter quickAdapter) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_assets_null, null, false);
        quickAdapter.setEmptyView(view);
    }

}
