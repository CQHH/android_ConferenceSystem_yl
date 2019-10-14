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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hhxk.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @title  修改成员view
 * @date   2019/02/28
 * @author enmaoFu
 */
public class ManagerUpPersonDialog extends Dialog {

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

    private EditText inpName;

    private EditText inputPost;

    private EditText inputDepartment;

    private TextView btn;

    private Spinner spinnerD;

    private Spinner spinnerB;

    private Map<String,String> depMap;

    private List<String> departments;

    private Map<String,String> postMap;

    private List<String> posts;

    public ManagerUpPersonDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        setContentView(R.layout.dialog_manager_up_person);

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
        inpName = (EditText)findViewById(R.id.input_name);
        btn = (TextView)findViewById(R.id.btn);
        spinnerD = (Spinner)findViewById(R.id.spinner_d);
        spinnerB = (Spinner)findViewById(R.id.spinner_b);
    }

    public void setSelection(int len){
        inpName.setSelection(len);
    }

    public void setName(String str){
        inpName.setText(str);
    }

    public String getName(){
        return inpName.getText().toString().trim();
    }

    public void setBtnOnClick(View.OnClickListener clickListener){
        btn.setOnClickListener(clickListener);
    }

    public void setDepMap(Map<String,String> map){
        depMap = map;
    }

    public void setPostMap(Map<String,String> map){
        postMap = map;
    }

    public void setSpinnerD(Context mContext){
        departments = new ArrayList<String>();
        for (Map.Entry<String,String> entry : depMap.entrySet()) {
            departments.add(entry.getValue());
        }

        //第二个参数是显示的布局,第三个参数是在布局显示的位置id,第四个参数是将要显示的数据
        ArrayAdapter adapterD = new ArrayAdapter(mContext, R.layout.item_manager_up_add_person, R.id.text,departments);
        spinnerD.setAdapter(adapterD);
    }

    public void setSpinnerB(Context mContext){
        posts = new ArrayList<String>();
        for (Map.Entry<String,String> entry : postMap.entrySet()) {
            posts.add(entry.getValue());
        }

        //第二个参数是显示的布局,第三个参数是在布局显示的位置id,第四个参数是将要显示的数据
        ArrayAdapter adapterD = new ArrayAdapter(mContext, R.layout.item_manager_up_add_person, R.id.text,posts);
        spinnerB.setAdapter(adapterD);
    }

    public void setSpinnerDepOnItemClick(AdapterView.OnItemSelectedListener onItemSelectedListener){
        spinnerD.setOnItemSelectedListener(onItemSelectedListener);
    }

    public void setSpinnerPostOnItemClick(AdapterView.OnItemSelectedListener onItemSelectedListener){
        spinnerB.setOnItemSelectedListener(onItemSelectedListener);
    }

    /**
     * 给对话框设置动画
     */
    public void setDialogAnimation() {
        this.getWindow().setWindowAnimations(DIALOG_ANIM_SLID_BOTTOM);
    }

}
