package com.hhxk.app.ui.details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.hhxk.app.R;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.util.NetWebSocketRequest;
import com.hhxk.app.util.record.PcmRecorder;
import com.hhxk.app.util.record.SpeechError;
import com.iflytek.fsp.shield.android.sdk.websocket.WebSocketCallback;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.ByteString;
import retrofit2.Call;

/**
 * @title  会议详情-我发起的会议会议记录fragemnt
 * @date   2019/03/13
 * @author enmaoFu
 */
public class RecordDetailsFgt extends BaseLazyFgt {

    @BindView(R.id.text)
    EditText mText;
    @BindView(R.id.start_and_end)
    TextView startAndEnd;
    @BindView(R.id.w_re_on)
    RelativeLayout wReOn;
    @BindView(R.id.w_re_two)
    RelativeLayout wReTwo;
    @BindView(R.id.textcon)
    EditText mTextcon;
    @BindView(R.id.ed_btn)
    TextView edBtn;

    /**
     * 音频采集仪
     */
    private PcmRecorder mPcmRecorder;

    /**
     * 语音转写网络请求
     */
    private NetWebSocketRequest mWebSocketRequest;

    /**
     * 可变string序列
     */
    private StringBuilder mRecognizeText = new StringBuilder();

    /**
     * 消息机制，用于配合计时
     */
    private Handler handler = new Handler();

    /**
     * 表示秒
     */
    private int second = 0;

