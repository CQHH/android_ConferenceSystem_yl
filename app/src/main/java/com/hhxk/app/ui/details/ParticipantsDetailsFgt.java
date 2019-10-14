package com.hhxk.app.ui.details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.listview.ListViewForScrollView;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.ParticipantsDeatilsCAdapter;
import com.hhxk.app.adapter.ParticipantsDeatilsZAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.ParticipantsCPojo;
import com.hhxk.app.pojo.ParticipantsDeatilsCPojo;
import com.hhxk.app.pojo.ParticipantsDeatilsZPojo;
import com.hhxk.app.pojo.ParticipantsSPojo;
import com.hhxk.app.pojo.ParticipantsZPojo;
import com.hhxk.app.pojo.SelectChoicePersonCPojo;
import com.hhxk.app.pojo.SelectChoicePersonCYPojo;
import com.hhxk.app.pojo.SelectChoicePersonZPojo;
import com.hhxk.app.pojo.SelectChoicePersonZYPojo;
import com.hhxk.app.util.HashMapUtils;
import com.hhxk.app.view.SelectPersonCDialog;
import com.hhxk.app.view.SelectPersonZDialog;
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
 * @title  会议详情-会议详情嵌套的参会人员fragemnt
 * @date   2019/02/25
 * @author enmaoFu
 */
public class ParticipantsDetailsFgt extends BaseLazyFgt {

    @BindView(R.id.listview_z)
    ListViewForScrollView listviewZ;
    @BindView(R.id.listview_c)
    ListViewForScrollView listviewC;
    @BindView(R.id.add_z)
    TextView addZ;
    @BindView(R.id.add_c)
    TextView addC;
    @BindView(R.id.text_z)
    TextView textZ;
    @BindView(R.id.text_c)
    TextView textC;

    private SelectPersonCDialog selectPersonCDialog;
    private SelectPersonZDialog selectPersonZDialog;

    private MMKV kv;

    /**
     * 适配器
     */
    private ParticipantsDeatilsZAdapter participantsDeatilsZAdapter;

    /**
     * 数据源
     */
    private List<ParticipantsDeatilsZPojo> participantsDeatilsZPojos;

    /**
     * 适配器
     */
    private ParticipantsDeatilsCAdapter participantsDeatilsCAdapter;

    /**
     * 数据源
     */
    private List<ParticipantsDeatilsCPojo> participantsDeatilsCPojos;

    /**
     * 主持人删除弹框
     */
    private DialogInterface participantsZDialog;

    /**
     * 主持人下标
     */
    private int participantsZPo;

    /**
     * 参会人员下标
     */
    private int participantsCPo;

    /**
     * 参会人员删除弹框
     */
    private DialogInterface participantsCDialog;

    /**
     * 选择部门实体类
     */
    private List<ParticipantsSPojo> participantsSPojos;

    /**
     * 主持人员map
     */
    private Map<String,String> mapZ;

    /**
     * 主持会议人员已选择
     */
    private List<SelectChoicePersonZYPojo> selectChoicePersonZYPojos;

    /**
     * 会议主持人名字集合
     */
    private List<String> stringZList;

    /**
     * 部门编号
     */
    private String departmentId;

    /**
     * 当前页数
     */
    private int pager = 1;

    /**
     * 主持人员根据部门返回数据的实体类数据源
     */
    private List<SelectChoicePersonZPojo> selectChoicePersonZPojos;

    /**
     * 参会人员map
     */
    private Map<String,String> mapC;

    /**
     * 参会人员已选择
     */
    private List<SelectChoicePersonCYPojo> selectChoicePersonCYPojos;

    /**
     * 参会人员名字集合
     */
    private List<String> stringCList;

    /**
     * 参会人员根据部门返回数据的实体类
     */
    private List<SelectChoicePersonCPojo> selectChoicePersonCPojos;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query_details_participants;
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();

        if("2".equals(kv.decodeString("role_id"))){
            addZ.setVisibility(View.GONE);
            addC.setVisibility(View.GONE);
            textZ.setVisibility(View.VISIBLE);
            textC.setVisibility(View.VISIBLE);
        }else{
            addZ.setVisibility(View.VISIBLE);
            addC.setVisibility(View.VISIBLE);
            textZ.setVisibility(View.GONE);
            textC.setVisibility(View.GONE);
        }

