package com.hhxk.app.ui.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.AppManger;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.ManagerDepartmentAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.event.ManagerDepartmentEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.ManagePersonPojo;
import com.hhxk.app.pojo.ManagerDepartmentPojo;
import com.hhxk.app.ui.details.ConferenceDetailsFgt;
import com.hhxk.app.view.ManagerAddDepartmentDialog;
import com.hhxk.app.view.ManagerUpDepartmentDialog;
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
import retrofit2.http.HTTP;

/**
 * @title  部门管理fragemnt
 * @date   2019/02/27
 * @author enmaoFu
 */
public class ManagerDepartmentFgt extends BaseLazyFgt {

    @BindView(R.id.refresh)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private ManagerDepartmentAdapter managerDepartmentAdapter;

    /**
     * 数据源
     */
    private List<ManagerDepartmentPojo> managerDepartmentPojos;

    private ManagerAddDepartmentDialog managerAddDepartmentDialog;

    private ManagerUpDepartmentDialog managerUpDepartmentDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_manager_department;
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
                doHttp(RetrofitUtils.createApi(Http.class).findDept(),1);
            }
        });

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        managerDepartmentAdapter = new ManagerDepartmentAdapter(R.layout.item_manager_department, new ArrayList<ManagerDepartmentPojo>());
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
        recyclerView.setAdapter(managerDepartmentAdapter);

        managerDepartmentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<ManagerDepartmentPojo>() {
            @Override
            public void onItemChildClick(BaseQuickAdapter<ManagerDepartmentPojo, ? extends BaseViewHolder> adapter, View view, final int position) {
                switch (view.getId()){
                    case R.id.updata:
                        final String str = managerDepartmentAdapter.getItem(position).getDepartmentName();
                        managerUpDepartmentDialog = new ManagerUpDepartmentDialog(getActivity());
                        managerUpDepartmentDialog.show();
                        managerUpDepartmentDialog.setDialogAnimation();
                        managerUpDepartmentDialog.setTitle("修改部门");
                        managerUpDepartmentDialog.setHint("请输入部门");
                        managerUpDepartmentDialog.setText(str);
                        managerUpDepartmentDialog.setSelection(str.length());
                        managerUpDepartmentDialog.setBtnOnClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String input = managerUpDepartmentDialog.getInput();
                                if(input.equals(str)){
                                    showErrorToast("部门信息没有更改");
                                }else if(input.length() == 0){
                                    showErrorToast("请输入部门信息");
                                }else{
                                    showLoadingDialog(null);
                                    doHttp(RetrofitUtils.createApi(Http.class).changeDept(managerDepartmentAdapter.getItem(position).getDepartmentId(), input),3);
                                }
                            }
                        });
                        break;
                    case R.id.post:
                        EventBus.getDefault().post(new ManagerDepartmentEvent("post_open",
                                String.valueOf(managerDepartmentAdapter.getItem(position).getDepartmentId())));
                        break;
                    case R.id.delete:
                        //点击弹出对话框，选择拍照或者系统相册
                        new AlertDialog.Builder(getActivity()).setTitle("是否立即删除该部门?")//设置对话框标题
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showLoadingDialog(null);
                                        doHttp(RetrofitUtils.createApi(Http.class).deleteDept(managerDepartmentAdapter.getItem(position).getDepartmentId()),4);
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
    }

    @Override
    protected boolean setIsInitRequestData() {
        return true;
    }

    @Override
    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).findDept(),1);
    }

    @OnClick({R.id.add})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.add:
                managerAddDepartmentDialog = new ManagerAddDepartmentDialog(getActivity());
                managerAddDepartmentDialog.show();
                managerAddDepartmentDialog.setDialogAnimation();
                managerAddDepartmentDialog.setTitle("添加部门");
                managerAddDepartmentDialog.setHint("请输入部门");
                managerAddDepartmentDialog.setBtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getInput = managerAddDepartmentDialog.getInput();
                        if(getInput.length() == 0){
                            showErrorToast("请输入部门");
                        }else{
                            showLoadingDialog(null);
                            doHttp(RetrofitUtils.createApi(Http.class).addDept(getInput),2);
                        }
                        showToast(getInput);
                    }
                });
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                ptrFrameLayout.refreshComplete();
                managerDepartmentPojos = AppJsonUtil.getArrayList(result,ManagerDepartmentPojo.class);
                managerDepartmentAdapter.setNewData(managerDepartmentPojos);
                break;
            case 2:
                showToast("添加成功");
                managerAddDepartmentDialog.dismiss();
                doHttp(RetrofitUtils.createApi(Http.class).findDept(),1);
                break;
            case 3:
                showToast("修改成功");
                managerUpDepartmentDialog.dismiss();
                doHttp(RetrofitUtils.createApi(Http.class).findDept(),1);
                break;
            case 4:
                showToast("删除成功");
                doHttp(RetrofitUtils.createApi(Http.class).findDept(),1);
                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        switch (what){
            case 4:
                dismissLoadingDialog();
                showErrorToast("请先删除该部门所有职务和人员");
                break;
        }
    }
}
