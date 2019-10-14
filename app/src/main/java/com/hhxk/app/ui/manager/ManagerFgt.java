package com.hhxk.app.ui.manager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hhxk.app.R;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ManagerDepartmentEvent;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @title  管理操作fragemnt
 * @date   2019/02/27
 * @author enmaoFu
 */
public class ManagerFgt extends BaseLazyFgt {

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

    private ManagerPostFgt managerPostFgt;

    private MMKV kv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_manager;
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();

        EventBus.getDefault().register(this);
        fm = getChildFragmentManager();

        mFragments = new ArrayList<>();
        mTabsString = new ArrayList<>();
        mTabsString.add("成员管理");
        mTabsString.add("部门管理");

        mFragments.add(new ManagePersonFgt());
        mFragments.add(new ManagerDepartmentFgt());

        ManagerFgt.pageAdapter pageAdapter = new ManagerFgt.pageAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(pageAdapter);
        mTab.newTab();
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    protected void requestData() {

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(ManagerDepartmentEvent managerDepartmentEvent){
        switch (managerDepartmentEvent.getCode()){
            case "post_open":
                kv.encode("manager_department_id", managerDepartmentEvent.getDepartmentId());
                contentf.setVisibility(View.VISIBLE);
                lin.setVisibility(View.GONE);
                ft = fm.beginTransaction();
                if(null == fm.findFragmentByTag("post")){
                    managerPostFgt = new ManagerPostFgt();
                    ft.add(R.id.fra,managerPostFgt,"post");
                }else{
                    ft.show(managerPostFgt);
                }
                ft.commit();
                break;
            case "post_close":
                kv.encode("manager_department_id", managerDepartmentEvent.getDepartmentId());
                lin.setVisibility(View.VISIBLE);
                contentf.setVisibility(View.GONE);
                ft = fm.beginTransaction();
                ft.remove(managerPostFgt);
                ft.commit();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
