package com.hhxk.app.ui.details;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.MaterialDetailsAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.pojo.MaterialDetailsPojo;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @title  会议详情-会议详情嵌套的会议材料fragemnt
 * @date   2019/02/25
 * @author enmaoFu
 */
public class MaterialDetailsFgt extends BaseLazyFgt {

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
    private MaterialDetailsAdapter materialDetailsAdapter;

    /**
     * 数据源
     */
    private List<MaterialDetailsPojo> materialDetailsPojos;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query_details_material;
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
        mLayoutManager = new GridLayoutManager(getActivity(),3);
        //实例化适配器
        materialDetailsAdapter = new MaterialDetailsAdapter(R.layout.item_material_details, setData());
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
        recyclerView.setAdapter(materialDetailsAdapter);

    }

    @Override
    protected void requestData() {

    }

    public List<MaterialDetailsPojo> setData(){
        materialDetailsPojos = new ArrayList<>();
        MaterialDetailsPojo materialDetailsPojo = null;
        for(int i = 0; i < 20; i++){
            materialDetailsPojo = new MaterialDetailsPojo();
            materialDetailsPojos.add(materialDetailsPojo);
        }
        return materialDetailsPojos;
    }

}
