package com.em.baseframe.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.em.baseframe.R;
import com.em.baseframe.http.HttpCallBack;
import com.em.baseframe.util.AppUtils;
import com.em.baseframe.util.NetWorkUtils;
import com.em.baseframe.util.ToastUtil;
import com.em.baseframe.util.ViewStateManger;
import com.em.baseframe.view.statusbar.StatusBarUtil;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @title  基础的Fragment,单独放在Activity中使用的
 * @date   2017/06/17
 * @author enmaoFu
 */
public abstract class BaseFragment extends Fragment implements HttpCallBack{

    /**
     * 页面视图管理状态
     * 例如:网络对话框,无网络页面等
     */
    private ViewStateManger mStateManger;

    /**
     * 是否初始化请求网络数据
     * 默认为false
     */
    protected boolean isInitRequestData = false;

    /**
     * 直接的跳转默认为有动画
     */
    private boolean hasAnimiation = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fgtView = inflater.inflate(R.layout.frame_base_layout, container, false);
        FrameLayout content = (FrameLayout) fgtView.findViewById(R.id.content);

        View contentView = inflater.inflate(getLayoutId(), null, false);
        if (contentView.getParent() != content) {
            content.addView(contentView);
        }

        mStateManger = new ViewStateManger(getActivity(), content, contentView, R.layout.frame_error_layout, R.layout.frame_loading_content_dialog, new ViewStateManger.BtnRefreshClickListener() {
            @Override
            public void onRefreshClick() {
                //判断网络是否可用
                if (NetWorkUtils.isNetworkConnected(getActivity())) {
                    requestData();
                } else {
                    showErrorToast("网络连接错误");
                }
            }
        });

        ButterKnife.bind(this, fgtView);

        // 设置是否是初始化网络操作
        isInitRequestData = setIsInitRequestData();

        initStatusBar();

        initData();

        initRequsetMethod();

        return fgtView;
    }

    //======================================初始化

    /**
     * 布局文件ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 设置是否是初始化网络
     *
     * @return
     */
    protected abstract boolean setIsInitRequestData();

    /**
     * 请求网络初始化数据
     */
    protected abstract void requestData();

    /**
     * 初始化请求网络
     */
    protected void initRequsetMethod() {
        if (isInitRequestData) {
            //判断网络是否可用
            if (NetWorkUtils.isNetworkConnected(getActivity())) {
                requestData();
            } else {
                showNetWorkErrorPage();
            }
        }
    }

    /**
     * 设置沉浸式状态栏
     * 如果需要改变，则在继承的终类重写该方法或对应的方法
     */
    protected void initStatusBar() {
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.colorPrimary),40);
    }

    /**
     * view的点击事件
     */
    public abstract void btnClick(View view);

    //======================================进度条

    /**
     * 显示网络错误界面
     */
    protected void showNetWorkErrorPage() {
        mStateManger.showNetWorkErrorPage();
    }

    /**
     * 隐藏网络错误界面
     */
    protected void hideNetWorkErrorPage() {
        mStateManger.hideNetWorkErrorPage();
    }

    /**
     * 显示提示信息
     *
     * @param message
     */
    protected void showToast(String message) {
        ToastUtil.showSuccessToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 显示提示信息
     *
     * @param message
     */
    protected void showErrorToast(String message) {
        ToastUtil.showErrorToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 显示进度对话条
     */
    protected void showLoadingDialog(String message) {
        mStateManger.showLoadingDialog(message);
    }

    /**
     * 设置全屏遮盖的进度条
     *
     * @param spacingTop    距离顶部的距离单位dp
     * @param spacingBottom 距离底部的距离单位dp
     */
    protected void setLoadingContentSpace(int spacingTop, int spacingBottom) {
        mStateManger.setLoadingContentSpace(spacingTop, spacingBottom);
    }

    /**
     * 显示全屏遮盖的进度条(toolbar可以显示出来默认50dp)
     */
    protected void showLoadingContentDialog() {
        mStateManger.showLoadingContentDialog();
    }

    /**
     * 隐藏进度条
     */
    protected void dismissLoadingContentDialog() {
        mStateManger.dismissLoadingContentDialog();
    }

    /**
     * 隐藏进度条
     */
    protected void dismissLoadingDialog() {
        mStateManger.dismissLoadingDialog();
    }

    //======================================启动Activity

    /**
     * 获取跳转是否有动画
     * @return
     */
    public boolean isHasAnimiation() {
        return hasAnimiation;
    }

    /**
     * 设置跳转是否有动画
     * @param hasAnimiation
     */
    public void setHasAnimiation(boolean hasAnimiation) {
        this.hasAnimiation = hasAnimiation;
    }

    /**
     * 启动一个Activity
     *
     * @param className 将要启动的Activity的类名
     * @param options   传到将要启动Activity的Bundle，不传时为null
     */
    public float startActivity(Class<?> className, Bundle options) {
        if (AppUtils.isFastDoubleClick()) {
            return 0;
        }
        Intent intent = new Intent(getActivity(), className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivity(intent);
        if (hasAnimiation) {
            getActivity().overridePendingTransition(R.anim.slide_right_in,
                    R.anim.slide_left_out);

        }
        return 0;
    }

    /**
     * @param className
     * @param options
     * @param flag      是否防止两次进入
     */
    public void startActivity(Class<?> className, Bundle options, boolean flag) {
        if (flag && AppUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivity(intent);
        if (hasAnimiation) {
            getActivity().overridePendingTransition(R.anim.slide_right_in,
                    R.anim.slide_left_out);

        }
    }

    /**
     * 启动一个有会返回值的Activity
     *
     * @param className   将要启动的Activity的类名
     * @param options     传到将要启动Activity的Bundle，不传时为null
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> className, Bundle options,
                                       int requestCode) {
        if (AppUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, requestCode);
        if (hasAnimiation) {
            getActivity().overridePendingTransition(R.anim.slide_right_in,
                    R.anim.slide_left_out);

        }
    }

    /**
     *
     * @param className
     * @param options
     * @param requestCode
     * @param flag      是否防止两次进入
     */
    public void startActivityForResult(Class<?> className, Bundle options,
                                       int requestCode, boolean flag) {
        if (flag && AppUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, requestCode);
        if (hasAnimiation) {
            getActivity().overridePendingTransition(R.anim.slide_right_in,
                    R.anim.slide_left_out);

        }
    }

    //======================================网络请求

    /**
     * 网络请求成功
     * @param result
     * @param call
     * @param response
     * @param what
     */
    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        isInitRequestData = false;
        dismissLoadingDialog();
        dismissLoadingContentDialog();
    }

    /**
     * 网络请求失败
     * @param result
     * @param call
     * @param response
     * @param what
     */
    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        isInitRequestData = false;
        dismissLoadingDialog();
        dismissLoadingContentDialog();
    }

    /**
     * 网络请求错误
     * @param call
     * @param t
     * @param what
     */
    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        if (getActivity().isFinishing()) {
            return;
        }
        showErrorToast("网络连接错误");
        dismissLoadingDialog();
        dismissLoadingContentDialog();
        if (isInitRequestData) {
            showNetWorkErrorPage();
        } else {
            hideNetWorkErrorPage();
        }
    }

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
                            BaseFragment.this.onSuccess(result, call, response, what);
                        } else {
                            BaseFragment.this.onFailure(result, call, response, what);
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
                BaseFragment.this.onError(call, t, what);

            }
        });

    }

    /**
     * 销毁Activity的时候的操作
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
    }

}
