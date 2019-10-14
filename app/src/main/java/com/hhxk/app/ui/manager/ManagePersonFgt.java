package com.hhxk.app.ui.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.AppManger;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.ManagePersonAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.ManagePersonPojo;
import com.hhxk.app.pojo.ManagerDepartmentListPojo;
import com.hhxk.app.pojo.ManagerPostPojo;
import com.hhxk.app.util.HashMapUtils;
import com.hhxk.app.view.ManagerAddPersonDialog;
import com.hhxk.app.view.ManagerDepartmentDialog;
import com.hhxk.app.view.ManagerUpPersonDialog;
import com.tencent.mmkv.MMKV;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  成员管理fragemnt
 * @date   2019/02/26
 * @author enmaoFu
 */
public class ManagePersonFgt extends BaseLazyFgt{

    @BindView(R.id.refresh)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;
    @BindView(R.id.department_text)
    TextView departmentText;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private ManagePersonAdapter managePersonAdapter;

    /**
     * 数据源
     */
    private List<ManagePersonPojo> managePersonPojos;

    /**
     * 当前页数
     */
    private int pager = 1;

    /**
     * 每页条数
     */
    private int pageSize = 3;

    private ManagerDepartmentDialog managerDepartmentDialog;

    private List<ManagerDepartmentListPojo> managerDepartmentListPojos;

    private  MMKV kv;

    private int departmentId = 0;

    private int po;

    private int personPo;

    private ManagerUpPersonDialog managerUpPersonDialog;

    private ManagerAddPersonDialog managerAddPersonDialog;

    private List<ManagerPostPojo> managerPostPojos;

    /**
     * 修改时保存的ID
     */
    private String upDepartmentId;
    private String upPositionId;

    /**
     * 修改时保存的部门和职务
     */
    private String upDepartmentName;
    private String upPositionName;
    private String upUserName;

    /**
     * 添加时保存的ID
     */
    private String addDepartmentId;
    private String addPositionId;

    /**
     * 添加时保存的部门和职务
     */
    private String addDepartmentName;
    private String addPositionName;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_manager_person;
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();
        kv.encode("isSelect", false);

