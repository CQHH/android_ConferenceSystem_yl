package com.hhxk.app.ui.details;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.DateTool;
import com.em.baseframe.util.RetrofitUtils;
import com.hhxk.app.R;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.MyHostEvent;
import com.hhxk.app.event.PreservationEvent;
import com.hhxk.app.event.SponsorEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.EssentialinformationDetailsHostPojo;
import com.hhxk.app.pojo.EssentialinformationDetailsLssuePojo;
import com.hhxk.app.pojo.EssentialinformationDetailsOtherPojo;
import com.hhxk.app.pojo.EssentialinformationDetailsPojo;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  会议详情-会议详情嵌套的基本信息fragemnt
 * @date   2019/02/25
 * @author enmaoFu
 */
public class EssentialinformationDetailsFgt extends BaseLazyFgt {

    @BindView(R.id.title_input)
    EditText titleInput;
    @BindView(R.id.start_data)
    TextView startData;
    @BindView(R.id.end_data)
    TextView endData;
    @BindView(R.id.address_input)
    EditText addressInput;
    @BindView(R.id.info_input)
    EditText infoInput;
    @BindView(R.id.up)
    TextView up;
    @BindView(R.id.preservation)
    TextView preservation;

    private MMKV kv;

    private TimePickerDialog startDataDialog;
    private TimePickerDialog endDataDialog;

    private long tenYears = 1L * 365 * 1000 * 60 * 60 * 24L;

    /**
     * 基本信息
     */
    private EssentialinformationDetailsPojo essentialinformationDetailsPojo;

    /**
     * 主持人员
     */
    public static List<EssentialinformationDetailsHostPojo> essentialinformationDetailsHostPojos;

    /**
     * 参会人员
     */
    public static List<EssentialinformationDetailsOtherPojo> essentialinformationDetailsOtherPojos;

    /**
     * 会议议题
     */
    public static List<EssentialinformationDetailsLssuePojo> essentialinformationDetailsLssuePojos;

