package com.em.baseframe.view.refresh;

import android.content.Context;
import android.graphics.Color;

import com.em.baseframe.R;
import com.em.baseframe.util.DensityUtils;
import com.em.refresh.PtrFrameLayout;
import com.em.refresh.header.StoreHouseHeader;

/**
 * @title  初始化设置下拉刷新
 * @date   2017/06/17
 * @author enmaoFu
 */
public class PtrInitHelper {

    public static void initPtr(Context mContext, final PtrFrameLayout mPtrFrameLayout) {

      /*  MaterialHeader header = new MaterialHeader(mContext);
        int[] colors = mContext.getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DensityUtils.dp2px(mContext, 20), 0, DensityUtils.dp2px(mContext, 20));
        header.setPtrFrameLayout(mPtrFrameLayout);

        mPtrFrameLayout.setLoadingMinTime(100);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.disableWhenHorizontalMove(true);*/
        StoreHouseHeader header = new StoreHouseHeader(mContext);
        header.setTextColor(Color.parseColor("#4B97FF"));
        header.setPadding(0, DensityUtils.dp2px(mContext, 20), 0, DensityUtils.dp2px(mContext, 10));
        header.initWithString("HH");
        mPtrFrameLayout.setDurationToCloseHeader(2500);
        mPtrFrameLayout.setLoadingMinTime(1200);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.disableWhenHorizontalMove(true);
    }

}
