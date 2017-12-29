package com.fxtx.framework.adapter;

import android.content.Context;

import com.fxtx.framework.widgets.listview.SectionedBaseAdapter;

import java.util.List;

/**
 * Adapter 适配器，  支持多种布局同时实现
 */
public abstract class SectionedAdapter<T> extends SectionedBaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;


    public SectionedAdapter(Context context,List<T> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
    }
    @Override
    public long getItemId(int section, int position) {
        return position;
    }
    @Override
    public int getSectionCount() {
        return mDatas == null ? 0 : mDatas.size();//头部个数
    }

    public T getHeaderItem(int section) {
        return mDatas.get(section);
    }
}
