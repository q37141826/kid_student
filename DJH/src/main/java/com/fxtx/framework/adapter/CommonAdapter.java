package com.fxtx.framework.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Adapter 适配器，  支持多种布局同时实现
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    private int[] itemLayoutId;
    protected int itemSelect;//当前选择的

    public CommonAdapter(Context context, List<T> mDatas, int... itemLayoutId) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.itemLayoutId = itemLayoutId;
    }

    public CommonAdapter(Context context,int itemSelect,List<T>mDatas ,int... itemLayoutId){
        this.mContext = context;
        this.mDatas = mDatas;
        this.itemLayoutId = itemLayoutId;
        this.itemSelect = itemSelect;
    }
    public void setItemSelect(int itemSelect) {
        this.itemSelect = itemSelect;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    @Override
    public int getViewTypeCount() {
        return itemLayoutId.length;
    }

    @Override
    public int getItemViewType(int position) {
        return itemSelect;
    }

    @Override
    public T getItem(int position) {
        if (position >= mDatas.size())
            return null;
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getHolder(mContext, convertView,
                parent, itemLayoutId[itemSelect], itemSelect);
        convert(viewHolder, position, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder viewHolder, int position, T item);

}
