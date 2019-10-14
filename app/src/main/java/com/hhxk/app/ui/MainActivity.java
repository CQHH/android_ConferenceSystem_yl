package com.hhxk.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.baseframe.config.UserInfoManger;
import com.em.baseframe.util.AppManger;
import com.em.baseframe.view.viewpager.CustomViewPager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hhxk.app.R;
import com.hhxk.app.adapter.viewpager.MainViewPagerAdapter;
import com.hhxk.app.base.BaseAty;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.event.PreservationEvent;
import com.hhxk.app.ui.archives.ArchivesFgt;
import com.hhxk.app.ui.center.PersonalCenterFgt;
import com.hhxk.app.ui.home.HomeFgt;
import com.hhxk.app.ui.manager.ManagePersonFgt;
import com.hhxk.app.ui.manager.ManagerFgt;
import com.hhxk.app.ui.my.MyConferenceFgt;
import com.hhxk.app.ui.query.QueryConferenceFgt;
import com.hhxk.app.ui.sponsor.SponsorConferenceFgt;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseAty {

    /**
     * 记录是否有首次按键
     */
    private static boolean mBackKeyPressed = false;

    /**
     * 左边菜单的布局
     */
    @BindView(R.id.home_re)
    RelativeLayout homeRe;
    @BindView(R.id.sponsor_conference_re)
    RelativeLayout sponsorConferenceRe;
    @BindView(R.id.query_conference_re)
    RelativeLayout queryConferenceRe;
    @BindView(R.id.my_conference_re)
    RelativeLayout myConferenceRe;
    @BindView(R.id.archives_re)
    RelativeLayout archivesRe;
    @BindView(R.id.up_re)
    RelativeLayout upRe;
    @BindView(R.id.personal_center_re)
    RelativeLayout personalCenterRe;

    @BindView(R.id.home_text)
    TextView homeText;
    @BindView(R.id.sponsor_conference_text)
    TextView sponsorConferenceText;
    @BindView(R.id.query_conference_text)
    TextView queryConferenceText;
    @BindView(R.id.my_conference_text)
    TextView myConferenceText;
    @BindView(R.id.archives_text)
    TextView archivesText;
    @BindView(R.id.up_text)
    TextView upText;
    @BindView(R.id.personal_center_text)
    TextView personalCenterText;

    @BindView(R.id.home_view)
    View homeView;
    @BindView(R.id.sponsor_conference_view)
    View sponsorConferenceView;
    @BindView(R.id.query_conference_view)
    View queryConferenceView;
    @BindView(R.id.my_conference_view)
    View myConferenceView;
    @BindView(R.id.archives_view)
    View archivesView;
    @BindView(R.id.up_view)
    View upView;
    @BindView(R.id.personal_center_view)
    View personalCenterView;

    @BindView(R.id.home_img)
    ImageView homeImg;
    @BindView(R.id.sponsor_conference_img)
    ImageView sponsorConferenceImg;
    @BindView(R.id.query_conference_img)
    ImageView queryConferenceImg;
    @BindView(R.id.my_conference_img)
    ImageView myConferenceImg;
    @BindView(R.id.archives_img)
    ImageView archivesImg;
    @BindView(R.id.up_img)
    ImageView upImg;
    @BindView(R.id.personal_center_img)
    ImageView personalCenterImg;

    @BindView(R.id.view_pager)
    CustomViewPager customViewPager;

    @BindView(R.id.img)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.date)
    TextView dateText;
    @BindView(R.id.user_text)
    TextView userText;

    private List<Fragment> fgts;
    private MainViewPagerAdapter mainViewPagerAdapter;

    private MMKV kv;
    private TimeThread timeThread;
    private boolean stopThread = false;
    private static final int MSG_ONE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //通过消息的内容msg.what  分别更新ui
            switch (msg.what) {
                case MSG_ONE:
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    dateText.setText(simpleDateFormat.format(date));
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        AppManger.getInstance().killActivity(LoginActivity.class);
        EventBus.getDefault().register(this);
        kv = MMKV.defaultMMKV();

        simpleDraweeView.setImageURI((new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.user_img)).build());
        timeThread = new TimeThread();
        timeThread.start();
        userText.setText(kv.decodeString("user_name"));

        //把Fragment添加到List集合里面
        fgts = new ArrayList<>();
        fgts.add(new HomeFgt());
        fgts.add(new SponsorConferenceFgt());
        fgts.add(new QueryConferenceFgt());
        fgts.add(new MyConferenceFgt());
        fgts.add(new ArchivesFgt());
        fgts.add(new ManagerFgt());
        fgts.add(new PersonalCenterFgt());
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fgts);
        customViewPager.setAdapter(mainViewPagerAdapter);
        customViewPager.setOffscreenPageLimit(7);
        customViewPager.setCurrentItem(0);//初始化显示第一个页面
        homeRe.setBackgroundColor(Color.parseColor("#0A2953"));
        sponsorConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
        queryConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
        myConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
        archivesRe.setBackgroundColor(Color.parseColor("#071D3F"));
        upRe.setBackgroundColor(Color.parseColor("#071D3F"));
        personalCenterRe.setBackgroundColor(Color.parseColor("#071D3F"));

        homeView.setVisibility(View.VISIBLE);
        sponsorConferenceView.setVisibility(View.GONE);
        queryConferenceView.setVisibility(View.GONE);
        myConferenceView.setVisibility(View.GONE);
       archivesView.setVisibility(View.GONE);
        upView.setVisibility(View.GONE);
        personalCenterView.setVisibility(View.GONE);

        homeImg.setImageDrawable(getResources().getDrawable(R.drawable.home_b));
        sponsorConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.sponsor_h));
        queryConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.query_h));
        myConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.my_h));
        //archivesImg.setImageDrawable(getResources().getDrawable(R.drawable.all_h));
        upImg.setImageDrawable(getResources().getDrawable(R.drawable.mng_h));
        personalCenterImg.setImageDrawable(getResources().getDrawable(R.drawable.personal_h));

        homeText.setTextColor(Color.parseColor("#ffffff"));
        sponsorConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
        queryConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
        myConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
        //archivesText.setTextColor(Color.parseColor("#9FA7AE"));
        upText.setTextColor(Color.parseColor("#9FA7AE"));
        personalCenterText.setTextColor(Color.parseColor("#9FA7AE"));

    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    protected void requestData() {

    }

    @Override
    @OnClick({R.id.home_re,R.id.sponsor_conference_re,R.id.query_conference_re,
              R.id.my_conference_re,R.id.up_re,R.id.archives_re,R.id.personal_center_re,R.id.logout})
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.home_re:

                EventBus.getDefault().post(new ConferenceEvent("close"));
                customViewPager.setCurrentItem(0);

                homeRe.setBackgroundColor(Color.parseColor("#0A2953"));
                sponsorConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                queryConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                myConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                archivesRe.setBackgroundColor(Color.parseColor("#071D3F"));
                upRe.setBackgroundColor(Color.parseColor("#071D3F"));
                personalCenterRe.setBackgroundColor(Color.parseColor("#071D3F"));

                homeView.setVisibility(View.VISIBLE);
                sponsorConferenceView.setVisibility(View.GONE);
                queryConferenceView.setVisibility(View.GONE);
                myConferenceView.setVisibility(View.GONE);
                archivesView.setVisibility(View.GONE);
                upView.setVisibility(View.GONE);
                personalCenterView.setVisibility(View.GONE);

                homeImg.setImageDrawable(getResources().getDrawable(R.drawable.home_b));
                sponsorConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.sponsor_h));
                queryConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.query_h));
                myConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.my_h));
                archivesImg.setImageDrawable(getResources().getDrawable(R.drawable.all_h));
                upImg.setImageDrawable(getResources().getDrawable(R.drawable.mng_h));
                personalCenterImg.setImageDrawable(getResources().getDrawable(R.drawable.personal_h));

                homeText.setTextColor(Color.parseColor("#ffffff"));
                sponsorConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                queryConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                myConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                archivesText.setTextColor(Color.parseColor("#9FA7AE"));
                upText.setTextColor(Color.parseColor("#9FA7AE"));
                personalCenterText.setTextColor(Color.parseColor("#9FA7AE"));

                break;
            case R.id.sponsor_conference_re:

                if("2".equals(kv.decodeString("role_id"))){
                    showErrorToast("无此操作权限");
                }else {

                    EventBus.getDefault().post(new ConferenceEvent("close"));
                    customViewPager.setCurrentItem(1);

                    homeRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    sponsorConferenceRe.setBackgroundColor(Color.parseColor("#0A2953"));
                    queryConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    myConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    archivesRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    upRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    personalCenterRe.setBackgroundColor(Color.parseColor("#071D3F"));

                    homeView.setVisibility(View.GONE);
                    sponsorConferenceView.setVisibility(View.VISIBLE);
                    queryConferenceView.setVisibility(View.GONE);
                    myConferenceView.setVisibility(View.GONE);
                    archivesView.setVisibility(View.GONE);
                    upView.setVisibility(View.GONE);
                    personalCenterView.setVisibility(View.GONE);

                    homeImg.setImageDrawable(getResources().getDrawable(R.drawable.home_h));
                    sponsorConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.sponsor_b));
                    queryConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.query_h));
                    myConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.my_h));
                    archivesImg.setImageDrawable(getResources().getDrawable(R.drawable.all_h));
                    upImg.setImageDrawable(getResources().getDrawable(R.drawable.mng_h));
                    personalCenterImg.setImageDrawable(getResources().getDrawable(R.drawable.personal_h));

                    homeText.setTextColor(Color.parseColor("#9FA7AE"));
                    sponsorConferenceText.setTextColor(Color.parseColor("#ffffff"));
                    queryConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                    myConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                    archivesText.setTextColor(Color.parseColor("#9FA7AE"));
                    upText.setTextColor(Color.parseColor("#9FA7AE"));
                    personalCenterText.setTextColor(Color.parseColor("#9FA7AE"));
                }

                break;
            case R.id.query_conference_re:

                EventBus.getDefault().post(new ConferenceEvent("close"));
                customViewPager.setCurrentItem(2);

                homeRe.setBackgroundColor(Color.parseColor("#071D3F"));
                sponsorConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                queryConferenceRe.setBackgroundColor(Color.parseColor("#0A2953"));
                myConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                archivesRe.setBackgroundColor(Color.parseColor("#071D3F"));
                upRe.setBackgroundColor(Color.parseColor("#071D3F"));
                personalCenterRe.setBackgroundColor(Color.parseColor("#071D3F"));

                homeView.setVisibility(View.GONE);
                sponsorConferenceView.setVisibility(View.GONE);
                queryConferenceView.setVisibility(View.VISIBLE);
                myConferenceView.setVisibility(View.GONE);
                archivesView.setVisibility(View.GONE);
                upView.setVisibility(View.GONE);
                personalCenterView.setVisibility(View.GONE);

                homeImg.setImageDrawable(getResources().getDrawable(R.drawable.home_h));
                sponsorConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.sponsor_h));
                queryConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.query_b));
                myConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.my_h));
                archivesImg.setImageDrawable(getResources().getDrawable(R.drawable.all_h));
                upImg.setImageDrawable(getResources().getDrawable(R.drawable.mng_h));
                personalCenterImg.setImageDrawable(getResources().getDrawable(R.drawable.personal_h));

                homeText.setTextColor(Color.parseColor("#9FA7AE"));
                sponsorConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                queryConferenceText.setTextColor(Color.parseColor("#ffffff"));
                myConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                archivesText.setTextColor(Color.parseColor("#9FA7AE"));
                upText.setTextColor(Color.parseColor("#9FA7AE"));
                personalCenterText.setTextColor(Color.parseColor("#9FA7AE"));

                break;
            case R.id.my_conference_re:

                EventBus.getDefault().post(new ConferenceEvent("close"));
                customViewPager.setCurrentItem(3);

                homeRe.setBackgroundColor(Color.parseColor("#071D3F"));
                sponsorConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                queryConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                myConferenceRe.setBackgroundColor(Color.parseColor("#0A2953"));
                archivesRe.setBackgroundColor(Color.parseColor("#071D3F"));
                upRe.setBackgroundColor(Color.parseColor("#071D3F"));
                personalCenterRe.setBackgroundColor(Color.parseColor("#071D3F"));

                homeView.setVisibility(View.GONE);
                sponsorConferenceView.setVisibility(View.GONE);
                queryConferenceView.setVisibility(View.GONE);
                myConferenceView.setVisibility(View.VISIBLE);
                archivesView.setVisibility(View.GONE);
                upView.setVisibility(View.GONE);
                personalCenterView.setVisibility(View.GONE);

                homeImg.setImageDrawable(getResources().getDrawable(R.drawable.home_h));
                sponsorConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.sponsor_h));
                queryConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.query_h));
                myConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.my_b));
                archivesImg.setImageDrawable(getResources().getDrawable(R.drawable.all_h));
                upImg.setImageDrawable(getResources().getDrawable(R.drawable.mng_h));
                personalCenterImg.setImageDrawable(getResources().getDrawable(R.drawable.personal_h));

                homeText.setTextColor(Color.parseColor("#9FA7AE"));
                sponsorConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                queryConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                myConferenceText.setTextColor(Color.parseColor("#ffffff"));
                archivesText.setTextColor(Color.parseColor("#9FA7AE"));
                upText.setTextColor(Color.parseColor("#9FA7AE"));
                personalCenterText.setTextColor(Color.parseColor("#9FA7AE"));

                break;
            case R.id.archives_re:

                EventBus.getDefault().post(new ConferenceEvent("close"));
                customViewPager.setCurrentItem(4);

                homeRe.setBackgroundColor(Color.parseColor("#071D3F"));
                sponsorConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                queryConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                myConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                archivesRe.setBackgroundColor(Color.parseColor("#0A2953"));
                upRe.setBackgroundColor(Color.parseColor("#071D3F"));
                personalCenterRe.setBackgroundColor(Color.parseColor("#071D3F"));

                homeView.setVisibility(View.GONE);
                sponsorConferenceView.setVisibility(View.GONE);
                queryConferenceView.setVisibility(View.GONE);
                myConferenceView.setVisibility(View.GONE);
                archivesView.setVisibility(View.VISIBLE);
                upView.setVisibility(View.GONE);
                personalCenterView.setVisibility(View.GONE);

                homeImg.setImageDrawable(getResources().getDrawable(R.drawable.home_h));
                sponsorConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.sponsor_h));
                queryConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.query_h));
                myConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.my_h));
                archivesImg.setImageDrawable(getResources().getDrawable(R.drawable.all_b));
                upImg.setImageDrawable(getResources().getDrawable(R.drawable.mng_h));
                personalCenterImg.setImageDrawable(getResources().getDrawable(R.drawable.personal_h));

                homeText.setTextColor(Color.parseColor("#9FA7AE"));
                sponsorConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                queryConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                myConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                archivesText.setTextColor(Color.parseColor("#ffffff"));
                upText.setTextColor(Color.parseColor("#9FA7AE"));
                personalCenterText.setTextColor(Color.parseColor("#9FA7AE"));

                break;
            case R.id.up_re:

                if("2".equals(kv.decodeString("role_id"))){
                    showErrorToast("无此操作权限");
                }else {

                    EventBus.getDefault().post(new ConferenceEvent("close"));
                    customViewPager.setCurrentItem(5);

                    homeRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    sponsorConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    queryConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    myConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    archivesRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    upRe.setBackgroundColor(Color.parseColor("#0A2953"));
                    personalCenterRe.setBackgroundColor(Color.parseColor("#071D3F"));

                    homeView.setVisibility(View.GONE);
                    sponsorConferenceView.setVisibility(View.GONE);
                    queryConferenceView.setVisibility(View.GONE);
                    myConferenceView.setVisibility(View.GONE);
                    archivesView.setVisibility(View.GONE);
                    upView.setVisibility(View.VISIBLE);
                    personalCenterView.setVisibility(View.GONE);

                    homeImg.setImageDrawable(getResources().getDrawable(R.drawable.home_h));
                    sponsorConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.sponsor_h));
                    queryConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.query_h));
                    myConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.my_h));
                    archivesImg.setImageDrawable(getResources().getDrawable(R.drawable.all_h));
                    upImg.setImageDrawable(getResources().getDrawable(R.drawable.mng_b));
                    personalCenterImg.setImageDrawable(getResources().getDrawable(R.drawable.personal_h));

                    homeText.setTextColor(Color.parseColor("#9FA7AE"));
                    sponsorConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                    queryConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                    myConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                    archivesText.setTextColor(Color.parseColor("#9FA7AE"));
                    upText.setTextColor(Color.parseColor("#ffffff"));
                    personalCenterText.setTextColor(Color.parseColor("#9FA7AE"));
                }

                break;
            case R.id.personal_center_re:

                    EventBus.getDefault().post(new ConferenceEvent("close"));
                    customViewPager.setCurrentItem(6);

                    homeRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    sponsorConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    queryConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    myConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    archivesRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    upRe.setBackgroundColor(Color.parseColor("#071D3F"));
                    personalCenterRe.setBackgroundColor(Color.parseColor("#0A2953"));

                    homeView.setVisibility(View.GONE);
                    sponsorConferenceView.setVisibility(View.GONE);
                    queryConferenceView.setVisibility(View.GONE);
                    myConferenceView.setVisibility(View.GONE);
                    archivesView.setVisibility(View.GONE);
                    upView.setVisibility(View.GONE);
                    personalCenterView.setVisibility(View.VISIBLE);

                    homeImg.setImageDrawable(getResources().getDrawable(R.drawable.home_h));
                    sponsorConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.sponsor_h));
                    queryConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.query_h));
                    myConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.my_h));
                    archivesImg.setImageDrawable(getResources().getDrawable(R.drawable.all_h));
                    upImg.setImageDrawable(getResources().getDrawable(R.drawable.mng_h));
                    personalCenterImg.setImageDrawable(getResources().getDrawable(R.drawable.personal_b));

                    homeText.setTextColor(Color.parseColor("#9FA7AE"));
                    sponsorConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                    queryConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                    myConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                    archivesText.setTextColor(Color.parseColor("#9FA7AE"));
                    upText.setTextColor(Color.parseColor("#9FA7AE"));
                    personalCenterText.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.logout:
                //点击弹出对话框，选择拍照或者系统相册
                new AlertDialog.Builder(this).setTitle("是否立即退出当前账号?")//设置对话框标题
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //退出登录
                                setHasAnimiation(false);
                                MMKV kv = MMKV.defaultMMKV();
                                kv.encode("isLogin", false);
                                //姓名
                                kv.encode("user_name", "");
                                //职务
                                kv.encode("department_position","");
                                //部门
                                kv.encode("department_name", "");
                                //权限名
                                kv.encode("role_name", "");
                                //用户ID
                                kv.encode("user_id", "");
                                //权限ID
                                kv.encode("role_id", "");
                                //用户账号
                                kv.encode("user_account", "");
                                //发起会议id标识
                                kv.encode("meetingId",00);
                                AppManger.getInstance().killAllActivity();
                                System.exit(0);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
        }
    }

    /**
     * 通知Handler每秒获取一次时间
     */
    class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();
            while (!stopThread) {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = MSG_ONE;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(PreservationEvent preservationEvent){
        switch (preservationEvent.getCode()){
            case "保存":
                customViewPager.setCurrentItem(3);

                homeRe.setBackgroundColor(Color.parseColor("#071D3F"));
                sponsorConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                queryConferenceRe.setBackgroundColor(Color.parseColor("#071D3F"));
                myConferenceRe.setBackgroundColor(Color.parseColor("#0A2953"));
                //archivesRe.setBackgroundColor(Color.parseColor("#071D3F"));
                upRe.setBackgroundColor(Color.parseColor("#071D3F"));
                personalCenterRe.setBackgroundColor(Color.parseColor("#071D3F"));

                homeView.setVisibility(View.GONE);
                sponsorConferenceView.setVisibility(View.GONE);
                queryConferenceView.setVisibility(View.GONE);
                myConferenceView.setVisibility(View.VISIBLE);
                //archivesView.setVisibility(View.GONE);
                upView.setVisibility(View.GONE);
                personalCenterView.setVisibility(View.GONE);

                homeImg.setImageDrawable(getResources().getDrawable(R.drawable.home_h));
                sponsorConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.sponsor_h));
                queryConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.query_h));
                myConferenceImg.setImageDrawable(getResources().getDrawable(R.drawable.my_b));
                //archivesImg.setImageDrawable(getResources().getDrawable(R.drawable.all_h));
                upImg.setImageDrawable(getResources().getDrawable(R.drawable.mng_h));
                personalCenterImg.setImageDrawable(getResources().getDrawable(R.drawable.personal_h));

                homeText.setTextColor(Color.parseColor("#9FA7AE"));
                sponsorConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                queryConferenceText.setTextColor(Color.parseColor("#9FA7AE"));
                myConferenceText.setTextColor(Color.parseColor("#ffffff"));
                //archivesText.setTextColor(Color.parseColor("#9FA7AE"));
                upText.setTextColor(Color.parseColor("#9FA7AE"));
                personalCenterText.setTextColor(Color.parseColor("#9FA7AE"));
                break;
        }
    }

    /**
     * 监听后退键，点击两次退出APP
     */
    @Override
    public void onBackPressed() {
        if(!mBackKeyPressed){
            showToast("再按一次退出应用");
            mBackKeyPressed = true;
            //延时两秒，如果超出则擦除第一次按键记录
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        }else{
            //退出程序
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThread = true;
    }
}
