package com.hhxk.app.adapter;


import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.pojo.MaterialPojo;

import java.util.List;

/**
 * @title  发起会议-会议材料recyclerview适配器
 * @date   2019/02/20
 * @author enmaoFu
 */
public class MaterialAdapter extends BaseQuickAdapter<MaterialPojo,BaseViewHolder> {
    public MaterialAdapter(int layoutResId, List<MaterialPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialPojo item) {
    }
}
