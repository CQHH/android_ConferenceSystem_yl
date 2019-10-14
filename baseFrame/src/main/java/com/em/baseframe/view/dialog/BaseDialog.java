package com.em.baseframe.view.dialog;

import android.app.Dialog;
import android.content.Context;

import com.em.baseframe.R;

/**
 * @title  基础的弹框类
 * @date   2017/06/17
 * @author enmaoFu
 */
public  class BaseDialog extends Dialog {


    public BaseDialog(Context context) {
        this(context, R.style.dialog_untran);
        setCanceledOnTouchOutside(false);

    }


    public BaseDialog(Context context, int theme) {
        super(context, R.style.dialog_untran);
        setCanceledOnTouchOutside(false);
    }

}
