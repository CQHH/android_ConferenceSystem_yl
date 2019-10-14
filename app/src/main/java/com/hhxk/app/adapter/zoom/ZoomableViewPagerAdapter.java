package com.hhxk.app.adapter.zoom;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.hhxk.app.R;
import com.hhxk.app.view.zoomable.DoubleTapGestureListener;
import com.hhxk.app.view.zoomable.ZoomableDraweeView;

import java.util.ArrayList;

/**
 * @title  图片查看页面（缩放，左右滑动）适配器
 * @date   2019/03/11
 * @author enmaoFu
 */
public class ZoomableViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> mPaths;

    public ZoomableViewPagerAdapter(Context context, ArrayList<String> paths) {
        mContext = context;
        mPaths = paths;
    }

    @Override
    public int getCount() {
        return mPaths != null ? mPaths.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.zoomable_view_pager_item, null);
        ZoomableDraweeView zoomableDraweeView = (ZoomableDraweeView) view.findViewById(R.id.zoomable_image);
        //允许缩放时切换
        zoomableDraweeView.setAllowTouchInterceptionWhileZoomed(true);
        //长按
        zoomableDraweeView.setIsLongpressEnabled(false);
        //双击击放大或缩小
        zoomableDraweeView.setTapListener(new DoubleTapGestureListener(zoomableDraweeView));

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(mPaths.get(position))
                .build();
        //加载图片
        zoomableDraweeView.setController(draweeController);
        container.addView(view);
        view.requestLayout();
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        ZoomableDraweeView zoomableDraweeView = (ZoomableDraweeView) view.findViewById(R.id.zoomable_image);
        zoomableDraweeView.setController(null);
        container.removeView(view);
    }

}
