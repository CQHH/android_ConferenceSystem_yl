package com.hhxk.app.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hhxk.app.R;
import com.hhxk.app.adapter.ManagerDepartmentListAdapter;
import com.hhxk.app.pojo.ManagerDepartmentListPojo;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * @title  根据部门切换view
 * @date   2019/02/27
 * @author enmaoFu
 */
public class ManagerDepartmentDialog extends Dialog {

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private ManagerDepartmentListAdapter managerDepartmentListAdapter;

    /**
     * 数据源
     */
    private List<ManagerDepartmentListPojo> managerDepartmentListPojos;

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

    private TextView btn;

    private TextView title;

    private RecyclerView mRecyclerView;

    public ManagerDepartmentDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        setContentView(R.layout.dialog_manager_department_list);

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
        btn = (TextView)findViewById(R.id.btn);
        title = (TextView)findViewById(R.id.title);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyvlerview);
    }

    public void setRv(Context mContext,List<ManagerDepartmentListPojo> managerDepartmentListPojosNew){
        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(mContext);
        //实例化适配器
        managerDepartmentListAdapter = new ManagerDepartmentListAdapter(R.layout.item_manager_department_list, managerDepartmentListPojosNew);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置间隔样式
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(mContext)
                        .color(Color.parseColor("#E8E8E8"))
                        .sizeResId(R.dimen.recyclerview_item_hr)
                        .build());
        //大小不受适配器影响
        mRecyclerView.setHasFixedSize(true);
        //设置adapter
        mRecyclerView.setAdapter(managerDepartmentListAdapter);
    }

    public ManagerDepartmentListAdapter getAdapter(){
        return managerDepartmentListAdapter;
    }

   /* public void setBtnOnClick(View.OnClickListener clickListener){
        dialogTokenDetailsText.setOnClickListener(clickListener);
    }*/

    public void setTitle(String str){
        title.setText(str);
    }


    /**
     * 给对话框设置动画
     */
    public void setDialogAnimation() {
        this.getWindow().setWindowAnimations(DIALOG_ANIM_SLID_BOTTOM);
    }

}
