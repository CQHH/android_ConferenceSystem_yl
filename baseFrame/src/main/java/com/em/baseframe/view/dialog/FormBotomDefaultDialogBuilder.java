package com.em.baseframe.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.em.baseframe.R;

public class FormBotomDefaultDialogBuilder extends Dialog implements DialogInterface,
        View.OnClickListener {

    private Context tmpContext;
    /**
     * dialog布局
     */
    private View mDialogView;

    /**
     * 第一个按钮
     */
    private Button mButton_first;
    /**
     * 最后按钮
     */
    private Button mButton_last;
    /**
     * 取消按钮
     */
    private Button mButton_cancel;




    public FormBotomDefaultDialogBuilder(Context context) {

        this(context, R.style.dialog_untran);

    }

    public FormBotomDefaultDialogBuilder(Context context, int theme) {
        super(context, R.style.dialog_untran);
        init(context);
        this.tmpContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        this.onWindowAttributesChanged(wl);

    }

    private void init(Context context) {

        mDialogView = View.inflate(context, R.layout.form_bottom_choose_dialog,
                null);

        mButton_first = (Button) mDialogView.findViewById(R.id.btn_first);
        mButton_last = (Button) mDialogView.findViewById(R.id.btn_last);
        mButton_cancel = (Button) mDialogView.findViewById(R.id.btn_cancle);

        mButton_first.setOnClickListener(this);
        mButton_last.setOnClickListener(this);
        mButton_cancel.setOnClickListener(this);
        setContentView(mDialogView);

    }

    private DialogBtnCallBack click_01;
    private DialogBtnCallBack click_02;
    private DialogBtnCallBack click_03;

    /**
     * 设置取消的点击事件
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBCancelBtnClick(DialogBtnCallBack click) {
        if (click != null) {
            this.click_03 = click;
        }
        return this;
    }

    /**
     * 设置第一个按钮的点击事件
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBFirstBtnClick(DialogBtnCallBack click) {
        if (click != null) {
            this.click_01 = click;
        }
        return this;
    }

    /**
     * 设置最后一个按钮的点击事件
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBLastBtnClick(DialogBtnCallBack click) {
        if (click != null) {
            this.click_02 = click;
        }
        return this;
    }

    /**
     * 设置所有button的字体颜色
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setAllBtnTextColor(int color) {
        mButton_first.setTextColor(color);
        mButton_last.setTextColor(color);
        mButton_cancel.setTextColor(color);

        return this;
    }
    /**
     * 设置第一个button的字体颜色
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBFirstBtnTextColor(int color) {
        mButton_first.setTextColor(color);
        return this;
    }
    /**
     * 设置最后button的字体颜色
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBLastBtnTextColor(int color) {
        mButton_last.setTextColor(color);
        return this;
    }
    /**
     * 设置取消button的字体颜色
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBCancelBtnTextColor(int color) {
        mButton_cancel.setTextColor(color);
        return this;
    }

    /**
     * 设置第一个button的字体
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBFirstBtnText(String s) {
        mButton_first.setText(s);

        return this;
    }

    /**
     * 设置最后一个button的字体
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBLastBtnText(String s) {
        mButton_last.setText(s);

        return this;
    }

    /**
     * 设置取消button的字体
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBCancelBtnText(String s) {
        mButton_cancel.setText(s);

        return this;
    }

    /**
     * 设置取消按钮的点击事件
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBCancelClick(DialogBtnCallBack click) {
        if (click != null) {
            this.click_03 = click;

        }
        return this;
    }
    /**
     * 设置第一个按钮的是否可点击
     *
     * @param
     * @return
     */
    public FormBotomDefaultDialogBuilder setFBFirstBtnEnable(boolean isEnable) {
        mButton_first.setEnabled(isEnable);
        return this;
    }

    /**
     * 判断是否显示view
     */
    private void toggleView(View view, Object obj) {
        if (obj == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {

        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    public interface DialogBtnCallBack {
        void dialogBtnOnClick();
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_first == v.getId()) {
            if (click_01 != null) {
                click_01.dialogBtnOnClick();
            }

        } else if (R.id.btn_last == v.getId()) {
            if (click_02 != null) {
                click_02.dialogBtnOnClick();
            }
        } else if (R.id.btn_cancle == v.getId()) {
            if (click_03 != null) {
                click_03.dialogBtnOnClick();
            }
        }

        dismiss();

    }

}