package com.hhxk.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hhxk.app.R;

/**
 * @title  修改个人信息view
 * @date   2019/02/25
 * @author enmaoFu
 */
public class UpDatePersonDialog extends Dialog {

    /**
     * 从下往上滑动动画
     */
    public static final int DIALOG_ANIM_SLID_BOTTOM = com.em.baseframe.R.style.DialogAnimationSlidBottom;
    /**
     * 对话框宽度所占屏幕宽度的比例
     */
    public static final float WIDTHFACTOR = 0.3f;

    /**
     * 对话框
     */
    private Window window;

    private EditText input;

    private TextView btn;

    private TextView title;

    public UpDatePersonDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        setContentView(R.layout.dialog_update);

        window = this.getWindow();
        //是否系统级弹框
        if (false) {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        // 获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenwidth = metrics.widthPixels;
        int width = 0;
        if (WIDTHFACTOR > 0) {
            width = (int) (screenwidth * WIDTHFACTOR);
        } else {
            width = (int) (screenwidth * WIDTHFACTOR);
        }
        // 设置对话框宽度
        window.getAttributes().width = width;
        // 设置对话框位置
        window.setGravity(Gravity.CENTER);

        init();
    }

    private void init(){
        input = (EditText)findViewById(R.id.input);
        btn = (TextView)findViewById(R.id.btn);
        title = (TextView)findViewById(R.id.title);
    }

   /* public void setBtnOnClick(View.OnClickListener clickListener){
        dialogTokenDetailsText.setOnClickListener(clickListener);
    }*/

   public void setPwd(){
       //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
       input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
   }

   public void setTitle(String str){
       title.setText(str);
   }

   public String getInput(){
       return input.getText().toString().trim();
   }

   public void setHint(String str){
       input.setHint(str);
   }

   public void setBtnOnClick(View.OnClickListener clickListener){
       btn.setOnClickListener(clickListener);
   }

    /**
     * 给对话框设置动画
     */
    public void setDialogAnimation() {
        this.getWindow().setWindowAnimations(DIALOG_ANIM_SLID_BOTTOM);
    }

}
