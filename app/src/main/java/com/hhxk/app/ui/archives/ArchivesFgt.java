package com.hhxk.app.ui.archives;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.DateTool;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.ArchivesAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.AllConferencePojo;
import com.hhxk.app.pojo.ArchivesPojo;
import com.hhxk.app.ui.details.ConferenceDetailsFgt;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * @title  会议档案fragemnt
 * @date   2019/02/18
 * @author enmaoFu
 */
public class ArchivesFgt extends BaseLazyFgt {

    @BindView(R.id.refresh)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;
    @BindView(R.id.select_dep)
    Spinner selectDep;
    @BindView(R.id.input_ed)
    EditText inputEd;
    @BindView(R.id.start)
    TextView startDate;
    @BindView(R.id.end)
    TextView endDate;
    @BindView(R.id.fra)
    FrameLayout contentf;
    @BindView(R.id.lin)
    LinearLayout lin;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private ArchivesAdapter archivesAdapter;

    /**
     * 数据源
     */
    private List<ArchivesPojo> archivesPojos;

    /**
     * 下拉框数据源
     */
    private List<String> strList;

    /**
     * 选择时间
     */
    private TimePickerDialog startDataDialog;
    private TimePickerDialog endDataDialog;

    /**
     * 当前页数
     */
    private int pager = 1;

    /**
     * 每页条数
     */
    private int pageSize = 3;

    /**
     * fragment管理器
     */
    private FragmentManager fm;

    /**
     * fragment事务
     */
    private FragmentTransaction ft;

    /**
     * 添加的fragment
     */
    private ConferenceDetailsFgt conferenceDetailsFgt;

    /**
     * 代替SP的存储
     */
    private MMKV kv;

    /**
     * 删除会议弹框
     */
    private DialogInterface dialogInterface;

    /**
     * 删除会议的下标
     */
    private int po;

    /**
     * 下拉选择下标
     */
    private int selectInt = 1;

    /**
     * 查询关键词
     */
    private String getInput = "";

    /**
     * 查询起点时间
     */
    private String startDateStr = "";

    /**
     * 查询结束数时间
     */
    private String endDateStr = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_archives;
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();
        EventBus.getDefault().register(this);
        conferenceDetailsFgt = new ConferenceDetailsFgt();
        fm = getChildFragmentManager();

