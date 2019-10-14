package com.hhxk.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hhxk.app.R;

/**
 * @title  添加会议议题view
 * @date   2019/02/25
 * @author enmaoFu
 */
public class AddIssueDialog extends Dialog {

    private TextView dialogTitle;
    private EditText edTitle;
    private EditText edName;
    private EditText edPo;
    private TextView btnAdd;

    /**
     * 从下往上滑动动画
     */
    public static final int DIALOG_ANIM_SLID_BOTTOM = com.em.baseframe.R.style.DialogAnimationSlidBottom;
    /**
     * 对话框宽度所占屏幕宽度的比例
     */
    public static final float WIDTHFACTOR = 0.6f;

    /**
     * 对话框
     */
    private Window window;

    public AddIssueDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        setContentView(R.layout.dialog_add_issue);

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
        dialogTitle = (TextView)findViewById(R.id.dialog_title) ;
        edTitle = (EditText)findViewById(R.id.ed_title);
        edName = (EditText)findViewById(R.id.ed_name);
        edPo = (EditText)findViewById(R.id.ed_po);
        btnAdd = (TextView)findViewById(R.id.btn_add);
    }

    public void setDialogTitle(String str){
        dialogTitle.setText(str);
    }

    public String getTitlle(){
        return edTitle.getText().toString().trim();
    }

    public String getName(){
        return edName.getText().toString().trim();
    }

    public String getPo(){
        return edPo.getText().toString().trim();
    }

    public void setTitle(String title){
        edTitle.setText(title);
        edTitle.setSelection(title.length());
    }

    public void setName(String name){
        edName.setText(name);
        edName.setSelection(name.length());
    }

    public void setPo(String po){
        edPo.setText(po);
        edPo.setSelection(po.length());
    }

    public void setOnclick(View.OnClickListener clickListener){
        btnAdd.setOnClickListener(clickListener);
    }

   /* public void setBtnOnClick(View.OnClickListener clickListener){
        dialogTokenDetailsText.setOnClickListener(clickListener);
    }*/

    /**
     * 给对话框设置动画
     */
    public void setDialogAnimation() {
        this.getWindow().setWindowAnimations(DIALOG_ANIM_SLID_BOTTOM);
    }

}
