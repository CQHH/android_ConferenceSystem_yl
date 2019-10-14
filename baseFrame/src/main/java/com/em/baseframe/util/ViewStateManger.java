package com.em.baseframe.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.em.baseframe.R;
import com.em.baseframe.view.dialog.LoadingDialog;


/**
 * @title  进入页面视图状态管理类
 * @date   2017/06/17
 * @author enmaoFu
 */
public class ViewStateManger {

    /**
     * 请求网络对话弹窗
     */
    private LoadingDialog mLoadingDialog;
    /**
     * 请求网络全屏对话弹窗
     */
    private LinearLayout mLoadingContent;

    /**
     * 无网络全屏对话弹窗
     */
    private View errorView;
    /**
     * 内容界面
     */
    private View contentView;


    /**
     * 系统容器布局
     */
    private FrameLayout content;

    private Context mContext;

    private LayoutInflater mInflater;


    private int errorViewID;
    private int loadingContentID;


    private BtnRefreshClickListener mRefreshClickListener;

    /**
     *
     * @param context
     * @param content 系统容器布局
     * @param contentView 内容界面
     * @param errorViewID 无网络全屏对话弹窗Id
     * @param loadingContentID 请求网络全屏对话弹窗
     * @param clickListener 无网络刷新按钮
     */
    public ViewStateManger(Context context, View content, View contentView, int errorViewID, int loadingContentID, BtnRefreshClickListener clickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.content = (FrameLayout) content;
        this.contentView = contentView;
        this.errorViewID = errorViewID;
        this.loadingContentID = loadingContentID;
        this.mRefreshClickListener = clickListener;
    }

    /**
     * 显示进度对话条
     */
    public void showLoadingDialog(String message) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
            mLoadingDialog.setContentView(R.layout.frame_loading_dialog);
        }
        mLoadingDialog.showLoadingDialog(message);
    }

    /**
     * 隐藏进度条
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            //结束掉网络请求
            mLoadingDialog.dismiss();
        }
    }


    /**
     * 显示网络错误界面
     */
    public void showNetWorkErrorPage() {

        if (errorView == null) {
            errorView = mInflater.inflate(errorViewID, null, false);
            errorView.setClickable(true);
            content.addView(errorView);
            TextView btn_error = (TextView) errorView.findViewById(R.id.btn_resh);
            btn_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRefreshClickListener.onRefreshClick();
                }
            });
        }

        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        if (mLoadingContent != null) {
            mLoadingContent.setVisibility(View.GONE);
        }
    }


    /**
     * 隐藏网络错误界面
     */
    public void hideNetWorkErrorPage() {
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        contentView.setVisibility(View.VISIBLE);
        if (mLoadingContent != null) {
            mLoadingContent.setVisibility(View.GONE);
        }

    }


    /**
     * 设置全屏遮盖的进度条
     *
     * @param spacingTop    距离顶部的距离单位dp
     * @param spacingBottom 距离底部的距离单位dp
     */
    public void setLoadingContentSpace(int spacingTop, int spacingBottom) {


        if (mLoadingContent == null) {
            mLoadingContent = (LinearLayout) mInflater.inflate(loadingContentID, null, false);
            content.addView(mLoadingContent);
        }


        if (spacingTop >= 0 && spacingBottom >= 0) {
            FrameLayout spacingView = (FrameLayout) mLoadingContent.findViewById(R.id.frame_spacing);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.topMargin = DensityUtils.dp2px(mContext, spacingTop);
            params.bottomMargin = DensityUtils.dp2px(mContext, spacingBottom);
            spacingView.setLayoutParams(params);

        } else if (spacingTop >= 0 && spacingBottom < 0) {
            FrameLayout spacingView = (FrameLayout) mLoadingContent.findViewById(R.id.frame_spacing);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.topMargin = DensityUtils.dp2px(mContext, spacingTop);
            spacingView.setLayoutParams(params);

        } else if (spacingTop < 0 && spacingBottom >= 0) {
            FrameLayout spacingView = (FrameLayout) mLoadingContent.findViewById(R.id.frame_spacing);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.bottomMargin = DensityUtils.dp2px(mContext, spacingBottom);
            spacingView.setLayoutParams(params);
        }


    }

    /**
     * 显示全屏遮盖的进度条
     */
    public void showLoadingContentDialog() {

        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        contentView.setVisibility(View.VISIBLE);
        if (mLoadingContent == null) {
            mLoadingContent = (LinearLayout) mInflater.inflate(loadingContentID, null, false);
            content.addView(mLoadingContent);
        }
        mLoadingContent.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     */
    public void dismissLoadingContentDialog() {
        if (mLoadingContent != null) {
            mLoadingContent.setVisibility(View.GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        contentView.setVisibility(View.VISIBLE);
    }


    public interface BtnRefreshClickListener {

        void onRefreshClick();
    }
}