    private MMKV kv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query_details_record;
    }

    @Override
    protected void initData() {
        kv = MMKV.defaultMMKV();
        editTextEnable(false,mTextcon);

        mWebSocketRequest = NetWebSocketRequest.getInstance();
        if(kv.decodeString("record").equals("y")){
            wReOn.setVisibility(View.VISIBLE);
            wReTwo.setVisibility(View.VISIBLE);
            edBtn.setVisibility(View.VISIBLE);
        }else{
            wReOn.setVisibility(View.GONE);
            wReTwo.setVisibility(View.VISIBLE);
            edBtn.setVisibility(View.GONE);
        }

    }

    @Override
    protected boolean setIsInitRequestData() {

        return true;
    }

    @Override
    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).findSummary(String.valueOf(kv.decodeInt("meeting_id_details"))),1);
    }

    @OnClick({R.id.start_and_end,R.id.save,R.id.ed_btn})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.start_and_end:
                if(startAndEnd.getText().toString().trim().equals("开始转写")){
                    mRecognizeText.delete(0,mRecognizeText.length());
                    String str = mText.getText().toString().trim();
                    if(str.length() > 0){
                        String body = new String(str);
                        mRecognizeText.append(body);
                    }
                    release();
                    mWebSocketRequest.astWebsocketChinConnect(mWebSocketCallback, "astWebsocketChinConnect");
                }else{
                    stopRecord();
                    clearRunnable();
                    startAndEnd.setText("开始转写");
                }
                break;
            /*case R.id.again:
                if(startAndEnd.getText().toString().trim().equals("开始转写")){
                    showErrorToast("未开始转写");
                }else{
                    stopRecord();
                    handler.removeCallbacks(runnable);
                    startAndEnd.setText("开始转写");
                    mText.setText(mTextcon.getText().toString().trim());
                    mText.setSelection(mTextcon.length());
                }
                break;*/
            case R.id.save:
                if(mText.getText().toString().trim().equals(mTextcon.getText().toString().trim())){
                    showErrorToast("纪要内容无改变");
                }else{
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Http.class).addSummary(String.valueOf(kv.decodeInt("meeting_id_details")),
                            mText.getText().toString().trim()),2);
                }
                break;
            case R.id.ed_btn:
                if(mTextcon.getText().toString().trim().equals("暂无会议纪要")){
                    showErrorToast("暂无会议纪要");
                }else if(edBtn.getText().toString().trim().equals("编辑")){
                    edBtn.setText("保存");
                    editTextEnable(true,mTextcon);
                    showToast("可以开始编辑");
                }else if(edBtn.getText().toString().trim().equals("保存")){
                    if(!mTextcon.getText().toString().trim().equals(mText.getText().toString().trim())){
                        //点击弹出对话框，选择拍照或者系统相册
                        new AlertDialog.Builder(getActivity()).setTitle("已保存纪要内容与正在转写纪要内容不一致，是否强制保存?")//设置对话框标题
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showLoadingDialog(null);
                                        doHttp(RetrofitUtils.createApi(Http.class).addSummary(String.valueOf(kv.decodeInt("meeting_id_details")),
                                                mTextcon.getText().toString().trim()),3);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
                    }else if(!startAndEnd.getText().toString().trim().equals("开始转写")){
                        showErrorToast("右侧正在转写，请结束转写后保存");
                    }else {
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Http.class).addSummary(String.valueOf(kv.decodeInt("meeting_id_details")),
                                mTextcon.getText().toString().trim()),3);
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, retrofit2.Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                if(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"summaryInfo").equals("") ||
                        AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"summaryInfo").equals("null")){
                    mTextcon.setText("暂无会议纪要");
                }else{
                    mTextcon.setText(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"summaryInfo"));
                    mText.setText(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"summaryInfo"));
                    mText.setSelection(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"summaryInfo").length());
                }
                break;
            case 2:
                showToast("保存会议纪要成功");
                doHttp(RetrofitUtils.createApi(Http.class).findSummary(String.valueOf(kv.decodeInt("meeting_id_details"))),1);
                break;
            case 3:
                showToast("保存会议纪要成功");
                edBtn.setText("编辑");
                editTextEnable(false,mTextcon);
                doHttp(RetrofitUtils.createApi(Http.class).findSummary(String.valueOf(kv.decodeInt("meeting_id_details"))),1);
                break;
        }
    }

    /**
     * 转写网络监听
     */
    private WebSocketCallback mWebSocketCallback = new WebSocketCallback() {
        @Override
        public void onOpen(Response response) {
            startRecord();
            handler.postDelayed(runnable, 1000);
        }

        @Override
        public void onMessage(String text) {
            JSONObject result = JSON.parseObject(text);
            //可能有两种返回结果，网关返回的错误和正常数据
            //{"respCode":"SHD-1011","respMsg":"websocket调用失败”}
            //{"body":[-27,-68,],"respCode":"","respMsg":"","terminalNo":"/10.5.221.224:48867"}
            JSONArray jsonArray = result.getJSONArray("body");
            if (jsonArray != null) {
                int length = jsonArray.size();
                if (length > 0) {
                    byte[] b = new byte[length];
                    for (int i = 0; i < length; i++) {
                        b[i] = ((Integer) jsonArray.get(i)).byteValue();
                    }
                    String body = new String(b);
                    mRecognizeText.append(body);
                    mText.setText(mRecognizeText);
                    mText.setSelection(mRecognizeText.length());
                }
            } else {
                String respCode = result.getString("respCode");
                String respMsg = result.getString("respMsg");
                showErrorToast(respCode + "," + respMsg);
            }
        }

        @Override
        public void onMessage(ByteString bytes) {

        }

        @Override
        public void onClosing(int code, String reason) {

        }

        @Override
        public void onClosed(int code, String reason) {

        }

        @Override
        public void onFailure(Throwable t, @Nullable Response response) {
            showErrorToast(t.getMessage());
        }
    };

    /**
     * 录音监听
     */
    private PcmRecorder.PcmRecordListener mPcmRecordListener = new PcmRecorder.PcmRecordListener() {
        @Override
        public void onRecordBuffer(byte[] data, int var2, int var3) {
            if (mWebSocketRequest != null) {
                mWebSocketRequest.send(data);
            }
        }

        @Override
        public void onError(SpeechError error) {
            showErrorToast(error.getErrorDescription());
        }

        @Override
        public void onRecordStarted(boolean var1) {

        }

        @Override
        public void onRecordReleased() {

        }
    };

    /**
     * 释放录音转写网络
     */
    private void release() {
        stopRecord();
        if (mWebSocketRequest != null) {
            mWebSocketRequest.close();
        }
    }

    /**
     * 开始
     */
    private void startRecord() {
        mPcmRecorder = new PcmRecorder(16000,
                40, MediaRecorder.AudioSource.MIC);
        mPcmRecorder.startRecording(mPcmRecordListener);
    }

    /**
     * 停止
     */
    private void stopRecord() {
        if (mPcmRecorder != null) {
            mPcmRecorder.stopRecord(true);
            mPcmRecorder = null;
        }
    }

    /**
     * 计时器线程
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            second++;
            startAndEnd.setText("停止转写" + second + "秒");
            handler.postDelayed(this, 1000);
        }
    };

    /**
     * 清除计时器
     */
    public void clearRunnable(){
        handler.removeCallbacks(runnable);
        second = 0;
    }

    /**
     * 设置ed是否可编辑
     * @param enable
     * @param editText
     */
    public void editTextEnable(boolean enable, EditText editText){
        editText.setFocusable(enable);
        editText.setFocusableInTouchMode(enable);
        editText.setLongClickable(enable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
        handler.removeCallbacks(runnable);
    }
}
