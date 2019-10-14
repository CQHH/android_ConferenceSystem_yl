package com.hhxk.app.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.em.baseframe.config.UserInfoManger;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hhxk.app.R;
import com.hhxk.app.base.BaseAty;
import com.hhxk.app.interfaces.Http;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class LoginActivity extends BaseAty {

    @BindView(R.id.img)
    SimpleDraweeView img;
    @BindView(R.id.user_input)
    EditText userInput;
    @BindView(R.id.pwd_input)
    EditText pwdInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        img.setImageURI((new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.loginbg)).build());
    }

    @Override
    protected void requestData() {

    }

    @OnClick({R.id.btn,R.id.w_pwd})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                String getUser = userInput.getText().toString().trim();
                String getPwd = pwdInput.getText().toString().trim();
                if(getUser.length() == 0){
                    showErrorToast("请输入用户名");
                }else if(getPwd.length() == 0){
                    showErrorToast("请输入密码");
                }else{
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Http.class).Login(getUser,getPwd),1);
                }
                //startActivity(MainActivity.class,null);
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                showToast("登录成功");
                //设置为已登录状态
                //UserInfoManger.setIsLogin(true);
                MMKV kv = MMKV.defaultMMKV();
                kv.encode("isLogin", true);
                //姓名
                kv.encode("user_name", AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"user_name"));
                //职务
                kv.encode("department_position", AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"department_position"));
                //部门
                kv.encode("department_name", AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"department_name"));
                //权限名
                kv.encode("role_name", AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"role_name"));
                //用户ID
                kv.encode("user_id", AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"user_id"));
                //权限ID
                kv.encode("role_id", AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"role_id"));
                //用户账号
                kv.encode("user_account", AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"user_account"));
                startActivity(MainActivity.class,null);
                break;
        }
    }
}