        /**
         * 下拉刷新
         */
        PtrInitHelper.initPtr(getActivity(), ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pager = 1;
                doHttp(RetrofitUtils.createApi(Http.class).searchInfoByType(selectInt,pager,startDateStr,endDateStr,getInput),1);
            }
        });

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        archivesAdapter = new ArchivesAdapter(R.layout.item_archives, setData());
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
        recyclerView.setAdapter(archivesAdapter);

        //上拉加载更多
        archivesAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (recyclerView != null)
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (pager == 1) {
                                archivesAdapter.loadMoreEnd();
                                return;
                            }
                            doHttp(RetrofitUtils.createApi(Http.class).searchInfoByType(selectInt,pager,startDateStr,endDateStr,getInput),2);
                        }
                    });

            }
        }, recyclerView);

        archivesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<ArchivesPojo>() {
            @Override
            public void onItemClick(BaseQuickAdapter<ArchivesPojo, ? extends BaseViewHolder> adapter, View view, int position) {
                EventBus.getDefault().post(new ConferenceEvent("open"));
                kv.encode("meeting_id_details",archivesAdapter.getItem(position).getMeeting_id());
                kv.encode("record","n");
            }
        });
        archivesAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<ArchivesPojo>() {
            @Override
            public void onItemChildClick(BaseQuickAdapter<ArchivesPojo, ? extends BaseViewHolder> adapter, View view, final int position) {
                switch (view.getId()){
                    case R.id.delete:
                        po = position;
                        //点击弹出对话框，选择拍照或者系统相册
                        new AlertDialog.Builder(getActivity()).setTitle("删除会议将不可恢复，确定删除?")//设置对话框标题
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialogInterface = dialog;
                                        showLoadingDialog(null);
                                        doHttp(RetrofitUtils.createApi(Http.class).deleteMeetingById(
                                                archivesAdapter.getItem(position).getMeeting_id()),3);
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
        });

        strList = new ArrayList<>();
        strList.add("全部");
        strList.add("标题");
        strList.add("姓名");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.item_archives_spinner, R.id.text,strList);
        selectDep.setAdapter(adapter);
        selectDep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectStr = adapterView.getItemAtPosition(i).toString();
                switch (selectStr){
                    case "全部":
                        selectInt = 1;
                        break;
                    case "标题":
                        selectInt = 2;
                        break;
                    case "姓名":
                        selectInt = 3;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
    }

    @Override
    protected boolean setIsInitRequestData() {
        return true;
    }

    @Override
    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).searchInfoByType(selectInt,pager,startDateStr,endDateStr,getInput),1);
    }

    @OnClick({R.id.start_date,R.id.end_date,R.id.btn,R.id.clear})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.start_date:
                startDataDialog = new TimePickerDialog.Builder()
                        .setCancelStringId("取消")
                        .setSureStringId("确定")
                        .setTitleStringId("选择查询起点时间")
                        .setYearText("年")
                        .setMonthText("月")
                        .setDayText("日")
                        /*.setHourText("时")
                        .setMinuteText("分")*/
                        .setCyclic(false)
                        .setMinMillseconds(DateTool.strTimeToTimestamp("2018-03-18","yyyy-MM-dd"))
                        .setMaxMillseconds(System.currentTimeMillis())
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(Color.parseColor("#769dfc"))
                        .setType(Type.YEAR_MONTH_DAY)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(Color.parseColor("#769dfc"))
                        .setWheelItemTextSize(12)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                startDate.setText(DateTool.timeStamp2Date(millseconds,"yyyy-MM-dd HH:mm"));
                            }
                        })
                        .build();
                startDataDialog.show(getActivity().getSupportFragmentManager(), "start");
                break;
            case R.id.end_date:
                endDataDialog = new TimePickerDialog.Builder()
                        .setCancelStringId("取消")
                        .setSureStringId("确定")
                        .setTitleStringId("选择查询结束时间")
                        .setYearText("年")
                        .setMonthText("月")
                        .setDayText("日")
                        /*.setHourText("时")
                        .setMinuteText("分")*/
                        .setCyclic(false)
                        .setMinMillseconds(DateTool.strTimeToTimestamp("2018-03-18","yyyy-MM-dd"))
                        .setMaxMillseconds(System.currentTimeMillis())
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(Color.parseColor("#769dfc"))
                        .setType(Type.YEAR_MONTH_DAY)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(Color.parseColor("#769dfc"))
                        .setWheelItemTextSize(12)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                endDate.setText(DateTool.timeStamp2Date(millseconds,"yyyy-MM-dd HH:mm"));
                            }
                        })
                        .build();
                endDataDialog.show(getActivity().getSupportFragmentManager(), "end");
                break;
            case R.id.btn:
                getInput = inputEd.getText().toString().trim();
                startDateStr = startDate.getText().toString().trim();
                endDateStr = endDate.getText().toString().trim();
                int codeTwo = DateTool.getTimeCompareSize(startDateStr,endDateStr);
                if(selectInt == 1){
                    pager = 1;
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Http.class).searchInfoByType(selectInt,pager,"","",""),1);
                    Logger.v(selectInt + "--" + startDateStr + "--" + endDateStr + "--" + getInput);
                }else if(getInput.length() == 0){
                    showErrorToast("请输入关键字");
                }else if(startDateStr.length() == 0 && endDateStr.length() != 0){
                    showErrorToast("请选择起点时间");
                }else if(startDateStr.length() != 0 && endDateStr.length() == 0){
                    showErrorToast("请选择结束时间");
                }else if(codeTwo == 2){
                    showErrorToast("查询会议起点时间和结束时间不能相同");
                }else if(codeTwo == 1){
                    showErrorToast("查询会议结束时间不能小于起点时间");
                }else{
                    pager = 1;
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Http.class).searchInfoByType(selectInt,pager,startDateStr,endDateStr,getInput),1);
                }
                break;
            case R.id.clear:
                inputEd.setText("");
                startDate.setText("");
                endDate.setText("");
                break;
        }
    }

    public List<ArchivesPojo> setData(){
        archivesPojos = new ArrayList<>();
        ArchivesPojo archivesPojo = null;
        for(int i = 0; i < 20; i++){
            archivesPojo = new ArchivesPojo();
            archivesPojos.add(archivesPojo);
        }
        return archivesPojos;
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

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                ptrFrameLayout.refreshComplete();
                archivesAdapter.removeAll();
                archivesPojos = AppJsonUtil.getArrayList(result,"content",ArchivesPojo.class);
                if (archivesPojos != null) {
                    archivesAdapter.setNewData(archivesPojos);
                    if (archivesPojos.size() < pageSize) {
                        archivesAdapter.loadMoreEnd();
                    }
                } else {
                    archivesAdapter.loadMoreEnd();
                }
                //增加页码
                pager++;
                break;
            case 2:
                //加载更多
                archivesPojos = AppJsonUtil.getArrayList(result,"content",ArchivesPojo.class);
                if (archivesPojos != null && archivesPojos.size() > 0) {
                    archivesAdapter.addData(archivesPojos);
                    archivesAdapter.loadMoreComplete();
                } else {
                    archivesAdapter.loadMoreEnd();
                }
                //增加页码
                pager++;
                break;
            case 3:
                dialogInterface.dismiss();
                archivesAdapter.remove(po);
                showToast("删除成功");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
