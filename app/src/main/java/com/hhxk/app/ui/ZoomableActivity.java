package com.hhxk.app.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.hhxk.app.R;
import com.hhxk.app.adapter.zoom.ZoomableViewPagerAdapter;
import com.hhxk.app.base.BaseAty;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @title  图片查看页面（缩放，左右滑动）
 * @date   2019/03/11
 * @author enmaoFu
 */
public class ZoomableActivity extends BaseAty implements ViewPager.OnPageChangeListener{

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.zoomable_index)
    TextView zoomableIndex;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    /**
     * 传过来图片路径集合
     */
    private ArrayList<String> imagePath;

    /**
     * 传过来当前的图片下标
     */
    private int mIndex;

    /**
     * 传过来的标题
     */
    private String title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zoomable;
    }

    @Override
    protected void initData() {

        imagePath = getIntent().getStringArrayListExtra("imagePath");
        mIndex = getIntent().getIntExtra("mIndex", 0);
        title = getIntent().getStringExtra("title");

        initToolbar(mToolbar,title);

        viewPager.setAdapter(new ZoomableViewPagerAdapter(this, imagePath));
        viewPager.setCurrentItem(mIndex);
        zoomableIndex.setText(mIndex + 1 + "/" + imagePath.size());
        viewPager.addOnPageChangeListener(this);

    }

    @Override
    protected void requestData() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        zoomableIndex.setText(position + 1 + "/" + imagePath.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}