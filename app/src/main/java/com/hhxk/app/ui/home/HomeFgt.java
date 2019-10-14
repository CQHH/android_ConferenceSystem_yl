package com.hhxk.app.ui.home;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.adapter.recyclerview.listener.OnItemChildClickListener;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.DateTool;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.HomeAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.HomeHeadPojo;
import com.hhxk.app.pojo.HomeItemPojo;
import com.hhxk.app.pojo.HomeNewPojo;
import com.hhxk.app.pojo.HomePojo;
import com.hhxk.app.ui.details.ConferenceDetailsFgt;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

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
 * @title  系统首页fragemnt
 * @date   2019/02/18
 * @author enmaoFu
 */
public class HomeFgt extends BaseLazyFgt {

    @BindView(R.id.refresh)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;
    @BindView(R.id.text_z)
    TextView textZ;
    @BindView(R.id.text_y)
    TextView textY;
    @BindView(R.id.text_all)
    TextView textAll;
    @BindView(R.id.fra)
    FrameLayout contentf;
    @BindView(R.id.lin)
    LinearLayout lin;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private HomeAdapter homeAdapter;

    /**
     * 数据源
     */
    private ArrayList<HomeHeadPojo> homeHeadPojos;

    private MMKV kv;

    private List<HomePojo> homePojos;

    private List<HomeNewPojo> homeNewPojos;

    private String hostStr = "暂无";

    private FragmentManager fm;

    private FragmentTransaction ft;

    private ConferenceDetailsFgt conferenceDetailsFgt;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();
        EventBus.getDefault().register(this);
        conferenceDetailsFgt = new ConferenceDetailsFgt();
        fm = getChildFragmentManager();

        /**
         * 下拉刷新
         */
        PtrInitHelper.initPtr(getActivity(), ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                doHttp(RetrofitUtils.createApi(Http.class).indexInfo(kv.decodeString("user_id")),1);
            }
        });

        //实例化布局管理器
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //实例化适配器
        homeAdapter = new HomeAdapter(R.layout.item_home, R.layout.item_home_head, new ArrayList<HomeHeadPojo>());
        //设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        //设置间隔样式
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor("#E8E8E8"))
                        .sizeResId(R.dimen.recyclerview_item_hr)
                        .build());
        //大小不受适配器影响
        recyclerView.setHasFixedSize(true);
        //设置加载动画类型
        homeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置删除动画类型
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置adapter
        recyclerView.setAdapter(homeAdapter);

        //设置item点击事件
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<HomeHeadPojo>() {
            @Override
            public void onItemClick(BaseQuickAdapter<HomeHeadPojo, ? extends BaseViewHolder> adapter, View view, int position) {
                int meetingId;
                try {
                    meetingId = homeAdapter.getItem(position).t.getMeeting_id();
                }catch (NullPointerException e){
                    return;
                }
                EventBus.getDefault().post(new ConferenceEvent("open"));
                kv.encode("meeting_id_details",meetingId);
                kv.encode("record","n");
            }
        });
    }

    @Override
    protected boolean setIsInitRequestData() {
        return true;
    }

    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).indexInfo(kv.decodeString("user_id")),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                homePojos = AppJsonUtil.getArrayList(result,"meeting",HomePojo.class);
                homeNewPojos = AppJsonUtil.getArrayList(result,"meetingList",HomeNewPojo.class);

                textZ.setText(String.valueOf(homePojos.get(0).getMeeting_z()));
                textY.setText(String.valueOf(homePojos.get(0).getMeeting_y()));
                textAll.setText(String.valueOf(homePojos.get(0).getMeeting_all()));

                //分组必须的实体类集合
                List<HomeHeadPojo> homeHeadPojos = new ArrayList<>();
                for(int j = 0; j < homeNewPojos.size(); j++) {
                    HomeHeadPojo homeHeadPojo = new HomeHeadPojo(true, "");
                    homeHeadPojo.setStrTitle(homeNewPojos.get(j).getTitle());
                    homeHeadPojos.add(homeHeadPojo);

                    //得到每个分组下面的Item
                    List<HomeNewPojo.InfoBean> infoBeans = homeNewPojos.get(j).getInfo();
                    HomeItemPojo headPojo = null;
                    for(int i = 0; i < infoBeans.size(); i++){
                        headPojo = new HomeItemPojo();
                        headPojo.setTitle(infoBeans.get(i).getMeeting_name());
                        headPojo.setDate(DateTool.timeStamp2Date(infoBeans.get(i).getMeeting_start(),"yyyy年MM月dd日 HH:mm"));
                        if(null != infoBeans.get(i).getUserList() || infoBeans.get(i).getUserList().size() != 0){
                            List<String> strings = infoBeans.get(i).getUserList();
                            for(String strs:strings){
                                hostStr = strs + ",";
                            }
                        }
                        headPojo.setPerson(hostStr);
                        headPojo.setIntroduction(infoBeans.get(i).getMeeting_info());
                        headPojo.setStrY(String.valueOf(infoBeans.get(i).getNum()));
                        headPojo.setStrQ(String.valueOf(infoBeans.get(i).getSig()));
                        headPojo.setStrD(String.valueOf(infoBeans.get(i).getNotArrive()));
                        headPojo.setMeeting_id(infoBeans.get(i).getMeeting_id());
                        //实体类里加上一个额外的下标，就是当前第几组
                        headPojo.setPo(j);
                        HomeHeadPojo homeHeadPojoLPojo = new HomeHeadPojo(headPojo);
                        homeHeadPojos.add(homeHeadPojoLPojo);
                    }
                    if (homeHeadPojos != null) {
                        homeAdapter.setNewData(homeHeadPojos);
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(ConferenceEvent conferenceEvent){
        switch (conferenceEvent.getCode()){
            case "open":
                contentf.setVisibility(View.VISIBLE);
                lin.setVisibility(View.GONE);
                ft = fm.beginTransaction();
                if(null == fm.findFragmentByTag("post")){
                    conferenceDetailsFgt = new ConferenceDetailsFgt();
                    ft.add(R.id.fra,conferenceDetailsFgt,"post");
                }else{
                    ft.show(conferenceDetailsFgt);
                }
                ft.commit();
                break;
            case "close":
                lin.setVisibility(View.VISIBLE);
                contentf.setVisibility(View.GONE);
                ft = fm.beginTransaction();
                ft.remove(conferenceDetailsFgt);
                ft.commit();
                break;
        }
    }
}
