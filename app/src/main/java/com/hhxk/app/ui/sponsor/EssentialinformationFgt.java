package com.hhxk.app.ui.sponsor;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.DateTool;
import com.em.baseframe.util.RetrofitUtils;
import com.hhxk.app.R;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.event.PreservationEvent;
import com.hhxk.app.event.SponsorClearEvent;
import com.hhxk.app.event.SponsorEvent;
import com.hhxk.app.interfaces.Http;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  发起会议-基本信息fragemnt
 * @date   2019/02/20
 * @author enmaoFu
 */
public class EssentialinformationFgt extends BaseLazyFgt {

    @BindView(R.id.title_input)
    TextView titleInput;
    @BindView(R.id.start_data)
    TextView startData;
    @BindView(R.id.end_data)
    TextView endData;
    @BindView(R.id.address_input)
    EditText addressInput;
    @BindView(R.id.info_input)
    EditText infoInput;

    private TimePickerDialog startDataDialog;
    private TimePickerDialog endDataDialog;

    private long tenYears = 1L * 365 * 1000 * 60 * 60 * 24L;

    private MMKV kv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_essential_information;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        kv = MMKV.defaultMMKV();
    }

    @Override
    protected boolean setIsInitRequestData() {
        return false;
    }

    @Override
    protected void requestData() {

    }

    @OnClick({R.id.start_data,R.id.end_data,R.id.preservation,R.id.next})
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
                break;
            case R.id.preservation:
                String getTitleTwo = titleInput.getText().toString().trim();
                String getAddressTwo = addressInput.getText().toString().trim();
                String getInfoTwo = infoInput.getText().toString().trim();
                int codeTwo = DateTool.getTimeCompareSize(startData.getText().toString().trim(),endData.getText().toString().trim());
                if(getTitleTwo.length() == 0){
                    showErrorToast("请输入会议名称");
                }else if(startData.getText().toString().trim().equals("请选择会议开始时间")){
                    showErrorToast("请选择会议开始时间");
                }else if(endData.getText().toString().trim().equals("请选择会议结束时间")){
                    showErrorToast("请选择会议结束时间");
                }else if(codeTwo == 2){
                    showErrorToast("会议开始时间和结束时间不能相同");
                }else if(codeTwo == 1){
                    showErrorToast("会议结束时间不能小于开始时间");
                }else if(getAddressTwo.length() == 0){
                    showErrorToast("请输入会议地点");
                }else if(getInfoTwo.length() == 0){
                    showErrorToast("请输入会议简介");
                }else{
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Http.class).preservationMeeting(kv.decodeString("user_id"),
                            getTitleTwo,startData.getText().toString().trim(),endData.getText().toString().trim(),getInfoTwo,getAddressTwo),2);
                }
                break;
            case R.id.next:
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
                    doHttp(RetrofitUtils.createApi(Http.class).startMeeting(kv.decodeString("user_id"),
                            getTitle,startData.getText().toString().trim(),endData.getText().toString().trim(),getInfo,getAddress),1);
                }
                /*SponsorEvent se = new SponsorEvent();
                se.setCode("基本信息-下");
                se.setMeetingId(1);
                EventBus.getDefault().post(se);*/
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                showToast("基本信息填写完成");
                SponsorEvent se = new SponsorEvent();
                se.setCode("基本信息-下");
                se.setMeetingId(AppJsonUtil.getInt(AppJsonUtil.getString(result, "content"),"meetingId"));
                EventBus.getDefault().post(se);
                break;
            case 2:
                showToast("基本信息保存成功");
                PreservationEvent pe = new PreservationEvent();
                pe.setCode("保存");
                EventBus.getDefault().post(pe);
                SponsorEvent seTwo = new SponsorEvent();
                seTwo.setCode("基本信息-保存");
                EventBus.getDefault().post(seTwo);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(SponsorClearEvent sponsorClearEvent){
        if(sponsorClearEvent.getClear().equals("clear")){
            titleInput.setText("");
            startData.setText("请选择会议开始时间");
            endData.setText("请选择会议结束时间");
            addressInput.setText("");
            infoInput.setText("");
            kv.encode("meetingId",00);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        kv.encode("meetingId",00);
    }
}
