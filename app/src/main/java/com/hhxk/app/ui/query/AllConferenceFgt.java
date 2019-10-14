package com.hhxk.app.ui.query;

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
import com.hhxk.app.adapter.AllConferenceAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.AllConferencePojo;
import com.hhxk.app.pojo.ManagePersonPojo;
import com.tencent.mmkv.MMKV;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  所有会议-全部fragemnt
 * @date   2019/02/21
 * @author enmaoFu
 */
public class AllConferenceFgt extends BaseLazyFgt {

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
    private AllConferenceAdapter allConferenceAdapter;

    /**
     * 数据源
     */
    private List<AllConferencePojo> allConferencePojos;

    /**
     * 每页条数
     */
    private int pageSize = 3;

    /**
     * 当前页数
     */
    private int pager = 1;

    private MMKV kv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query_conference_all;
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();

        /**
         * 下拉刷新
         */
        PtrInitHelper.initPtr(getActivity(), ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pager = 1;
                doHttp(RetrofitUtils.createApi(Http.class).allMeeting(pager),1);
            }
        });

        //实例化布局管理器
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        //实例化适配器
        allConferenceAdapter = new AllConferenceAdapter(R.layout.item_query_conference_all, new ArrayList<AllConferencePojo>());
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
        setEmptyView(allConferenceAdapter);
        //设置adapter
        recyclerView.setAdapter(allConferenceAdapter);

        //上拉加载更多
        allConferenceAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (recyclerView != null)
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {

                            if (pager == 1) {
                                allConferenceAdapter.loadMoreEnd();
                                return;
                            }
                            doHttp(RetrofitUtils.createApi(Http.class).allMeeting(pager),2);
                        }
                    });

            }
        }, recyclerView);

        allConferenceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<AllConferencePojo>() {
            @Override
            public void onItemClick(BaseQuickAdapter<AllConferencePojo, ? extends BaseViewHolder> adapter, View view, int position) {
                EventBus.getDefault().post(new ConferenceEvent("open"));
                kv.encode("meeting_id_details",allConferenceAdapter.getItem(position).getMeeting_id());
                kv.encode("record","n");
            }
        });
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        doHttp(RetrofitUtils.createApi(Http.class).allMeeting(pager),1);
    }

    @Override
    protected boolean setIsInitRequestData() {
        return true;
    }

    @Override
    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).allMeeting(pager),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                ptrFrameLayout.refreshComplete();
                allConferenceAdapter.removeAll();
                allConferencePojos = AppJsonUtil.getArrayList(result,"content",AllConferencePojo.class);
                if (allConferencePojos != null) {
                    allConferenceAdapter.setNewData(allConferencePojos);
                    if (allConferencePojos.size() < pageSize) {
                        allConferenceAdapter.loadMoreEnd();
                    }
                } else {
                    allConferenceAdapter.loadMoreEnd();
                }
                //增加页码
                pager++;
                break;
            case 2:
                //加载更多
                allConferencePojos = AppJsonUtil.getArrayList(result,"content",AllConferencePojo.class);
                if (allConferencePojos != null && allConferencePojos.size() > 0) {
                    allConferenceAdapter.addData(allConferencePojos);
                    allConferenceAdapter.loadMoreComplete();
                } else {
                    allConferenceAdapter.loadMoreEnd();
                }
                //增加页码
                pager++;
                break;
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
}
