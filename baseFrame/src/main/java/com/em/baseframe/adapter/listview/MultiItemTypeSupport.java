package com.em.baseframe.adapter.listview;

/**
 * @title  listview的适配器工具
 * @date   2017/06/17
 * @author enmaoFu
 */
public interface MultiItemTypeSupport<T>
{
	int getLayoutId(int position, T t);

	int getViewTypeCount();

	int getItemViewType(int position, T t);
}