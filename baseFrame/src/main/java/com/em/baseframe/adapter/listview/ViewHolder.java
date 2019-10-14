package com.em.baseframe.adapter.listview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * @title  listview的适配器工具 通用的viewHodler支持多个item
 * @date   2017/06/17
 * @author enmaoFu
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;
    private int mLayoutId;
    private int mPosition;

    public ViewHolder(Context context, View itemView,int mPosition) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        this.mPosition=mPosition;
        mViews = new SparseArray<>();
        mConvertView.setTag(this);

    }
    public static ViewHolder createViewHolder(Context context, View itemView)
    {
        ViewHolder holder = new ViewHolder(context, itemView,-1);
        return holder;
    }

    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false);
            ViewHolder holder = new ViewHolder(context, itemView,position);
            holder.mLayoutId = layoutId;
            return holder;
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setTextViewText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
    /**
     * 为TextView设置字体颜色
     *
     * @param viewId
     * @param
     * @return
     */
    public ViewHolder setTextViewTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    /**
     * 为EditText设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setEditText(int viewId, String text) {
        EditText view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为Button设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setButtonText(int viewId, String text) {
        Button view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为RadioButton设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setRadioBtnText(int viewId, String text) {
        RadioButton view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 设置view的点击事件
     *
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnClick(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 为ImageView设置图片,通过资源文件
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageByResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为view设置背景
     *
     * @param viewId
     * @param
     * @param
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {

        getView(viewId).setBackground(drawable);

        return this;
    }

    /**
     * 为view设置背景
     *
     * @param viewId
     * @param
     * @param
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ViewHolder setBackgroundColor(int viewId, int color) {

        getView(viewId).setBackgroundColor(color);

        return this;
    }

    /**
     * 为ImageView设置图片，通过bitmap
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ViewHolder setImageByBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片，通过网络下载
     *
     * @param viewId
     * @param url
     * @return
     */
    public ViewHolder setImageByUrl(int viewId, String url) {

        Uri uri = Uri.parse(url);

        ((SimpleDraweeView) getView(viewId)).setImageURI(uri);
        return this;
    }


    /**
     * 给checkbox修改状态
     *
     * @param viewId
     * @param isChecked
     * @return
     */
    public ViewHolder setCheckBoxChecked(int viewId, boolean isChecked) {
        CheckBox cb = getView(viewId);
        cb.setChecked(isChecked);
        return this;
    }

    /**
     * 给Radiobtn修改状态
     *
     * @param viewId
     * @param isChecked
     * @return
     */
    public ViewHolder setRadioBtnChecked(int viewId, boolean isChecked) {
        RadioButton rb = getView(viewId);
        rb.setChecked(isChecked);
        return this;
    }

    /**
     * 给Edittext设置不可编辑
     *
     * @param viewId
     * @param
     * @return
     */
    public ViewHolder setEditTextNotEdit(int viewId) {
        EditText et = getView(viewId);
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
        // 设置光标隐藏
        et.setCursorVisible(false);
        return this;
    }

    /**
     * 给Edittext设置可编辑
     *
     * @param viewId
     * @param
     * @return
     */
    public ViewHolder setEditTextYesEdit(int viewId) {
        EditText et = getView(viewId);
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        // 设置光标显示
        et.setCursorVisible(true);
        return this;
    }


    /**
     * 设置view的可见性
     *
     * @param viewId
     * @param Visibility
     * @return
     */
    public ViewHolder setViewVisibility(int viewId, int Visibility) {
        View view = getView(viewId);
        view.setVisibility(Visibility);
        return this;
    }

    public int getLayoutId() {
        return mLayoutId;
    }
    public void updatePosition(int position)
    {
        mPosition = position;
    }


}
