package com.hhxk.app.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.em.baseframe.util.ToastUtil;

import butterknife.ButterKnife;


/**
 * File: com.done.coloreggapplication.fragment.base.BaseFragment.java
 * Description: 简单的懒加载方式
 *
 * @author Done
 * @date 2018/8/23
 */

public abstract class BaseLazyFragment extends Fragment implements View.OnClickListener {

    private View viewHolder;

    protected Activity mGroupActivity;

    /**
     * 是否可见
     */
    protected boolean isVisible = false;

    /**
     * 是否已经实例化过此fragment
     */
    protected boolean isViewCreated = false;

    /**
     * 是否加载过数据
     */
    private boolean isLoadedData = false;

    private static final String DATA_FROM_ACTIVITY = "DATA_FROM_ACTIVITY";

    private SparseArray<View> mViews = new SparseArray<>();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser && isViewCreated && !isLoadedData) {
            initData();
            isLoadedData = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGroupActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewHolder = inflater.inflate(getLayoutId(), container, false);
        initView();
        isViewCreated = true;
        if (getUserVisibleHint() && !isLoadedData) {
            initData();
            isLoadedData = true;
        }
        ButterKnife.bind(this, viewHolder);
        return viewHolder;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            initBundle(arguments);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {

    }

    public abstract void initBundle(@NonNull Bundle bundle);

    protected <E extends View> E findView(int viewId, boolean bindClick) {
        if (viewHolder != null) {
            E view = (E) mViews.get(viewId);
            if (view == null) {
                view = viewHolder.findViewById(viewId);
                mViews.put(viewId, view);
            }
            if (bindClick) {
                view.setOnClickListener(this);
            }
            return view;
        }
        return null;
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();

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

}
