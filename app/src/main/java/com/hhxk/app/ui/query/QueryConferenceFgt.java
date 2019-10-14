package com.hhxk.app.ui.query;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hhxk.app.R;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.ui.details.ConferenceDetailsFgt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @title  所有会议fragemnt
 * @date   2019/02/18
 * @author enmaoFu
 */
public class QueryConferenceFgt extends BaseLazyFgt {

    @BindView(R.id.tab_layout)
    TabLayout mTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.fra)
    FrameLayout contentf;
    @BindView(R.id.lin)
    LinearLayout lin;

    /**
     * Fragment页面集合
     */
    private List<BaseLazyFgt> mFragments;

    /**
     * Tab切换卡名字集合
     */
    private List<String> mTabsString;

    private FragmentManager fm;

    private FragmentTransaction ft;

    private ConferenceDetailsFgt conferenceDetailsFgt;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query_conference;
    }

    @Override
    protected void initData() {

        EventBus.getDefault().register(this);
        conferenceDetailsFgt = new ConferenceDetailsFgt();
        fm = getChildFragmentManager();

        mFragments = new ArrayList<>();
        mTabsString = new ArrayList<>();
        mTabsString.add("全部会议");
        mTabsString.add("未开始会议");
        mTabsString.add("正在进行会议");
        mTabsString.add("已结束会议");

        mFragments.add(new AllConferenceFgt());
        mFragments.add(new NotConferenceFgt());
        mFragments.add(new OngoinConferenceFgt());
        mFragments.add(new EndConferenceFgt());

        //QueryConferenceFgt.pageAdapter pageAdapter = new QueryConferenceFgt.pageAdapter(getActivity().getSupportFragmentManager());
        QueryConferenceFgt.pageAdapter pageAdapter = new QueryConferenceFgt.pageAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(pageAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
    }

    @Override
    protected void requestData() {

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
}
