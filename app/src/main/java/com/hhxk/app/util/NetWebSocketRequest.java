package com.hhxk.app.util;

import com.iflytek.fsp.shield.android.sdk.constant.SdkConstant;
import com.iflytek.fsp.shield.android.sdk.websocket.ApiWsRequestInfo;
import com.iflytek.fsp.shield.android.sdk.websocket.BaseWsApp;
import com.iflytek.fsp.shield.android.sdk.websocket.WebSocketCallback;

/**
 * @title  语音转写网络请求
 * @date   2019/03/13
 * @author enmaoFu
 */
public class NetWebSocketRequest extends BaseWsApp {

    public static NetWebSocketRequest getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final NetWebSocketRequest INSTANCE = new NetWebSocketRequest();
    }

    private NetWebSocketRequest() {
        this.appId = "2275889aba0b457b94ef7f445d6fc2e1";
        this.appSecret = "C2097EF3A1908651958FADCBD8890D95";
        this.host = "222.177.68.54";
        this.webSocketPort = 4999;
        this.stage = "RELEASE";
        this.publicKey = "305C300D06092A864886F70D0101010500034B00304802410093866339E23AEC247303291752DD45B1035F8515F09C96FDD6C3CA3084FD622F230EF226C2039A1636A36FD9202F5D1962D0483280EA74005184A7F93F89DA1B0203010001";
    }

    /**
     * 实时中文转写
     * @param callback WebSocket连接回调
     * @param tag .
     */
    public void astWebsocketChinConnect(WebSocketCallback callback, Object tag) {
        ApiWsRequestInfo apiRequest = new ApiWsRequestInfo("/cosmo-guidance/voice", SdkConstant.AUTH_TYPE_DEFAULT, "1");
        apiRequest.addQuery("lang", "chin");
        //转写结果返回通过bizId来区分，注意不同用户和不同应用都要使用不同的bizId
        apiRequest.addQuery("bizId", "mysong");
        //直接返回每一句最终结果
        apiRequest.addQuery("scene", "ast");
        //直接返回未处理的结果，包括中间结果和最终结果
//        apiRequest.addQuery("scene", "ast-json");
        //添加path参数的方法
//        apiRequest.addPathParams("", "");

        connect(apiRequest, callback, tag);
    }

    /**
     * 实时英文转写
     * @param callback WebSocket连接回调
     * @param tag .
     */
    public void astWebsocketEnglConnect(WebSocketCallback callback, Object tag) {
        ApiWsRequestInfo apiRequest = new ApiWsRequestInfo("/cosmo-guidance/voice_engl", SdkConstant.AUTH_TYPE_DEFAULT, "1");
        apiRequest.addQuery("lang", "engl");
        apiRequest.addQuery("bizId", "mysong");
        apiRequest.addQuery("scene", "ast");

        connect(apiRequest, callback, tag);
    }

}
