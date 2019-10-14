package com.hhxk.app.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.hhxk.app.R;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.base.BaseLazyFragment;
import com.hhxk.app.event.ConferenceEvent;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @title  会议详情-会议详情嵌套的fragemnt
 * @date   2019/02/21
 * @author enmaoFu
 */
public class ConferenceDetailsFgt extends BaseLazyFragment{

    private TabLayout mTab;
    private ViewPager mViewPager;
    private ImageView backImg;

    /**
     * Fragment页面集合
     */
    private List<BaseLazyFgt> mFragments;

    /**
     * Tab切换卡名字集合
     */
    private List<String> mTabsString;

    private MMKV kv;

    @Override
    public void initBundle(@NonNull Bundle bundle) {

    }

    @Override
    protected void initView() {
        mTab = findView(R.id.tab_layout, false);
        mViewPager = findView(R.id.view_pager, false);
        backImg = findView(R.id.back_img,true);
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();

        mFragments = new ArrayList<>();
        mTabsString = new ArrayList<>();
        mTabsString.add("基本信息");
        mTabsString.add("参会人员");
        mTabsString.add("会议议题");
        mTabsString.add("会议纪要");
        /*mTabsString.add("会议材料");
        mTabsString.add("审批文件");*/

        mFragments.add(new EssentialinformationDetailsFgt());
        mFragments.add(new ParticipantsDetailsFgt());
        mFragments.add(new TopicsDetailsFgt());
        mFragments.add(new RecordDetailsFgt());
        /*mFragments.add(new MaterialDetailsFgt());
        mFragments.add(new ApprovalDocumentDetailsFgt());*/

        ConferenceDetailsFgt.pageAdapter pageAdapter = new ConferenceDetailsFgt.pageAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(pageAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_img:
                EventBus.getDefault().post(new ConferenceEvent("close"));
                kv.encode("meeting_id_details",00);
                break;
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query_conference_details;
    }

    class pageAdapter extends FragmentStatePagerAdapter {
        public pageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsString.get(position);
        }

    }

}
