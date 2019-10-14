package com.hhxk.app.ui.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.http.HttpCallBack;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.util.ToastUtil;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.ManagerDepartmentAdapter;
import com.hhxk.app.adapter.ManagerPostAdapter;
import com.hhxk.app.base.BaseLazyFragment;
import com.hhxk.app.event.ManagerDepartmentEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.ManagerDepartmentPojo;
import com.hhxk.app.pojo.ManagerPostPojo;
import com.hhxk.app.view.LoadingDialog;
import com.hhxk.app.view.ManagerAddPostDialog;
import com.hhxk.app.view.ManagerUpPostDialog;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @title  管理操作-部门管理职务嵌套的fragemnt
 * @date   2019/02/27
 * @author enmaoFu
 */
public class ManagerPostFgt extends BaseLazyFragment implements HttpCallBack {

    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private ImageView backImg;
    private TextView add;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private ManagerPostAdapter managerPostAdapter;

    /**
     * 数据源
     */
    private List<ManagerPostPojo> managerPostPojos;

    private ManagerUpPostDialog managerUpPostDialog;

    private ManagerAddPostDialog managerAddPostDialog;

    private LoadingDialog loadingDialog;

    private MMKV kv;

    @Override
    public void initBundle(@NonNull Bundle bundle) {

    }

    @Override
    protected void initView() {
        backImg = findView(R.id.back_img,true);
        ptrFrameLayout = findView(R.id.refresh,false);
        recyclerView = findView(R.id.recyvlerview,false);
        add = findView(R.id.add,true);
    }

    @Override
    public void onResume() {
        super.onResume();
        kv = MMKV.defaultMMKV();
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.show();
        doHttp(RetrofitUtils.createApi(Http.class).findPosition(kv.decodeString("manager_department_id")),1);
    }

    @Override
    protected void initData() {

        /**
         * 下拉刷新
         */
        PtrInitHelper.initPtr(getActivity(), ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                doHttp(RetrofitUtils.createApi(Http.class).findPosition(kv.decodeString("manager_department_id")),1);
            }
        });

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        managerPostAdapter = new ManagerPostAdapter(R.layout.item_manager_post, new ArrayList<ManagerPostPojo>());
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
        recyclerView.setAdapter(managerPostAdapter);
        managerPostAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<ManagerPostPojo>() {
            @Override
            public void onItemChildClick(BaseQuickAdapter<ManagerPostPojo, ? extends BaseViewHolder> adapter, View view, final int position) {
                switch (view.getId()){
                    case R.id.delete:
                        //点击弹出对话框，选择拍照或者系统相册
                        new AlertDialog.Builder(getActivity()).setTitle("是否删除当前职务?")//设置对话框标题
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        loadingDialog.show();
                                        doHttp(RetrofitUtils.createApi(Http.class)
                                                .deletePosition(String.valueOf(managerPostAdapter.getItem(position).getPositionId())),4);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
                        break;
                    case R.id.updata:
                        final String str = managerPostAdapter.getItem(position).getPositionName();
                        managerUpPostDialog = new ManagerUpPostDialog(getActivity());
                        managerUpPostDialog.show();
                        managerUpPostDialog.setDialogAnimation();
                        managerUpPostDialog.setTitle("修改职务");
                        managerUpPostDialog.setHint("请输入职务");
                        managerUpPostDialog.setText(str);
                        managerUpPostDialog.setSelection(str.length());
                        managerUpPostDialog.setBtnOnClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String input = managerUpPostDialog.getInput();
                                if(input.equals(str)){
                                    ToastUtil.showErrorToast("职务信息没有更改", Toast.LENGTH_SHORT);
                                }else if(input.length() == 0){
                                    ToastUtil.showErrorToast("请输入职务信息", Toast.LENGTH_SHORT);
                                }else{
                                    loadingDialog.show();
                                    doHttp(RetrofitUtils.createApi(Http.class).
                                            changePosition(String.valueOf(managerPostAdapter.getItem(position).getPositionId()),input),2);
                                }
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_img:
                EventBus.getDefault().post(new ManagerDepartmentEvent("post_close",""));
                break;
            case R.id.add:
                managerAddPostDialog = new ManagerAddPostDialog(getActivity());
                managerAddPostDialog.show();
                managerAddPostDialog.setDialogAnimation();
                managerAddPostDialog.setTitle("增加职务");
                managerAddPostDialog.setHint("请输入职务");
                managerAddPostDialog.setBtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = managerAddPostDialog.getInput();
                        if(input.length() == 0){
                            ToastUtil.showErrorToast("请输入职务信息", Toast.LENGTH_SHORT);
                        }else{
                            loadingDialog.show();
                            doHttp(RetrofitUtils.createApi(Http.class).addPosition(input,kv.decodeString("manager_department_id")),3);
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_manager_post;
    }

    /*public List<ManagerPostPojo> setData(){
        managerPostPojos = new ArrayList<>();
        ManagerPostPojo managerPostPojo = null;
        for(int i = 0; i < 20; i++){
            managerPostPojo = new ManagerPostPojo();
            managerPostPojos.add(managerPostPojo);
        }
        return managerPostPojos;
    }*/

    /**
     * 请求网络
     * @param bodyCall
     * @param what
     */
    public void doHttp(Call<ResponseBody> bodyCall, final int what) {
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String result = response.body().string();
                    Logger.json(result);
                    JSONObject object = JSONObject.parseObject(result);
                    if (object.containsKey("code")) {
                        int code = object.getInteger("code");
                        if (code == 200) {
                            ManagerPostFgt.this.onSuccess(result, call, response, what);
                        } else {
                            ManagerPostFgt.this.onFailure(result, call, response, what);
                        }
                    }

                } catch (Exception e) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    String exception = baos.toString();
                    Logger.w(exception);
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.w(t.getMessage() + call.request().url().toString());
                ManagerPostFgt.this.onError(call, t, what);

            }
        });

    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        switch (what){
            case 1:
                ptrFrameLayout.refreshComplete();
                managerPostPojos = AppJsonUtil.getArrayList(result,ManagerPostPojo.class);
                managerPostAdapter.setNewData(managerPostPojos);
                loadingDialog.dismiss();
                break;
            case 2:
                showToast("修改成功");
                loadingDialog.dismiss();
                managerUpPostDialog.dismiss();
                doHttp(RetrofitUtils.createApi(Http.class).findPosition(kv.decodeString("manager_department_id")),1);
                break;
            case 3:
                showToast("添加成功");
                loadingDialog.dismiss();
                managerAddPostDialog.dismiss();
                doHttp(RetrofitUtils.createApi(Http.class).findPosition(kv.decodeString("manager_department_id")),1);
                break;
            case 4:
                showToast("删除成功");
                loadingDialog.dismiss();
                doHttp(RetrofitUtils.createApi(Http.class).findPosition(kv.decodeString("manager_department_id")),1);
                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        String msg = AppJsonUtil.getString(result, "msg");
        if (msg != null) {
            showErrorToast(msg);
        }
        switch (what){
            case 4:
                loadingDialog.dismiss();
                showErrorToast("请先删除所有担任该职务的人员");
                break;
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {

    }
}
