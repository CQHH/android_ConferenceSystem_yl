package com.hhxk.app.ui.sponsor;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.hhxk.app.R;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.event.SponsorClearEvent;
import com.hhxk.app.event.SponsorEvent;
import com.hhxk.app.ui.details.ConferenceDetailsFgt;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @title  发起会议fragemnt
 * @date   2019/02/18
 * @author enmaoFu
 */
public class SponsorConferenceFgt extends BaseLazyFgt {

    @BindView(R.id.tab_layout)
    TabLayout mTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

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
    protected int getLayoutId() {
        return R.layout.fragment_sponsor_conference;
    }

    @Override
    protected void initData() {

        EventBus.getDefault().register(this);
        kv = MMKV.defaultMMKV();

        mFragments = new ArrayList<>();
        mTabsString = new ArrayList<>();
        mTabsString.add("基本信息");
        mTabsString.add("参会人员");
        mTabsString.add("会议议题");

        mFragments.add(new EssentialinformationFgt());
        mFragments.add(new ParticipantsFgt());
        mFragments.add(new TopicsFgt());

        SponsorConferenceFgt.pageAdapter pageAdapter = new SponsorConferenceFgt.pageAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pageAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabMode(TabLayout.MODE_FIXED);
        setTabLayoutCanClick(false);

    }

    @Override
    protected boolean setIsInitRequestData() {
        return false;
    }

    @Override
    protected void requestData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(SponsorEvent sponsorEvent){
        switch (sponsorEvent.getCode()){
            case "基本信息-保存":
                mViewPager.setCurrentItem(0);
                EventBus.getDefault().post(new SponsorClearEvent("clear"));
                break;
            case "基本信息-下":
                mViewPager.setCurrentItem(1);
                kv.encode("meetingId",sponsorEvent.getMeetingId());
                break;
            case "参会人员-保存":
                mViewPager.setCurrentItem(0);
                EventBus.getDefault().post(new SponsorClearEvent("clear"));
                break;
            case "参会人员-下":
                mViewPager.setCurrentItem(2);
                break;
            case "完成":
                mViewPager.setCurrentItem(0);
                EventBus.getDefault().post(new SponsorClearEvent("clear"));
                break;
            case "参会人员-上":
                mViewPager.setCurrentItem(0);
                break;
            case "会议议题-上":
                mViewPager.setCurrentItem(1);
                break;
            case "会议材料-上":
                mViewPager.setCurrentItem(2);
                break;
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setTabLayoutCanClick(boolean canClick){
        LinearLayout tabStrip= (LinearLayout) mTab.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if(tabView !=null){
                tabView.setClickable(canClick);
            }
        }
    }
}