    public static int meetingId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query_details_conference;
    }

    @Override
    protected void initData() {
        kv = MMKV.defaultMMKV();

        if("2".equals(kv.decodeString("role_id"))){
            titleInput.setCursorVisible(false);
            titleInput.setFocusable(false);
            titleInput.setFocusableInTouchMode(false);

            addressInput.setCursorVisible(false);
            addressInput.setFocusable(false);
            addressInput.setFocusableInTouchMode(false);

            infoInput.setCursorVisible(false);
            infoInput.setFocusable(false);
            infoInput.setFocusableInTouchMode(false);

            up.setVisibility(View.GONE);
        }else {
            titleInput.setCursorVisible(true);
            titleInput.setFocusable(true);
            titleInput.setFocusableInTouchMode(true);

            addressInput.setCursorVisible(true);
            addressInput.setFocusable(true);
            addressInput.setFocusableInTouchMode(true);

            infoInput.setCursorVisible(true);
            infoInput.setFocusable(true);
            infoInput.setFocusableInTouchMode(true);

            up.setVisibility(View.VISIBLE);
        }

        if("y".equals(kv.decodeString("hy"))){
            preservation.setVisibility(View.VISIBLE);
        }else{
            preservation.setVisibility(View.GONE);
        }

    }

    @Override
    protected boolean setIsInitRequestData() {
        return true;
    }

    @Override
    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).findMeetingDetails(kv.decodeInt("meeting_id_details")),1);
    }

    @OnClick({R.id.start_data,R.id.end_data,R.id.up,R.id.sign,R.id.preservation})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.start_data:
                startDataDialog = new TimePickerDialog.Builder()
                        .setCancelStringId("取消")
                        .setSureStringId("确定")
                        .setTitleStringId("选择会议开始时间")
                        .setYearText("年")
                        .setMonthText("月")
                        .setDayText("日")
                        .setHourText("时")
                        .setMinuteText("分")
                        .setCyclic(false)
                        .setMinMillseconds(System.currentTimeMillis())
                        .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(Color.parseColor("#769dfc"))
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(Color.parseColor("#769dfc"))
                        .setWheelItemTextSize(12)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                startData.setText(DateTool.timeStamp2Date(millseconds,"yyyy-MM-dd HH:mm"));
                            }
                        })
                        .build();
                startDataDialog.show(getActivity().getSupportFragmentManager(), "start");
                break;
            case R.id.end_data:
                if("2".equals(kv.decodeString("role_id"))){
                    return;
                }else{
                    endDataDialog = new TimePickerDialog.Builder()
                            .setCancelStringId("取消")
                            .setSureStringId("确定")
                            .setTitleStringId("选择会议结束时间")
                            .setYearText("年")
                            .setMonthText("月")
                            .setDayText("日")
                            .setHourText("时")
                            .setMinuteText("分")
                            .setCyclic(false)
                            .setMinMillseconds(System.currentTimeMillis())
                            .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                            .setCurrentMillseconds(System.currentTimeMillis())
                            .setThemeColor(Color.parseColor("#769dfc"))
                            .setType(Type.ALL)
                            .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                            .setWheelItemTextSelectorColor(Color.parseColor("#769dfc"))
                            .setWheelItemTextSize(12)
                            .setCallBack(new OnDateSetListener() {
                                @Override
                                public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                    endData.setText(DateTool.timeStamp2Date(millseconds,"yyyy-MM-dd HH:mm"));
                                }
                            })
                            .build();
                    endDataDialog.show(getActivity().getSupportFragmentManager(), "end");
                }
                break;
            case R.id.up:
                if("2".equals(kv.decodeString("role_id"))){
                    return;
                }else{
                    String getTitle = titleInput.getText().toString().trim();
                    String getAddress = addressInput.getText().toString().trim();
                    String getInfo = infoInput.getText().toString().trim();
                    int code = DateTool.getTimeCompareSize(startData.getText().toString().trim(),endData.getText().toString().trim());
                    if(getTitle.length() == 0){
                        showErrorToast("请输入会议名称");
                    }else if(startData.getText().toString().trim().equals("请选择会议开始时间")){
                        showErrorToast("请选择会议开始时间");
                    }else if(endData.getText().toString().trim().equals("请选择会议结束时间")){
                        showErrorToast("请选择会议结束时间");
                    }else if(code == 2){
                        showErrorToast("会议开始时间和结束时间不能相同");
                    }else if(code == 1){
                        showErrorToast("会议结束时间不能小于开始时间");
                    }else if(getAddress.length() == 0){
                        showErrorToast("请输入会议地点");
                    }else if(getInfo.length() == 0){
                        showErrorToast("请输入会议简介");
                    }else{
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Http.class).changeMeeting(kv.decodeInt("meeting_id_details"),
                                getTitle,startData.getText().toString().trim(),endData.getText().toString().trim(),getInfo,getAddress),2);
                    }
                }
                break;
            case R.id.sign:
                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Http.class).signIn(Integer.valueOf(kv.decodeString("user_id")),kv.decodeInt("meeting_id_details")),3);
                break;
            case R.id.preservation:
                String getTitle = titleInput.getText().toString().trim();
                String getAddress = addressInput.getText().toString().trim();
                String getInfo = infoInput.getText().toString().trim();
                int code = DateTool.getTimeCompareSize(startData.getText().toString().trim(),endData.getText().toString().trim());
                if(getTitle.length() == 0){
                    showErrorToast("请输入会议名称");
                }else if(startData.getText().toString().trim().equals("请选择会议开始时间")){
                    showErrorToast("请选择会议开始时间");
                }else if(endData.getText().toString().trim().equals("请选择会议结束时间")){
                    showErrorToast("请选择会议结束时间");
                }else if(code == 2){
                    showErrorToast("会议开始时间和结束时间不能相同");
                }else if(code == 1){
                    showErrorToast("会议结束时间不能小于开始时间");
                }else if(getAddress.length() == 0){
                    showErrorToast("请输入会议地点");
                }else if(getInfo.length() == 0){
                    showErrorToast("请输入会议简介");
                }else{
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Http.class).changeMeeting(kv.decodeInt("meeting_id_details"),
                            getTitle,startData.getText().toString().trim(),endData.getText().toString().trim(),getInfo,getAddress),4);

                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                //基本信息
                EssentialinformationDetailsPojo edp = AppJsonUtil.getObject(result,"paperlessInfo",EssentialinformationDetailsPojo.class);
                titleInput.setText(edp.getMeeting_name());
                startData.setText(DateTool.timeStamp2Date(edp.getMeeting_start(),"yyyy-MM-dd HH:mm"));
                endData.setText(DateTool.timeStamp2Date(edp.getMeeting_end(),"yyyy-MM-dd HH:mm"));
                addressInput.setText(edp.getMeeting_address());
                infoInput.setText(edp.getMeeting_info());
                meetingId = edp.getMeeting_id();

                //主持人员
                essentialinformationDetailsHostPojos = AppJsonUtil.getMyArrayList(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"content"), "userMap"),"hostList"),EssentialinformationDetailsHostPojo.class);

                //参会人员
                essentialinformationDetailsOtherPojos = AppJsonUtil.getMyArrayList(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"content"), "userMap"),"otherList"),EssentialinformationDetailsOtherPojo.class);

                //会议议题
                essentialinformationDetailsLssuePojos = AppJsonUtil.getArrayList(result,"lssueList",EssentialinformationDetailsLssuePojo.class);

                break;
            case 2:
                showToast("修改基本信息成功");
                break;
            case 3:
                showToast("签到成功");
                break;
            case 4:
                doHttp(RetrofitUtils.createApi(Http.class).generateMeeting(kv.decodeInt("meeting_id_details")),5);
                break;
            case 5:
                showToast("基本信息保存成功");
                MyHostEvent mh = new MyHostEvent();
                mh.setCode("保存");
                EventBus.getDefault().post(mh);
                break;
        }
    }
}
