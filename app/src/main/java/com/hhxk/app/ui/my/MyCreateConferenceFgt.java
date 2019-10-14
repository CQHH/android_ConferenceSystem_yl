package com.hhxk.app.ui.my;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.MyAllConferenceAdapter;
import com.hhxk.app.adapter.MyCreateConferenceAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.event.ConferenceUpEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.MyAllConferencePojo;
import com.hhxk.app.pojo.MyCreateConferencePojo;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  我的会议-我发起的会议fragemnt
 * @date   2019/02/21
 * @author enmaoFu
 */
public class MyCreateConferenceFgt extends BaseLazyFgt {

    @BindView(R.id.refresh)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private MyCreateConferenceAdapter myCreateConferenceAdapter;

    /**
     * 数据源
     */
    private List<MyCreateConferencePojo> myCreateConferencePojos;

    private MMKV kv;

    /**
     * 取消会议弹窗
     */
    private DialogInterface dialogInterface;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_create;
    }

    @Override
    protected void initData() {

        EventBus.getDefault().register(this);
        kv = MMKV.defaultMMKV();

        /**
         * 下拉刷新
         */
        PtrInitHelper.initPtr(getActivity(), ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                doHttp(RetrofitUtils.createApi(Http.class).createMeetingForMe(kv.decodeString("user_id")),1);
            }
        });

        //实例化布局管理器
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        //实例化适配器
        myCreateConferenceAdapter = new MyCreateConferenceAdapter(R.layout.item_my_create, new ArrayList<MyCreateConferencePojo>());
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
        setEmptyView(myCreateConferenceAdapter);
        //设置adapter
        recyclerView.setAdapter(myCreateConferenceAdapter);
        myCreateConferenceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<MyCreateConferencePojo>() {
            @Override
            public void onItemClick(BaseQuickAdapter<MyCreateConferencePojo, ? extends BaseViewHolder> adapter, View view, int position) {
                EventBus.getDefault().post(new ConferenceEvent("open"));
                kv.encode("meeting_id_details",myCreateConferenceAdapter.getItem(position).getMeeting_id());
                kv.encode("record","y");
                kv.encode("hy","n");
            }
        });
        myCreateConferenceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<MyCreateConferencePojo>() {
            @Override
            public void onItemChildClick(BaseQuickAdapter<MyCreateConferencePojo, ? extends BaseViewHolder> adapter, View view, final int position) {
                switch (view.getId()){
                    case R.id.clear:
                        //点击弹出对话框，选择拍照或者系统相册
                        new AlertDialog.Builder(getActivity()).setTitle("是否取消会议?")//设置对话框标题
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialogInterface = dialog;
                                        showLoadingDialog(null);
                                        doHttp(RetrofitUtils.createApi(Http.class).cancelMeeting(myCreateConferenceAdapter.getItem(position).getMeeting_id()),2);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
                        break;
                }
            }
        });

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        doHttp(RetrofitUtils.createApi(Http.class).createMeetingForMe(kv.decodeString("user_id")),1);
    }

    @Override
    protected boolean setIsInitRequestData() {
        return true;
    }

    @Override
    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).createMeetingForMe(kv.decodeString("user_id")),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                ptrFrameLayout.refreshComplete();
                myCreateConferencePojos = AppJsonUtil.getArrayList(result,MyCreateConferencePojo.class);
                myCreateConferenceAdapter.setNewData(myCreateConferencePojos);
                break;
            case 2:
                showToast("取消会议成功");
                doHttp(RetrofitUtils.createApi(Http.class).createMeetingForMe(kv.decodeString("user_id")),1);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(ConferenceUpEvent conferenceUpEvent){
        doHttp(RetrofitUtils.createApi(Http.class).createMeetingForMe(kv.decodeString("user_id")),1);
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
        EventBus.getDefault().unregister(this);
    }
}