        participantsDeatilsZAdapter = new ParticipantsDeatilsZAdapter(getActivity(),new ArrayList<ParticipantsDeatilsZPojo>(),R.layout.item_participants_deatils);
        listviewZ.setAdapter(participantsDeatilsZAdapter);
        participantsDeatilsZAdapter.setDatas(getZData());
        participantsDeatilsZAdapter.setOnDeleteItemListener(new ParticipantsDeatilsZAdapter.OnDeleteItemListener() {
            @Override
            public void onItemClick(ParticipantsDeatilsZPojo participantsDeatilsZPojo, final int positon) {
                participantsZPo = positon;
                new AlertDialog.Builder(getActivity()).setTitle("是否删除该人员?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                participantsZDialog = dialog;
                                int meetingId = participantsDeatilsZAdapter.getItem(positon).getMeeting_id();
                                int userId = participantsDeatilsZAdapter.getItem(positon).getUser_id();
                                showLoadingDialog(null);
                                doHttp(RetrofitUtils.createApi(Http.class).deleteMeetingHost(String.valueOf(meetingId),String.valueOf(userId)),1);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });

        participantsDeatilsCAdapter = new ParticipantsDeatilsCAdapter(getActivity(),new ArrayList<ParticipantsDeatilsCPojo>(),R.layout.item_participants_deatils);
        listviewC.setAdapter(participantsDeatilsCAdapter);
        participantsDeatilsCAdapter.setDatas(getCData());
        participantsDeatilsCAdapter.setOnDeleteItemListener(new ParticipantsDeatilsCAdapter.OnDeleteItemListener() {
            @Override
            public void onItemClick(ParticipantsDeatilsCPojo participantsDeatilsCPojo, final int positon) {
                participantsCPo = positon;
                new AlertDialog.Builder(getActivity()).setTitle("是否删除该人员?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                participantsCDialog = dialog;
                                int meetingId = participantsDeatilsCAdapter.getItem(positon).getMeeting_id();
                                int userId = participantsDeatilsCAdapter.getItem(positon).getUser_id();
                                showLoadingDialog(null);
                                doHttp(RetrofitUtils.createApi(Http.class).deleteMeetingHost(String.valueOf(meetingId),String.valueOf(userId)),2);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });
    }

    @Override
    protected boolean setIsInitRequestData() {
        return false;
    }

    @Override
    protected void requestData() {

    }

    @OnClick({R.id.add_z,R.id.add_c})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.add_z:
                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Http.class).findDept(),3);
                break;
            case R.id.add_c:
                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Http.class).findDept(),7);
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                participantsDeatilsZPojos = AppJsonUtil.getArrayList(result,"hostList",ParticipantsDeatilsZPojo.class);
                for(int i = 0; i < participantsDeatilsZPojos.size(); i++){
                    participantsDeatilsZPojos.get(i).setUser_role_id(kv.decodeString("role_id"));
                }
                participantsDeatilsZAdapter.setDatas(participantsDeatilsZPojos);

                participantsDeatilsCPojos = AppJsonUtil.getArrayList(result,"otherList",ParticipantsDeatilsCPojo.class);
                for(int i = 0; i < participantsDeatilsCPojos.size(); i++){
                    participantsDeatilsCPojos.get(i).setUser_role_id(kv.decodeString("role_id"));
                }
                participantsDeatilsCAdapter.setDatas(participantsDeatilsCPojos);
                participantsZDialog.dismiss();
                break;
            case 2:
                participantsDeatilsZPojos = AppJsonUtil.getArrayList(result,"hostList",ParticipantsDeatilsZPojo.class);
                for(int i = 0; i < participantsDeatilsZPojos.size(); i++){
                    participantsDeatilsZPojos.get(i).setUser_role_id(kv.decodeString("role_id"));
                }
                participantsDeatilsZAdapter.setDatas(participantsDeatilsZPojos);

                participantsDeatilsCPojos = AppJsonUtil.getArrayList(result,"otherList",ParticipantsDeatilsCPojo.class);
                for(int i = 0; i < participantsDeatilsCPojos.size(); i++){
                    participantsDeatilsCPojos.get(i).setUser_role_id(kv.decodeString("role_id"));
                }
                participantsDeatilsCAdapter.setDatas(participantsDeatilsCPojos);
                participantsCDialog.dismiss();
                break;
            case 3:
                participantsSPojos = AppJsonUtil.getArrayList(result,ParticipantsSPojo.class);
                if(participantsSPojos.size() == 0){
                    showErrorToast("暂时无人员可以添加");
                }else{
                    if(null == mapZ){
                        mapZ = new HashMap<>();
                    }
                    for(ParticipantsSPojo participantsSPojo:participantsSPojos){
                        mapZ.put(String.valueOf(participantsSPojo.getDepartmentId()),participantsSPojo.getDepartmentName());
                    }
                    selectPersonZDialog = new SelectPersonZDialog(getActivity());
                    selectPersonZDialog.show();
                    selectPersonZDialog.setDialogAnimation();
                    selectPersonZDialog.setSpinner(getActivity(),mapZ);
                    selectPersonZDialog.ClearOnClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectChoicePersonZYPojos.clear();
                            stringZList.clear();
                            selectPersonZDialog.dismiss();
                        }
                    });
                    selectPersonZDialog.setSpinnerOnItemClick(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectZStr = parent.getItemAtPosition(position).toString();
                            String selectZkey = HashMapUtils.getKey((HashMap<String, String>) mapZ,selectZStr);
                            departmentId = HashMapUtils.getKey((HashMap<String, String>) mapZ,selectZStr);
                            pager = 1;
                            doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,Integer.parseInt(selectZkey)),4);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                break;
            case 4:
                if(selectChoicePersonZYPojos == null){
                    selectChoicePersonZYPojos = new ArrayList<>();
                }
                if(stringZList == null){
                    stringZList = new ArrayList<>();
                }
                selectPersonZDialog.setRvZ(getActivity(),selectChoicePersonZYPojos);
                //点击添加，将已选择列表的数据获取到，放到已选择人员列表中，再将已选择人员列表中的人员名字提取到List<string>中用于后续判断
                List<ParticipantsDeatilsZPojo> psz = participantsDeatilsZAdapter.findAll();
                if(null != psz && psz.size() != 0){
                    selectChoicePersonZYPojos.clear();
                    SelectChoicePersonZYPojo selectChoicePersonZYPojo = null;
                    for(ParticipantsDeatilsZPojo pc:psz){
                        selectChoicePersonZYPojo = new SelectChoicePersonZYPojo();
                        selectChoicePersonZYPojo.setUser_id(pc.getUser_id());
                        selectChoicePersonZYPojo.setUser_name(pc.getUser_name());
                        selectChoicePersonZYPojo.setUser_account(pc.getUser_account());
                        selectChoicePersonZYPojo.setPosition_id(String.valueOf(pc.getPosition_id()));
                        selectChoicePersonZYPojo.setPosition_name(pc.getPosition_name());
                        selectChoicePersonZYPojo.setDepartment_id(String.valueOf(pc.getDepartment_id()));
                        selectChoicePersonZYPojo.setDepartment_name(pc.getDepartment_name());
                        selectChoicePersonZYPojos.add(selectChoicePersonZYPojo);
                    }
                    selectPersonZDialog.getZYAdapter().setNewData(selectChoicePersonZYPojos);
                    stringZList.clear();
                    for(int i = 0; i < selectChoicePersonZYPojos.size(); i++){
                        stringZList.add(selectChoicePersonZYPojos.get(i).getUser_name());
                    }
                }

                //得到根据部门获取的人员
                selectChoicePersonZPojos = AppJsonUtil.getArrayList(result,"content",SelectChoicePersonZPojo.class);
                selectPersonZDialog.setRv(getActivity(),selectChoicePersonZPojos);
                selectPersonZDialog.getOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        if (pager == 1) {
                            selectPersonZDialog.getAdapter().loadMoreEnd();
                            return;
                        }
                        doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,Integer.parseInt(departmentId)),5);
                    }
                });

                selectPersonZDialog.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<SelectChoicePersonZPojo>() {
                    @Override
                    public void onItemClick(BaseQuickAdapter<SelectChoicePersonZPojo, ? extends BaseViewHolder> adapter, View view, int position) {
                        //获取点击的Item数据
                        SelectChoicePersonZPojo selectChoicePersonZPojo = selectPersonZDialog.getAdapter().getItem(position);
                        //点击请选择人员列表进行判断，是否和已选择人员列表重复
                        SelectChoicePersonZYPojo selectChoicePersonZYPojo = null;
                        if(stringZList.contains(selectChoicePersonZPojo.getUser_name()) == false){
                            selectChoicePersonZYPojo = new SelectChoicePersonZYPojo();
                            selectChoicePersonZYPojo.setUser_id(selectChoicePersonZPojo.getUser_id());
                            selectChoicePersonZYPojo.setUser_name(selectChoicePersonZPojo.getUser_name());
                            selectChoicePersonZYPojo.setDepartment_id(selectChoicePersonZPojo.getDepartment_id());
                            selectChoicePersonZYPojo.setDepartment_name(selectChoicePersonZPojo.getDepartment_name());
                            selectChoicePersonZYPojo.setPosition_id(selectChoicePersonZPojo.getPosition_id());
                            selectChoicePersonZYPojo.setPosition_name(selectChoicePersonZPojo.getPosition_name());
                            selectChoicePersonZYPojo.setUser_account(selectChoicePersonZPojo.getUser_account());
                            selectChoicePersonZYPojos.add(selectChoicePersonZYPojo);
                            stringZList.add(selectChoicePersonZPojo.getUser_name());
                            selectPersonZDialog.getZYAdapter().setNewData(selectChoicePersonZYPojos);
                        }else{
                            showErrorToast("该人员已经选择");
                        }
                        Log.v("print",stringZList.size() + "-----");
                    }
                });

                selectPersonZDialog.BtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectPersonZDialog.getZYAdapter().getData().size() != 0){
                            participantsDeatilsZPojos = new ArrayList<>();
                            ParticipantsDeatilsZPojo participantsDeatilsZPojo = null;
                            for(int i = 0; i < selectChoicePersonZYPojos.size(); i++){
                                participantsDeatilsZPojo = new ParticipantsDeatilsZPojo();
                                participantsDeatilsZPojo.setUser_id(selectChoicePersonZYPojos.get(i).getUser_id());
                                participantsDeatilsZPojo.setDepartment_id(Integer.valueOf(selectChoicePersonZYPojos.get(i).getDepartment_id()));
                                participantsDeatilsZPojo.setUser_name(selectChoicePersonZYPojos.get(i).getUser_name());
                                participantsDeatilsZPojo.setDepartment_name(selectChoicePersonZYPojos.get(i).getDepartment_name());
                                participantsDeatilsZPojo.setPosition_name(selectChoicePersonZYPojos.get(i).getPosition_name());
                                participantsDeatilsZPojos.add(participantsDeatilsZPojo);
                            }
                            List<String> httpStr = new ArrayList<>();
                            for(ParticipantsDeatilsZPojo pz:participantsDeatilsZPojos){
                                httpStr.add(String.valueOf(pz.getUser_id()));
                            }
                            showLoadingDialog(null);
                            doHttp(RetrofitUtils.createApi(Http.class).addMeetingHost(httpStr,kv.decodeInt("meeting_id_details")),6);
                        }else{
                            showErrorToast("请选择会议主持人");
                        }
                    }
                });
                pager++;
                break;
            case 5:
                //加载更多
                selectChoicePersonZPojos = AppJsonUtil.getArrayList(result,"content",SelectChoicePersonZPojo.class);

                if (selectChoicePersonZPojos != null && selectChoicePersonZPojos.size() > 0) {
                    selectPersonZDialog.addRvData(selectChoicePersonZPojos);
                    selectPersonZDialog.getAdapter().loadMoreComplete();
                } else {
                    selectPersonZDialog.getAdapter().loadMoreEnd();
                }

                //增加页码
                pager++;
                break;
            case 6:
                participantsDeatilsZPojos = AppJsonUtil.getArrayList(result,"hostList",ParticipantsDeatilsZPojo.class);
                for(int i = 0; i < participantsDeatilsZPojos.size(); i++){
                    participantsDeatilsZPojos.get(i).setUser_role_id(kv.decodeString("role_id"));
                }
                participantsDeatilsZAdapter.setDatas(participantsDeatilsZPojos);

                participantsDeatilsCPojos = AppJsonUtil.getArrayList(result,"otherList",ParticipantsDeatilsCPojo.class);
                for(int i = 0; i < participantsDeatilsCPojos.size(); i++){
                    participantsDeatilsCPojos.get(i).setUser_role_id(kv.decodeString("role_id"));
                }
                participantsDeatilsCAdapter.setDatas(participantsDeatilsCPojos);

                /*participantsDeatilsCPojos = new ArrayList<>();
                ParticipantsDeatilsCPojo participantsDeatilsCPojo = null;
                for(ParticipantsDeatilsZPojo pzp:participantsDeatilsZPojos){
                    participantsDeatilsCPojo = new ParticipantsDeatilsCPojo();
                    participantsDeatilsCPojo.setUser_id(pzp.getUser_id());
                    participantsDeatilsCPojo.setPosition_name(pzp.getPosition_name());
                    participantsDeatilsCPojo.setUser_name(pzp.getUser_name());
                    participantsDeatilsCPojo.setDepartment_name(pzp.getDepartment_name());
                    participantsDeatilsCPojo.setDepartment_id(pzp.getDepartment_id());
                    participantsDeatilsCPojo.setMeeting_end(pzp.getMeeting_end());
                    participantsDeatilsCPojo.setMeeting_id(pzp.getMeeting_id());
                    participantsDeatilsCPojo.setMeeting_info(pzp.getMeeting_info());
                    participantsDeatilsCPojo.setMeeting_name(pzp.getMeeting_name());
                    participantsDeatilsCPojo.setMeeting_start(pzp.getMeeting_start());
                    participantsDeatilsCPojo.setMeeting_status(pzp.getMeeting_status());
                    participantsDeatilsCPojo.setMeeting_user_id(pzp.getMeeting_user_id());
                    participantsDeatilsCPojo.setMeeting_user_status(pzp.getMeeting_user_status());
                    participantsDeatilsCPojo.setPosition_id(pzp.getPosition_id());
                    participantsDeatilsCPojo.setUser_account(pzp.getUser_account());
                    participantsDeatilsCPojo.setUser_role_id(kv.decodeString("role_id"));
                    participantsDeatilsCPojo.setUser_status(pzp.getUser_status());
                    participantsDeatilsCPojo.setSign_in_status(pzp.getSign_in_status());
                    participantsDeatilsCPojos.add(participantsDeatilsCPojo);
                }
                participantsDeatilsCAdapter.setDatas(participantsDeatilsCPojos);*/

                selectPersonZDialog.dismiss();
                break;
            case 7:
                participantsSPojos = AppJsonUtil.getArrayList(result,ParticipantsSPojo.class);
                if(participantsSPojos.size() == 0){
                    showErrorToast("暂时无人员可以添加");
                }else{
                    if(null == mapC){
                        mapC = new HashMap<>();
                    }
                    for(ParticipantsSPojo participantsSPojo:participantsSPojos){
                        mapC.put(String.valueOf(participantsSPojo.getDepartmentId()),participantsSPojo.getDepartmentName());
                    }
                    selectPersonCDialog = new SelectPersonCDialog(getActivity());
                    selectPersonCDialog.setCanceledOnTouchOutside(false);
                    selectPersonCDialog.setCancelable(false);
                    selectPersonCDialog.show();
                    selectPersonCDialog.setDialogAnimation();
                    selectPersonCDialog.setSpinner(getActivity(),mapC);
                    selectPersonCDialog.ClearOnClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectChoicePersonCYPojos.clear();
                            stringCList.clear();
                            selectPersonCDialog.dismiss();
                        }
                    });
                    selectPersonCDialog.setSpinnerOnItemClick(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectCStr = parent.getItemAtPosition(position).toString();
                            departmentId = HashMapUtils.getKey((HashMap<String, String>) mapC,selectCStr);
                            Log.v("print",selectCStr + "-----cccc-----" + departmentId);
                            pager = 1;
                            doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,Integer.parseInt(departmentId)),8);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                break;
            case 8:
                if(selectChoicePersonCYPojos == null){
                    selectChoicePersonCYPojos = new ArrayList<>();
                }
                if(stringCList == null){
                    stringCList = new ArrayList<>();
                }
                selectPersonCDialog.setRvY(getActivity(),selectChoicePersonCYPojos);
                //点击添加，将已选择列表的数据获取到，放到已选择人员列表中，再将已选择人员列表中的人员名字提取到List<string>中用于后续判断
                List<ParticipantsDeatilsCPojo> pcs = participantsDeatilsCAdapter.findAll();
                if(null != pcs && pcs.size() != 0){
                    selectChoicePersonCYPojos.clear();
                    SelectChoicePersonCYPojo selectChoicePersonCYPojo = null;
                    for(ParticipantsDeatilsCPojo pc:pcs){
                        selectChoicePersonCYPojo = new SelectChoicePersonCYPojo();
                        selectChoicePersonCYPojo.setUser_id(pc.getUser_id());
                        selectChoicePersonCYPojo.setUser_name(pc.getUser_name());
                        selectChoicePersonCYPojo.setUser_account(pc.getUser_account());
                        selectChoicePersonCYPojo.setPosition_id(String.valueOf(pc.getPosition_id()));
                        selectChoicePersonCYPojo.setPosition_name(pc.getPosition_name());
                        selectChoicePersonCYPojo.setDepartment_id(String.valueOf(pc.getDepartment_id()));
                        selectChoicePersonCYPojo.setDepartment_name(pc.getDepartment_name());
                        selectChoicePersonCYPojos.add(selectChoicePersonCYPojo);
                    }

                    selectPersonCDialog.getCYAdapter().setNewData(selectChoicePersonCYPojos);
                    stringCList.clear();
                    for(int i = 0; i < selectChoicePersonCYPojos.size(); i++){
                        stringCList.add(selectChoicePersonCYPojos.get(i).getUser_name());
                    }
                }
                //得到根据部门获取的人员
                selectChoicePersonCPojos = AppJsonUtil.getArrayList(result,"content",SelectChoicePersonCPojo.class);
                selectPersonCDialog.setRv(getActivity(),selectChoicePersonCPojos);
                selectPersonCDialog.getOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        if (pager == 1) {
                            selectPersonCDialog.getAdapter().loadMoreEnd();
                            return;
                        }
                        doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,Integer.parseInt(departmentId)),9);
                    }
                });

                selectPersonCDialog.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<SelectChoicePersonCPojo>() {
                    @Override
                    public void onItemClick(BaseQuickAdapter<SelectChoicePersonCPojo, ? extends BaseViewHolder> adapter, View view, int position) {
                        //获取点击的Item数据
                        SelectChoicePersonCPojo selectChoicePersonCPojo = selectPersonCDialog.getAdapter().getItem(position);
                        //点击请选择人员列表进行判断，是否和已选择人员列表重复
                        SelectChoicePersonCYPojo selectChoicePersonCYPojo = null;
                        if(stringCList.contains(selectChoicePersonCPojo.getUser_name()) == false){
                            selectChoicePersonCYPojo = new SelectChoicePersonCYPojo();
                            selectChoicePersonCYPojo.setUser_id(selectChoicePersonCPojo.getUser_id());
                            selectChoicePersonCYPojo.setUser_name(selectChoicePersonCPojo.getUser_name());
                            selectChoicePersonCYPojo.setDepartment_id(selectChoicePersonCPojo.getDepartment_id());
                            selectChoicePersonCYPojo.setDepartment_name(selectChoicePersonCPojo.getDepartment_name());
                            selectChoicePersonCYPojo.setPosition_id(selectChoicePersonCPojo.getPosition_id());
                            selectChoicePersonCYPojo.setPosition_name(selectChoicePersonCPojo.getPosition_name());
                            selectChoicePersonCYPojo.setUser_account(selectChoicePersonCPojo.getUser_account());
                            selectChoicePersonCYPojos.add(selectChoicePersonCYPojo);
                            stringCList.add(selectChoicePersonCPojo.getUser_name());
                            selectPersonCDialog.getCYAdapter().setNewData(selectChoicePersonCYPojos);
                        }else{
                            showErrorToast("该人员已经选择");
                        }
                        Log.v("print",stringCList.size() + "-----");
                    }
                });

                selectPersonCDialog.BtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectPersonCDialog.getCYAdapter().getData().size() != 0){
                            participantsDeatilsCPojos = new ArrayList<>();
                            ParticipantsDeatilsCPojo participantsDeatilsCPojo = null;
                            for(int i = 0; i < selectChoicePersonCYPojos.size(); i++){
                                participantsDeatilsCPojo = new ParticipantsDeatilsCPojo();
                                participantsDeatilsCPojo.setUser_id(selectChoicePersonCYPojos.get(i).getUser_id());
                                participantsDeatilsCPojo.setDepartment_id(Integer.valueOf(selectChoicePersonCYPojos.get(i).getDepartment_id()));
                                participantsDeatilsCPojo.setUser_name(selectChoicePersonCYPojos.get(i).getUser_name());
                                participantsDeatilsCPojo.setDepartment_name(selectChoicePersonCYPojos.get(i).getDepartment_name());
                                participantsDeatilsCPojo.setPosition_name(selectChoicePersonCYPojos.get(i).getPosition_name());
                                participantsDeatilsCPojos.add(participantsDeatilsCPojo);
                            }

                            List<String> httpStr = new ArrayList<>();
                            for(ParticipantsDeatilsCPojo pc:participantsDeatilsCPojos){
                                httpStr.add(String.valueOf(pc.getUser_id()));
                            }
                            showLoadingDialog(null);
                            doHttp(RetrofitUtils.createApi(Http.class).addMeetingOther(httpStr,kv.decodeInt("meeting_id_details")),10);
                        }else{
                            showErrorToast("请选择参会人员");
                        }
                    }
                });
                pager++;
                break;
            case 9:
                //加载更多
                selectChoicePersonCPojos = AppJsonUtil.getArrayList(result,"content",SelectChoicePersonCPojo.class);

                if (selectChoicePersonCPojos != null && selectChoicePersonCPojos.size() > 0) {
                    selectPersonCDialog.addRvData(selectChoicePersonCPojos);
                    selectPersonCDialog.getAdapter().loadMoreComplete();
                } else {
                    selectPersonCDialog.getAdapter().loadMoreEnd();
                }

                //增加页码
                pager++;
                break;
            case 10:
                participantsDeatilsZPojos = AppJsonUtil.getArrayList(result,"hostList",ParticipantsDeatilsZPojo.class);
                for(int i = 0; i < participantsDeatilsZPojos.size(); i++){
                    participantsDeatilsZPojos.get(i).setUser_role_id(kv.decodeString("role_id"));
                }
                participantsDeatilsZAdapter.setDatas(participantsDeatilsZPojos);

                participantsDeatilsCPojos = AppJsonUtil.getArrayList(result,"otherList",ParticipantsDeatilsCPojo.class);
                for(int i = 0; i < participantsDeatilsCPojos.size(); i++){
                    participantsDeatilsCPojos.get(i).setUser_role_id(kv.decodeString("role_id"));
                }
                participantsDeatilsCAdapter.setDatas(participantsDeatilsCPojos);
                selectPersonCDialog.dismiss();
                break;
        }
    }

    public List<ParticipantsDeatilsZPojo> getZData(){
        if(null != EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos){
            if(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.size() != 0){
                participantsDeatilsZPojos = new ArrayList<>();
                ParticipantsDeatilsZPojo participantsDeatilsZPojo = null;
                for(int i = 0; i < EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.size(); i++){
                    participantsDeatilsZPojo = new ParticipantsDeatilsZPojo();
                    participantsDeatilsZPojo.setDepartment_id(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getDepartment_id());
                    participantsDeatilsZPojo.setDepartment_name(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getDepartment_name());
                    participantsDeatilsZPojo.setMeeting_end(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getMeeting_end());
                    participantsDeatilsZPojo.setMeeting_id(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getMeeting_id());
                    participantsDeatilsZPojo.setMeeting_info(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getMeeting_info());
                    participantsDeatilsZPojo.setMeeting_name(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getMeeting_name());
                    participantsDeatilsZPojo.setMeeting_start(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getMeeting_start());
                    participantsDeatilsZPojo.setMeeting_status(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getMeeting_status());
                    participantsDeatilsZPojo.setMeeting_user_status(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getMeeting_user_status());
                    participantsDeatilsZPojo.setPosition_id(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getPosition_id());
                    participantsDeatilsZPojo.setPosition_name(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getPosition_name());
                    participantsDeatilsZPojo.setUser_account(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getUser_account());
                    participantsDeatilsZPojo.setUser_id(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getUser_id());
                    participantsDeatilsZPojo.setUser_name(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getUser_name());
                    participantsDeatilsZPojo.setUser_role_id(kv.decodeString("role_id"));
                    participantsDeatilsZPojo.setMeeting_user_id(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getMeeting_user_id());
                    participantsDeatilsZPojo.setSign_in_status(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getSign_in_status());
                    participantsDeatilsZPojo.setMeeting_address(EssentialinformationDetailsFgt.essentialinformationDetailsHostPojos.get(i).getMeeting_address());
                    participantsDeatilsZPojos.add(participantsDeatilsZPojo);
                }
            }else{
                return participantsDeatilsZPojos;
            }
        }
        return participantsDeatilsZPojos;
    }

    public List<ParticipantsDeatilsCPojo> getCData(){
        if(null != EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos){
            if(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.size() != 0){
                participantsDeatilsCPojos = new ArrayList<>();
                ParticipantsDeatilsCPojo participantsDeatilsCPojo = null;
                for(int i = 0; i < EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.size(); i++){
                    participantsDeatilsCPojo = new ParticipantsDeatilsCPojo();
                    participantsDeatilsCPojo.setDepartment_id(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getDepartment_id());
                    participantsDeatilsCPojo.setDepartment_name(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getDepartment_name());
                    participantsDeatilsCPojo.setMeeting_end(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getMeeting_end());
                    participantsDeatilsCPojo.setMeeting_id(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getMeeting_id());
                    participantsDeatilsCPojo.setMeeting_info(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getMeeting_info());
                    participantsDeatilsCPojo.setMeeting_name(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getMeeting_name());
                    participantsDeatilsCPojo.setMeeting_start(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getMeeting_start());
                    participantsDeatilsCPojo.setMeeting_status(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getMeeting_status());
                    participantsDeatilsCPojo.setMeeting_user_status(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getMeeting_user_status());
                    participantsDeatilsCPojo.setPosition_id(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getPosition_id());
                    participantsDeatilsCPojo.setPosition_name(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getPosition_name());
                    participantsDeatilsCPojo.setUser_account(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getUser_account());
                    participantsDeatilsCPojo.setUser_id(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getUser_id());
                    participantsDeatilsCPojo.setUser_name(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getUser_name());
                    participantsDeatilsCPojo.setUser_role_id(kv.decodeString("role_id"));
                    participantsDeatilsCPojo.setMeeting_user_id(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getMeeting_user_id());
                    participantsDeatilsCPojo.setSign_in_status(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getSign_in_status());
                    participantsDeatilsCPojo.setMeeting_address(EssentialinformationDetailsFgt.essentialinformationDetailsOtherPojos.get(i).getMeeting_address());
                    participantsDeatilsCPojos.add(participantsDeatilsCPojo);
                }
            }else{
                return participantsDeatilsCPojos;
            }
        }
        return participantsDeatilsCPojos;
    }

}
