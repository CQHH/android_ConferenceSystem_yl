package com.hhxk.app.ui.sponsor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.listview.ListViewForScrollView;
import com.hhxk.app.R;
import com.hhxk.app.adapter.ParticipantsCAdapter;
import com.hhxk.app.adapter.ParticipantsZAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.PreservationEvent;
import com.hhxk.app.event.SponsorClearEvent;
import com.hhxk.app.event.SponsorEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.ParticipantsCPojo;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * @title  发起会议-参会人员fragemnt
 * @date   2019/02/20
 * @author enmaoFu
 */
public class ParticipantsFgt extends BaseLazyFgt {

    @BindView(R.id.listview_c)
    ListViewForScrollView listviewC;
    @BindView(R.id.listview_z)
    ListViewForScrollView listviewZ;

    private SelectPersonCDialog selectPersonCDialog;
    private SelectPersonZDialog selectPersonZDialog;

    /**
     * 适配器
     */
    private ParticipantsCAdapter participantsCAdapter;

    /**
     * 数据源
     */
    private List<ParticipantsCPojo> participantsCPojos;

    /**
     * 参会人员map
     */
    private Map<String,String> mapC;

    /**
     * 适配器
     */
    private ParticipantsZAdapter participantsZAdapter;

    /**
     * 数据源
     */
    private List<ParticipantsZPojo> participantsZPojos;

    /**
     * 主持人员map
     */
    private Map<String,String> mapZ;

    /**
     * 当前页数
     */
    private int pager = 1;

    /**
     * 每页条数
     */
    private int pageSize = 3;

    /**
     * 选择部门实体类
     */
    private List<ParticipantsSPojo> participantsSPojos;

    /**
     * 参会人员根据部门返回数据的实体类
     */
    private List<SelectChoicePersonCPojo> selectChoicePersonCPojos;

    /**
     * 主持人员根据部门返回数据的实体类数据源
     */
    private List<SelectChoicePersonZPojo> selectChoicePersonZPojos;

    /**
     * 参会人员已选择
     */
    private List<SelectChoicePersonCYPojo> selectChoicePersonCYPojos;

    /**
     * 主持会议人员已选择
     */
    private List<SelectChoicePersonZYPojo> selectChoicePersonZYPojos;

    /**
     * 参会人员名字集合
     */
    private List<String> stringCList;

    /**
     * 会议主持人名字集合
     */
    private List<String> stringZList;

    private String departmentId;

    private MMKV kv;

    /**
     * 主持人下标
     */
    private int participantsZPo;

    /**
     * 主持人删除弹框
     */
    private DialogInterface participantsZDialog;

    /**
     * 参会人员下标
     */
    private int participantsCPo;

