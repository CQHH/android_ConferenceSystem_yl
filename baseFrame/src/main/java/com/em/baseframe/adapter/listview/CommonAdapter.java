package com.em.baseframe.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @title  listview的通用适配器
 * @date   2017/06/17
 * @author enmaoFu
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    //设置public方便存取数据
    protected List<T> mDatas;
    protected final int mItemLayoutId;
    protected AdapterCallback adapterCallback;

    public CommonAdapter(Context context, List<T> mList, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mList;
        this.mItemLayoutId = itemLayoutId;
    }

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId,
                         AdapterCallback adapterCallback) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        this.adapterCallback = adapterCallback;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {

        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();

    }

    /**
     * 添加一个对象
     */
    public void addInfo(T info) {
        if (mDatas == null) {
          return;
        }
        mDatas.add(info);
        notifyDataSetChanged();
    }

    /**
     * 删除一个对象
     */
    public void removeInfo(T info) {
        if (mDatas != null) {
            mDatas.remove(info);
            notifyDataSetChanged();
        }

    }
    /**
     * 设置数据
     */
    public void setDatas(List<T> list_) {

            mDatas=list_;
            notifyDataSetChanged();

    }

    /**
     * 设置对应的数据
     * @param po
     * @param info
     */
    public void setData(int po,T info){
        mDatas.set(po,info);
        notifyDataSetChanged();
    }

    /**
     * 删除指定对象
     * @param po
     */
    public void remove(int po){
        if (mDatas != null) {
            mDatas.remove(po);
            notifyDataSetChanged();
        }
    }

    /**
     * 删除多个对象
     */
    public void removeInfos(List<T> list_) {
        if (mDatas != null) {
            if (list_ != null && list_.size() > 0) {
                for (T t : list_) {
                    mDatas.remove(t);
                }
                notifyDataSetChanged();
            }

        }


    }

    /**
     * 添加所有
     */
    public void addAll(List<T> list_) {
        mDatas.addAll(list_);
        notifyDataSetChanged();
        //temp = null;
    }


    /**
     * 查找所有
     */
    public List<T> findAll() {
        if (mDatas != null) {
            return mDatas;
        } else {
            return null;
        }
    }

    /**
     * 删除所有
     */
    public void removeAll() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public abstract void convert(ViewHolder holder, T item, int positon);

    public ViewHolder getViewHolder(int position, View convertView,
                                    ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,position
        );
    }



}
