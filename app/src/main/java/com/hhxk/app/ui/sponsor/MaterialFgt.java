package com.hhxk.app.ui.sponsor;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.MaterialAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.SponsorEvent;
import com.hhxk.app.pojo.MaterialPojo;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @title  发起会议-会议材料fragemnt
 * @date   2019/02/20
 * @author enmaoFu
 */
public class MaterialFgt extends BaseLazyFgt {

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
    private MaterialAdapter materialAdapter;

    /**
     * 数据源
     */
    private List<MaterialPojo> materialPojos;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_material;
    }

    @Override
    protected void initData() {

        EventBus.getDefault().register(this);

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
        mLayoutManager = new GridLayoutManager(getActivity(),3);
        //实例化适配器
        materialAdapter = new MaterialAdapter(R.layout.item_material, setData());
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
        //设置adapter
        recyclerView.setAdapter(materialAdapter);

    }

    @Override
    protected void requestData() {

    }

    @OnClick({R.id.back})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.back:
                SponsorEvent se = new SponsorEvent();
                se.setCode("会议材料-上");
                EventBus.getDefault().post(se);
                break;
        }
    }

    public List<MaterialPojo> setData(){
        materialPojos = new ArrayList<>();
        MaterialPojo materialPojo = null;
        for(int i = 0; i < 20; i++){
            materialPojo = new MaterialPojo();
            materialPojos.add(materialPojo);
        }
        return materialPojos;
    }
}
