package com.hhxk.app.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.WindowManager;

import com.em.baseframe.config.UserInfoManger;
import com.hhxk.app.R;
import com.hhxk.app.base.BaseAty;
import com.tencent.mmkv.MMKV;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SplashActivity extends BaseAty {

    /**
     * 用来判断申请权限被拒绝并且不再提醒,提示用户去设置界面重新打开权限后，返回来继续进入app
     */
    private int isInto = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isInto == 1){
            start();
        }else{
            isInto = 0;
            return;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        //当android系统小于6.0的时候直接使用所有权限，不用动态申请权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            start();
        } else {
            SplashActivityPermissionsDispatcher.ApplySuccessWithCheck(this);
        }
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    protected void requestData() {

    }

    public void start(){
        Handler mHandle = new Handler();
        mHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                goActivity();
            }
        }, 1000);
    }

    private void goActivity() {
        setHasAnimiation(false);
        MMKV kv = MMKV.defaultMMKV();
        if (kv.decodeBool("isLogin") == true) {
            startActivity(MainActivity.class, null);
            finish();
        } else {
            startActivity(LoginActivity.class, null);
            finish();
        }
        /*if (UserInfoManger.isLogin()) {
            startActivity(MainActivity.class, null);
            finish();
        } else {
            startActivity(LoginActivity.class, null);
            finish();
        }*/
        overridePendingTransition(R.anim.aty_in, R.anim.activity_alpha_out);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    //----------------------------------------------------------- 权限的申请 -----------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 申请权限成功时
     */
    @NeedsPermission({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    })
    void ApplySuccess() {
        start();
    }

    /**
     * 申请权限告诉用户原因时
     * @param request
     */
    @OnShowRationale({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    })
    void showRationaleForMap(PermissionRequest request) {
        showRationaleDialog("您没有授予足够的权限，请去设置", request);
    }

    /**
     * 申请权限被拒绝时
     *
     */
    @OnPermissionDenied({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    })
    void onMapDenied() {
        showToast("您没有授予足够的权限，App可能无法正常使用");
    }

    /**
     * 申请权限被拒绝并勾选不再提醒时
     */
    @OnNeverAskAgain({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    })
    void onMapNeverAskAgain() {
        AskForPermission();
    }

    /**
     * 告知用户具体需要权限的原因
     * @param messageResId
     * @param request
     */
    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("请求权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();//请求权限
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                        start();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 被拒绝并且不再提醒,提示用户去设置界面重新打开权限
     */
    private void AskForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("当前应用缺少所需要的权限,请去设置界面打开\n打开之后按两次返回键可回到该应用哦");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                start();
            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + SplashActivity.this.getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
                isInto = 1;
            }
        });
        builder.create().show();
    }

}