        /**
         * 下拉刷新
         */
        PtrInitHelper.initPtr(getActivity(), ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pager = 1;
                departmentId = 0;
                doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,departmentId),1);
            }
        });

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        managePersonAdapter = new ManagePersonAdapter(R.layout.item_manager_person, new ArrayList<ManagePersonPojo>());
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
        recyclerView.setAdapter(managePersonAdapter);

        //上拉加载更多
        managePersonAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (recyclerView != null)
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {

                            if (pager == 1) {
                                managePersonAdapter.loadMoreEnd();
                                return;
                            }
                            doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,departmentId),2);
                    }
                    });

            }
        }, recyclerView);

        managePersonAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<ManagePersonPojo>() {
            @Override
            public void onItemChildClick(BaseQuickAdapter<ManagePersonPojo, ? extends BaseViewHolder> adapter, View view, final int position) {
                switch (view.getId()){
                    case R.id.uppwd:
                        //点击弹出对话框，选择拍照或者系统相册
                        new AlertDialog.Builder(getActivity()).setTitle("是否重置该人员密码?")//设置对话框标题
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showLoadingDialog(null);
                                        doHttp(RetrofitUtils.createApi(Http.class).resetPass(managePersonAdapter.getItem(position).getUser_account()),10);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
                        break;
                    case R.id.delete:
                        //点击弹出对话框，选择拍照或者系统相册
                        new AlertDialog.Builder(getActivity()).setTitle("是否删除当前人员?")//设置对话框标题
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        po = position;
                                        showLoadingDialog(null);
                                        doHttp(RetrofitUtils.createApi(Http.class).deleteUserById(managePersonAdapter.getItem(position).getUser_id()),5);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
                        break;
                    case R.id.updata:
                        personPo = position;
                        managerUpPersonDialog = new ManagerUpPersonDialog(getActivity());
                        managerUpPersonDialog.show();
                        managerUpPersonDialog.setDialogAnimation();
                        managerUpPersonDialog.setName(managePersonAdapter.getItem(position).getUser_name());
                        managerUpPersonDialog.setSelection(managePersonAdapter.getItem(position).getUser_name().length());

                        final Map<String,String> depMap = new HashMap<>();
                        managerDepartmentListPojos.get(0).setDepartmentName("请选择");
                        for(int i = 0; i < managerDepartmentListPojos.size(); i++){
                            depMap.put(String.valueOf(managerDepartmentListPojos.get(i).getDepartmentId()),
                                    managerDepartmentListPojos.get(i).getDepartmentName());
                        }
                        managerUpPersonDialog.setDepMap(depMap);
                        managerUpPersonDialog.setSpinnerD(getActivity());

                        managerUpPersonDialog.setSpinnerDepOnItemClick(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectStr = parent.getItemAtPosition(position).toString();
                                String key = HashMapUtils.getKey((HashMap<String, String>) depMap,selectStr);
                                upDepartmentId = key;
                                upDepartmentName = selectStr;
                                upPositionId = "0";
                                doHttp(RetrofitUtils.createApi(Http.class).findPosition(key),6);
                                Log.v("print","key：" + key + "---value：" + selectStr);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        managerUpPersonDialog.setBtnOnClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String getName = managerUpPersonDialog.getName();
                                upUserName = getName;
                                if(getName.length() == 0){
                                    showErrorToast("请收入人员名字");
                                }else if(null == upDepartmentId || "null".equals(upDepartmentId) || "".equals(upDepartmentId)){
                                    showErrorToast("系统错误，请重新打开");
                                }else if(upDepartmentId.equals("0")){
                                    showErrorToast("请选择部门");
                                }else if(upPositionId.equals("0")){
                                    showErrorToast("请选择有职务的部门");
                                }else{
                                    showLoadingDialog(null);
                                    doHttp(RetrofitUtils.createApi(Http.class).changeInfo(kv.decodeString("user_id"),
                                            upDepartmentId,upPositionId),7);
                                }
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        doHttp(RetrofitUtils.createApi(Http.class).findDept(),3);
    }

    @Override
    protected boolean setIsInitRequestData() {
        return true;
    }

    @Override
    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,departmentId),1);
    }

    @OnClick({R.id.select_lin,R.id.add})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.select_lin:
                if(kv.decodeBool("isSelect") == false){
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Http.class).findDept(),3);
                }else{
                    showSelect();
                }
                break;
            case R.id.add:
                managerAddPersonDialog = new ManagerAddPersonDialog(getActivity());
                managerAddPersonDialog.show();
                managerAddPersonDialog.setDialogAnimation();

                final Map<String,String> depMap = new HashMap<>();
                managerDepartmentListPojos.get(0).setDepartmentName("请选择");
                for(int i = 0; i < managerDepartmentListPojos.size(); i++){
                    depMap.put(String.valueOf(managerDepartmentListPojos.get(i).getDepartmentId()),
                            managerDepartmentListPojos.get(i).getDepartmentName());
                }
                managerAddPersonDialog.setDepMap(depMap);
                managerAddPersonDialog.setSpinnerDep(getActivity());

                managerAddPersonDialog.setSpinnerDepOnItemClick(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectStr = parent.getItemAtPosition(position).toString();
                        String key = HashMapUtils.getKey((HashMap<String, String>) depMap,selectStr);
                        addDepartmentId = key;
                        addDepartmentName = selectStr;
                        addPositionId = "0";
                        doHttp(RetrofitUtils.createApi(Http.class).findPosition(key),8);
                        Log.v("print","key：" + key + "---value：" + selectStr);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                managerAddPersonDialog.setBtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getName = managerAddPersonDialog.getName();
                        if(getName.length() == 0){
                            showErrorToast("请收入人员名字");
                        }else if(managerAddPersonDialog.getCode().length() == 0){
                            showErrorToast("请收入人员账号");
                        }else if(null == addDepartmentId || "null".equals(addDepartmentId) || "".equals(addDepartmentId)){
                            showErrorToast("系统错误，请重新打开");
                        }else if(addDepartmentId.equals("0")){
                            showErrorToast("请选择部门");
                        }else if(addPositionId.equals("0")){
                            showErrorToast("请选择有职务的部门");
                        }else{
                            showLoadingDialog(null);
                            doHttp(RetrofitUtils.createApi(Http.class).register(getName,addDepartmentId,addPositionId,
                                    managerAddPersonDialog.getCode()),9);
                        }
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
                managePersonAdapter.removeAll();
                managePersonPojos = AppJsonUtil.getArrayList(AppJsonUtil.getString(result, "content"),ManagePersonPojo.class);
                if (managePersonPojos != null) {
                    managePersonAdapter.setNewData(managePersonPojos);
                    if (managePersonPojos.size() < pageSize) {
                        managePersonAdapter.loadMoreEnd();
                    }
                } else {
                    managePersonAdapter.loadMoreEnd();
                }
                //增加页码
                pager++;

                departmentText.setText("全部");

                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Http.class).findDept(),3);
                break;
            case 2:
                //加载更多
                managePersonPojos = AppJsonUtil.getArrayList(AppJsonUtil.getString(result, "content"),ManagePersonPojo.class);
                if (managePersonPojos != null && managePersonPojos.size() > 0) {
                    managePersonAdapter.addData(managePersonPojos);
                    managePersonAdapter.loadMoreComplete();
                } else {
                    managePersonAdapter.loadMoreEnd();
                }
                //增加页码
                pager++;
                break;
            case 3:
                managerDepartmentListPojos = AppJsonUtil.getArrayList(result,ManagerDepartmentListPojo.class);
                ManagerDepartmentListPojo managerDepartmentListPojo = new ManagerDepartmentListPojo();
                managerDepartmentListPojo.setDepartmentId(0);
                managerDepartmentListPojo.setDepartmentName("全部");
                managerDepartmentListPojo.setIs(1);
                managerDepartmentListPojos.add(0,managerDepartmentListPojo);

                kv.encode("isSelect", true);
                break;
            case 4:
                ptrFrameLayout.refreshComplete();
                managePersonAdapter.removeAll();
                managePersonPojos = AppJsonUtil.getArrayList(AppJsonUtil.getString(result, "content"),ManagePersonPojo.class);
                if (managePersonPojos != null) {
                    managePersonAdapter.setNewData(managePersonPojos);
                    if (managePersonPojos.size() < pageSize) {
                        managePersonAdapter.loadMoreEnd();
                    }
                } else {
                    managePersonAdapter.loadMoreEnd();
                }
                //增加页码
                pager++;

                departmentText.setText(managerDepartmentDialog.getAdapter().getItem(po).getDepartmentName());
                List<ManagerDepartmentListPojo> managerDepartmentListPojosNew = managerDepartmentDialog.getAdapter().getData();
                for(int i = 0; i < managerDepartmentListPojosNew.size(); i++){
                    managerDepartmentListPojosNew.get(i).setIs(0);
                }
                managerDepartmentListPojosNew.get(po).setIs(1);
                managerDepartmentDialog.getAdapter().setNewData(managerDepartmentListPojosNew);
                managerDepartmentDialog.dismiss();
                break;
            case 5:
                showToast("删除成功");
                List<ManagePersonPojo> managerDepartmentListPojoss = managePersonAdapter.getData();
                managerDepartmentListPojoss.remove(po);
                managePersonAdapter.setNewData(managerDepartmentListPojoss);
                break;
            case 6:
                final Map<String,String> postMap = new HashMap<>();
                managerPostPojos = AppJsonUtil.getArrayList(result,ManagerPostPojo.class);
                for(int i = 0; i < managerPostPojos.size(); i++){
                    postMap.put(String.valueOf(managerPostPojos.get(i).getPositionId()),
                            managerPostPojos.get(i).getPositionName());
                }
                managerUpPersonDialog.setPostMap(postMap);
                managerUpPersonDialog.setSpinnerB(getActivity());
                managerUpPersonDialog.setSpinnerPostOnItemClick(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectStr = parent.getItemAtPosition(position).toString();
                        String key = HashMapUtils.getKey((HashMap<String, String>) postMap,selectStr);
                        upPositionId = key;
                        upPositionName = selectStr;
                        Log.v("print","key：" + key + "---value：" + selectStr);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case 7:
                showToast("修改成功");
                ManagePersonPojo managePersonPojo = managePersonAdapter.getItem(personPo);
                managePersonPojo.setUser_name(upUserName);
                managePersonPojo.setDepartment_name(upDepartmentName);
                managePersonPojo.setPosition_name(upPositionName);
                managePersonAdapter.setData(personPo,managePersonPojo);
                managerUpPersonDialog.dismiss();
                upDepartmentId = "0";
                upDepartmentName = "";
                upPositionId = "0";
                upUserName = "";
                break;
            case 8:
                final Map<String,String> postAddMap = new HashMap<>();
                managerPostPojos = AppJsonUtil.getArrayList(result,ManagerPostPojo.class);
                for(int i = 0; i < managerPostPojos.size(); i++){
                    postAddMap.put(String.valueOf(managerPostPojos.get(i).getPositionId()),
                            managerPostPojos.get(i).getPositionName());
                }
                managerAddPersonDialog.setPostMap(postAddMap);
                managerAddPersonDialog.setSpinnerPost(getActivity());
                managerAddPersonDialog.setSpinnerPostOnItemClick(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectStr = parent.getItemAtPosition(position).toString();
                        String key = HashMapUtils.getKey((HashMap<String, String>) postAddMap,selectStr);
                        addPositionId = key;
                        addPositionName = selectStr;
                        Log.v("print","key：" + key + "---value：" + selectStr);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case 9:
                showToast("添加成功");
                ManagePersonPojo managePersonPojo1 = new ManagePersonPojo();
                managePersonPojo1.setDepartment_id(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"user_name"));
                managePersonPojo1.setUser_id(Integer.parseInt(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"user_id")));
                managePersonPojo1.setUser_name(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"user_name"));
                managePersonPojo1.setDepartment_name(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"department_name"));
                managePersonPojo1.setPosition_name(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"position_name"));
                managePersonPojo1.setUser_account(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"user_account"));
                managePersonPojo1.setPosition_id(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"position_id"));
                managePersonAdapter.addData(managePersonPojo1);
                managerAddPersonDialog.dismiss();
                addDepartmentId = "0";
                addDepartmentName = "";
                addPositionId = "0";
                addPositionName = "";
                break;
            case 10:
                showToast("重置该人员密码成功");
                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        if (ptrFrameLayout != null) {
            ptrFrameLayout.refreshComplete();
            managePersonAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        if (ptrFrameLayout != null) {
            ptrFrameLayout.refreshComplete();
            managePersonAdapter.loadMoreComplete();
        }
    }

    public void showSelect(){
        managerDepartmentDialog = new ManagerDepartmentDialog(getActivity());
        managerDepartmentDialog.show();
        managerDepartmentDialog.setDialogAnimation();
        managerDepartmentDialog.setTitle("请选择单位");
        managerDepartmentDialog.setRv(getActivity(),managerDepartmentListPojos);
        managerDepartmentDialog.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<ManagerDepartmentListPojo>() {
            @Override
            public void onItemClick(BaseQuickAdapter<ManagerDepartmentListPojo, ? extends BaseViewHolder> adapter, View view, int position) {
                departmentId = managerDepartmentDialog.getAdapter().getItem(position).getDepartmentId();
                pager = 1;
                po = position;
                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,departmentId),4);
            }
        });
    }
}
