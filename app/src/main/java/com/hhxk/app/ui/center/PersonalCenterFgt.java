package com.hhxk.app.ui.center;

import android.view.View;
import android.widget.TextView;

import com.em.baseframe.util.AppManger;
import com.em.baseframe.util.RetrofitUtils;
import com.hhxk.app.R;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.view.UpDatePersonDialog;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  个人中心fragemnt
 * @date   2019/02/18
 * @author enmaoFu
 */
public class PersonalCenterFgt extends BaseLazyFgt {

    @BindView(R.id.name)
    TextView nameText;
    @BindView(R.id.title)
    TextView titleText;
    @BindView(R.id.department)
    TextView departmentText;
    @BindView(R.id.jurisdiction)
    TextView jurisdictionText;

    private UpDatePersonDialog upDatePersonDialogName;
    private UpDatePersonDialog upDatePersonDialogTitle;
    private UpDatePersonDialog upDatePersonDialogDepartment;
    private UpDatePersonDialog upDatePersonDialogPwd;

    private MMKV kv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();

        nameText.setText(kv.decodeString("user_name"));
        titleText.setText(kv.decodeString("department_position"));
        departmentText.setText(kv.decodeString("department_name"));
        jurisdictionText.setText("系统权限：" + kv.decodeString("role_name"));
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
    }

    @Override
    protected void requestData() {

    }

    @OnClick({R.id.up_pwd})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.up_pwd:
                upDatePersonDialogPwd = new UpDatePersonDialog(getActivity());
                upDatePersonDialogPwd.show();
                upDatePersonDialogPwd.setDialogAnimation();
                upDatePersonDialogPwd.setTitle("修改密码");
                upDatePersonDialogPwd.setHint("请输入密码");
                upDatePersonDialogPwd.setPwd();
                upDatePersonDialogPwd.setBtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getInput = upDatePersonDialogPwd.getInput();
                        if(getInput.length() == 0){
                            showErrorToast("请输入密码");
                        }else{
                            showLoadingDialog(null);
                            doHttp(RetrofitUtils.createApi(Http.class).changePass(getInput,kv.decodeString("user_id")),1);
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
                upDatePersonDialogPwd.dismiss();
                showToast("修改密码成功,请重新登录");
                //退出登录
                setHasAnimiation(false);
                kv.encode("isLogin", false);
                //姓名
                kv.encode("user_name", "");
                //职务
                kv.encode("department_position","");
                //部门
                kv.encode("department_name", "");
                //权限名
                kv.encode("role_name", "");
                //用户ID
                kv.encode("user_id", "");
                //权限ID
                kv.encode("role_id", "");
                //用户账号
                kv.encode("user_account", "");
                //发起会议id标识
                kv.encode("meetingId",00);
                AppManger.getInstance().killAllActivity();
                System.exit(0);
                break;
        }
    }
}
