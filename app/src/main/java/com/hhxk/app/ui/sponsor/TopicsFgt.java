package com.hhxk.app.ui.sponsor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.dialog.FormBotomDefaultDialogBuilder;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.TopicsAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.PreservationEvent;
import com.hhxk.app.event.SponsorClearEvent;
import com.hhxk.app.event.SponsorEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.TopicsPojo;
import com.hhxk.app.ui.FileListActivity;
import com.hhxk.app.view.AddIssueDialog;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import net.bither.util.NativeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  发起会议-会议议题fragemnt
 * @date   2019/02/20
 * @author enmaoFu
 */
public class TopicsFgt extends BaseLazyFgt {

    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private TopicsAdapter topicsAdapter;

    /**
     * 数据源
     */
    private List<TopicsPojo> topicsPojos;

    private AddIssueDialog addIssueDialog;

    private AddIssueDialog edIssueDialog;

    private MMKV kv;

    /**
     * 删除弹框
     */
    private DialogInterface deleteDialog;

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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topics;
    }

    @Override
    protected void initData() {

        EventBus.getDefault().register(this);
        kv = MMKV.defaultMMKV();

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        topicsAdapter = new TopicsAdapter(R.layout.item_topics, new ArrayList<TopicsPojo>());
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
        setEmptyView(topicsAdapter);
        //设置adapter
        recyclerView.setAdapter(topicsAdapter);

        topicsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<TopicsPojo>() {
            @Override
            public void onItemChildClick(BaseQuickAdapter<TopicsPojo, ? extends BaseViewHolder> adapter, View view, final int position) {
                switch (view.getId()){
                    case R.id.delete:
                        deletePo = position;
                        new AlertDialog.Builder(getActivity()).setTitle("是否删除该议题?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteDialog = dialog;
                                        int lssueId = topicsAdapter.getItem(position).getLssue_id();
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
                        final String titleRv = topicsAdapter.getItem(position).getLssue_name();
                        final String nameRv = topicsAdapter.getItem(position).getUser_name();
                        final String poRv = topicsAdapter.getItem(position).getPosition();
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
                                    doHttp(RetrofitUtils.createApi(Http.class).changeLssue(topicsAdapter.getItem(position).getLssue_id(),
                                            title,name,po),3);
                                }
                            }
                        });
                        break;
                    case R.id.up_img:
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Http.class).moveUpward(String.valueOf(kv.decodeInt("meetingId")),
                                topicsAdapter.getItem(position).getLssue_id(),topicsAdapter.getItem(position).getLssue_order()),4);
                        break;
                    case R.id.down_img:
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Http.class).moveDown(String.valueOf(kv.decodeInt("meetingId")),
                                topicsAdapter.getItem(position).getLssue_id(),topicsAdapter.getItem(position).getLssue_order()),5);
                        break;
                    case R.id.up:
                        String lssusId = String.valueOf(topicsAdapter.getItem(position).getLssue_id());
                        Bundle bundle = new Bundle();
                        bundle.putString("lssusId",lssusId);
                        bundle.putString("roleId","1");
                        startActivity(FileListActivity.class,bundle);
                        break;
                }
            }
        });

    }

    @Override
    protected void requestData() {

    }

    @OnClick({R.id.add,R.id.back,R.id.success_btn})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.add:
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
                            doHttp(RetrofitUtils.createApi(Http.class).addLssue(kv.decodeInt("meetingId"),
                                    title,name,po),1);
                        }
                    }
                });
                break;
            case R.id.back:
                SponsorEvent se = new SponsorEvent();
                se.setCode("会议议题-上");
                EventBus.getDefault().post(se);
                break;
            case R.id.success_btn:
                if(topicsAdapter.getData().size() == 0){
                    showErrorToast("请添加会议议题");
                }else{
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Http.class).generateMeeting(kv.decodeInt("meetingId")),6);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                topicsPojos = AppJsonUtil.getArrayList(result,TopicsPojo.class);
                topicsAdapter.setNewData(topicsPojos);
                showToast("添加成功");
                addIssueDialog.dismiss();
                break;
            case 2:
                topicsAdapter.remove(deletePo);
                deleteDialog.dismiss();
                break;
            case 3:
                TopicsPojo topicsPojo = topicsAdapter.getItem(uppo);
                topicsPojo.setLssue_name(title);
                topicsPojo.setUser_name(name);
                topicsPojo.setPosition(po);
                topicsAdapter.setData(uppo,topicsPojo);
                showToast("修改成功");
                edIssueDialog.dismiss();
                break;
            case 4:
                topicsPojos = AppJsonUtil.getArrayList(result,TopicsPojo.class);
                topicsAdapter.setNewData(topicsPojos);
                showToast("更改议题顺序成功");
                break;
            case 5:
                topicsPojos = AppJsonUtil.getArrayList(result,TopicsPojo.class);
                topicsAdapter.setNewData(topicsPojos);
                showToast("更改议题顺序成功");
                break;
            case 6:
                showToast("保存会议议题成功");
                SponsorEvent sponsorEvent = new SponsorEvent();
                sponsorEvent.setCode("完成");
                EventBus.getDefault().post(sponsorEvent);
                PreservationEvent pe = new PreservationEvent();
                pe.setCode("保存");
                EventBus.getDefault().post(pe);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(SponsorClearEvent sponsorClearEvent){
        if(sponsorClearEvent.getClear().equals("clear")){
            topicsAdapter.removeAll();
            topicsPojos.clear();
            kv.encode("meetingId",00);
        }
    }

    /**
     * 设置RecyclerView空数据页面
     * @param quickAdapter
     */
    public void setEmptyView(BaseQuickAdapter quickAdapter) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_assets_null, null, false);
        quickAdapter.setEmptyView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        kv.encode("meetingId",00);
    }
}
