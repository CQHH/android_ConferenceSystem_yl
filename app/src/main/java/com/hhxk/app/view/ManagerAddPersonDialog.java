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
 * @title  添加成员view
 * @date   2019/02/25
 * @author enmaoFu
 */
public class ManagerAddPersonDialog extends Dialog {

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

    private EditText inputCode;

    private Spinner selectDep;

    private Spinner selectPost;

    private TextView btn;

    private Map<String,String> depMap;

    private List<String> departments;

    private Map<String,String> postMap;

    private List<String> posts;

    public ManagerAddPersonDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        setContentView(R.layout.dialog_manager_add_person);

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
        inputCode = (EditText)findViewById(R.id.input_code);
        btn = (TextView)findViewById(R.id.btn);
        selectDep = (Spinner)findViewById(R.id.select_dep);
        selectPost = (Spinner)findViewById(R.id.select_post);
    }

    public String getName(){
        return inpName.getText().toString().trim();
    }

    public void setDepMap(Map<String,String> map){
        depMap = map;
    }

    public String getCode(){
        return inputCode.getText().toString().trim();
    }

    public void setPostMap(Map<String,String> map){
        postMap = map;
    }

    public void setSpinnerDep(Context mContext){
        departments = new ArrayList<String>();
        for (Map.Entry<String,String> entry : depMap.entrySet()) {
            departments.add(entry.getValue());
        }

        //第二个参数是显示的布局,第三个参数是在布局显示的位置id,第四个参数是将要显示的数据
        ArrayAdapter adapterD = new ArrayAdapter(mContext, R.layout.item_manager_up_add_person, R.id.text,departments);
        selectDep.setAdapter(adapterD);
    }

    public void setSpinnerPost(Context mContext){
        posts = new ArrayList<String>();
        for (Map.Entry<String,String> entry : postMap.entrySet()) {
            posts.add(entry.getValue());
        }

        //第二个参数是显示的布局,第三个参数是在布局显示的位置id,第四个参数是将要显示的数据
        ArrayAdapter adapterD = new ArrayAdapter(mContext, R.layout.item_manager_up_add_person, R.id.text,posts);
        selectPost.setAdapter(adapterD);
    }

    public void setSpinnerDepOnItemClick(AdapterView.OnItemSelectedListener onItemSelectedListener){
        selectDep.setOnItemSelectedListener(onItemSelectedListener);
    }

    public void setSpinnerPostOnItemClick(AdapterView.OnItemSelectedListener onItemSelectedListener){
        selectPost.setOnItemSelectedListener(onItemSelectedListener);
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