    /**
     * 参会人员删除弹框
     */
    private DialogInterface participantsCDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_participants;
    }

    @Override
    protected void initData() {

        EventBus.getDefault().register(this);
        kv = MMKV.defaultMMKV();

        participantsZAdapter = new ParticipantsZAdapter(getActivity(),new ArrayList<ParticipantsZPojo>(),R.layout.item_participants);
        listviewZ.setAdapter(participantsZAdapter);
        participantsZAdapter.setOnDeleteItemListener(new ParticipantsZAdapter.OnDeleteItemListener() {
            @Override
            public void onItemClick(ParticipantsZPojo participantsZPojo, final int positon) {
                participantsZPo = positon;
                new AlertDialog.Builder(getActivity()).setTitle("是否删除该人员?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                participantsZDialog = dialog;
                                int meetingId = participantsZAdapter.getItem(positon).getMeeting_id();
                                int userId = participantsZAdapter.getItem(positon).getUser_id();
                                showLoadingDialog(null);
                                doHttp(RetrofitUtils.createApi(Http.class).deleteMeetingHost(String.valueOf(meetingId),String.valueOf(userId)),9);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });

        participantsCAdapter = new ParticipantsCAdapter(getActivity(),new ArrayList<ParticipantsCPojo>(),R.layout.item_participants);
        listviewC.setAdapter(participantsCAdapter);
        participantsCAdapter.setOnDeleteItemListener(new ParticipantsCAdapter.OnDeleteItemListener() {
            @Override
            public void onItemClick(ParticipantsCPojo medicalAddCasePojo, final int positon) {
                participantsCPo = positon;
                new AlertDialog.Builder(getActivity()).setTitle("是否删除该人员?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                participantsCDialog = dialog;
                                int meetingId = participantsCAdapter.getItem(positon).getMeeting_id();
                                int userId = participantsCAdapter.getItem(positon).getUser_id();
                                showLoadingDialog(null);
                                doHttp(RetrofitUtils.createApi(Http.class).deleteMeetingHost(String.valueOf(meetingId),String.valueOf(userId)),10);
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

    @OnClick({R.id.back,R.id.add_c,R.id.add_z,R.id.preservation,R.id.next})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.back:
                SponsorEvent se = new SponsorEvent();
                se.setCode("参会人员-上");
                EventBus.getDefault().post(se);
                break;
            case R.id.add_c:
                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Http.class).findDept(),1);
                break;
            case R.id.add_z:
                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Http.class).findDept(),2);
                break;
            case R.id.preservation:
                if(participantsCAdapter.findAll().size() != 0 && participantsZAdapter.findAll().size() != 0){
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Http.class).generateMeeting(kv.decodeInt("meetingId")),11);
                }else{
                    showErrorToast("请选择主持人与参会人员");
                }
                break;
            case R.id.next:
                if(participantsCAdapter.findAll().size() != 0 && participantsZAdapter.findAll().size() != 0){
                    SponsorEvent seTwo = new SponsorEvent();
                    seTwo.setCode("参会人员-下");
                    EventBus.getDefault().post(seTwo);
                }else{
                    showErrorToast("请选择主持人与参会人员");
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, final Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
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
                            doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,Integer.parseInt(departmentId)),3);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                break;
            case 2:
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
                            Log.v("print",selectZStr + "-----zzzz-----" + selectZkey);
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
            case 3:

                if(selectChoicePersonCYPojos == null){
                    selectChoicePersonCYPojos = new ArrayList<>();
                }
                if(stringCList == null){
                    stringCList = new ArrayList<>();
                }
                selectPersonCDialog.setRvY(getActivity(),selectChoicePersonCYPojos);

                //点击添加，将已选择列表的数据获取到，放到已选择人员列表中，再将已选择人员列表中的人员名字提取到List<string>中用于后续判断
                List<ParticipantsCPojo> pcs = participantsCAdapter.findAll();
                if(pcs.size() != 0){
                    selectChoicePersonCYPojos.clear();
                    SelectChoicePersonCYPojo selectChoicePersonCYPojo = null;
                    for(ParticipantsCPojo pc:pcs){
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
                        doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,Integer.parseInt(departmentId)),5);
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

                /*selectPersonCDialog.getCYAdapter().setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<SelectChoicePersonCYPojo>() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter<SelectChoicePersonCYPojo, ? extends BaseViewHolder> adapter, View view, int position) {
                        selectPersonCDialog.getCYAdapter().remove(position);
                        stringCList.remove(position);
                    }
                });*/
                selectPersonCDialog.BtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectPersonCDialog.getCYAdapter().getData().size() != 0){
                            participantsCPojos = new ArrayList<>();
                            ParticipantsCPojo participantsCPojo = null;
                            for(int i = 0; i < selectChoicePersonCYPojos.size(); i++){
                                participantsCPojo = new ParticipantsCPojo();
                                participantsCPojo.setUser_id(selectChoicePersonCYPojos.get(i).getUser_id());
                                participantsCPojo.setDepartment_id(Integer.valueOf(selectChoicePersonCYPojos.get(i).getDepartment_id()));
                                participantsCPojo.setUser_name(selectChoicePersonCYPojos.get(i).getUser_name());
                                participantsCPojo.setDepartment_name(selectChoicePersonCYPojos.get(i).getDepartment_name());
                                participantsCPojo.setPosition_name(selectChoicePersonCYPojos.get(i).getPosition_name());
                                participantsCPojos.add(participantsCPojo);
                            }

                            List<String> httpStr = new ArrayList<>();
                            for(ParticipantsCPojo pc:participantsCPojos){
                                httpStr.add(String.valueOf(pc.getUser_id()));
                            }
                            showLoadingDialog(null);
                            doHttp(RetrofitUtils.createApi(Http.class).addMeetingOther(httpStr,kv.decodeInt("meetingId")),7);
                        }else{
                            showErrorToast("请选择参会人员");
                        }
                    }
                });
                pager++;
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
                List<ParticipantsZPojo> psz = participantsZAdapter.findAll();
                if(psz.size() != 0){
                    selectChoicePersonZYPojos.clear();
                    SelectChoicePersonZYPojo selectChoicePersonZYPojo = null;
                    for(ParticipantsZPojo pc:psz){
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
                        doHttp(RetrofitUtils.createApi(Http.class).findUserList(pager,Integer.parseInt(departmentId)),6);
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
                /*selectPersonZDialog.getZYAdapter().setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<SelectChoicePersonZYPojo>() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter<SelectChoicePersonZYPojo, ? extends BaseViewHolder> adapter, View view, int position) {
                        selectPersonZDialog.getZYAdapter().remove(position);
                        stringZList.remove(position);
                    }
                });*/
                selectPersonZDialog.BtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectPersonZDialog.getZYAdapter().getData().size() != 0){
                            participantsZPojos = new ArrayList<>();
                            ParticipantsZPojo participantsZPojo = null;
                            for(int i = 0; i < selectChoicePersonZYPojos.size(); i++){
                                participantsZPojo = new ParticipantsZPojo();
                                participantsZPojo.setUser_id(selectChoicePersonZYPojos.get(i).getUser_id());
                                participantsZPojo.setDepartment_id(Integer.valueOf(selectChoicePersonZYPojos.get(i).getDepartment_id()));
                                participantsZPojo.setUser_name(selectChoicePersonZYPojos.get(i).getUser_name());
                                participantsZPojo.setDepartment_name(selectChoicePersonZYPojos.get(i).getDepartment_name());
                                participantsZPojo.setPosition_name(selectChoicePersonZYPojos.get(i).getPosition_name());
                                participantsZPojos.add(participantsZPojo);
                            }
                            List<String> httpStr = new ArrayList<>();
                            for(ParticipantsZPojo pz:participantsZPojos){
                                httpStr.add(String.valueOf(pz.getUser_id()));
                            }
                            showLoadingDialog(null);
                            doHttp(RetrofitUtils.createApi(Http.class).addMeetingHost(httpStr,kv.decodeInt("meetingId")),8);
                        }else{
                            showErrorToast("请选择会议主持人");
                        }
                    }
                });
                pager++;
                break;
            case 5:
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
            case 6:
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
            case 7:
                participantsZPojos = AppJsonUtil.getArrayList(result,"hostList",ParticipantsZPojo.class);
                participantsZAdapter.setDatas(participantsZPojos);

                participantsCPojos = AppJsonUtil.getArrayList(result,"otherList",ParticipantsCPojo.class);
                participantsCAdapter.setDatas(participantsCPojos);
                selectPersonCDialog.dismiss();
                break;
            case 8:
                participantsZPojos = AppJsonUtil.getArrayList(result,"hostList",ParticipantsZPojo.class);
                participantsZAdapter.setDatas(participantsZPojos);

                participantsCPojos = AppJsonUtil.getArrayList(result,"otherList",ParticipantsCPojo.class);
                participantsCAdapter.setDatas(participantsCPojos);

                /*participantsCPojos = new ArrayList<>();
                ParticipantsCPojo pcp = null;
                for(ParticipantsZPojo pzp:participantsZPojos){
                    pcp = new ParticipantsCPojo();
                    pcp.setUser_id(pzp.getUser_id());
                    pcp.setPosition_name(pzp.getPosition_name());
                    pcp.setUser_name(pzp.getUser_name());
                    pcp.setDepartment_name(pzp.getDepartment_name());
                    pcp.setDepartment_id(pzp.getDepartment_id());
                    pcp.setMeeting_end(pzp.getMeeting_end());
                    pcp.setMeeting_id(pzp.getMeeting_id());
                    pcp.setMeeting_info(pzp.getMeeting_info());
                    pcp.setMeeting_name(pzp.getMeeting_name());
                    pcp.setMeeting_start(pzp.getMeeting_start());
                    pcp.setMeeting_status(pzp.getMeeting_status());
                    pcp.setMeeting_user_id(pzp.getMeeting_user_id());
                    pcp.setMeeting_user_status(pzp.getMeeting_user_status());
                    pcp.setPosition_id(pzp.getPosition_id());
                    pcp.setUser_account(pzp.getUser_account());
                    pcp.setUser_role_id(pzp.getUser_role_id());
                    pcp.setUser_status(pzp.getUser_status());
                    participantsCPojos.add(pcp);
                }
                participantsCAdapter.setDatas(participantsCPojos);*/

                selectPersonZDialog.dismiss();
                break;
            case 9:
                participantsZPojos = AppJsonUtil.getArrayList(result,"hostList",ParticipantsZPojo.class);
                participantsZAdapter.setDatas(participantsZPojos);

                participantsCPojos = AppJsonUtil.getArrayList(result,"otherList",ParticipantsCPojo.class);
                participantsCAdapter.setDatas(participantsCPojos);
                participantsZDialog.dismiss();
                break;
            case 10:
                participantsZPojos = AppJsonUtil.getArrayList(result,"hostList",ParticipantsZPojo.class);
                participantsZAdapter.setDatas(participantsZPojos);

                participantsCPojos = AppJsonUtil.getArrayList(result,"otherList",ParticipantsCPojo.class);
                participantsCAdapter.setDatas(participantsCPojos);
                participantsCDialog.dismiss();
                break;
            case 11:
                showToast("保存参会人员成功");
                PreservationEvent pe = new PreservationEvent();
                pe.setCode("保存");
                EventBus.getDefault().post(pe);
                SponsorEvent se = new SponsorEvent();
                se.setCode("参会人员-保存");
                EventBus.getDefault().post(se);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(SponsorClearEvent sponsorClearEvent){
        if(sponsorClearEvent.getClear().equals("clear")){
            participantsCAdapter.removeAll();
            participantsCPojos.clear();
            mapC.clear();
            participantsZAdapter.removeAll();
            participantsZPojos.clear();
            mapZ.clear();
            pager = 1;
            participantsSPojos.clear();
            selectChoicePersonCPojos.clear();
            selectChoicePersonZPojos.clear();
            selectChoicePersonCYPojos.clear();
            selectChoicePersonZYPojos.clear();
            stringCList.clear();
            stringZList.clear();
            kv.encode("meetingId",00);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        kv.encode("meetingId",00);
    }
}
