package com.hhxk.app.adapter;


import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.hhxk.app.pojo.MaterialDetailsPojo;
import com.hhxk.app.pojo.MaterialPojo;

import java.util.List;

/**
 * @title  会议详情-会议材料recyclerview适配器
 * @date   2019/02/25
 * @author enmaoFu
 */
public class MaterialDetailsAdapter extends BaseQuickAdapter<MaterialDetailsPojo,BaseViewHolder> {
    public MaterialDetailsAdapter(int layoutResId, List<MaterialDetailsPojo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialDetailsPojo item) {

    }
}
