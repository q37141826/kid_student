package com.fxtx.framework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author djh-zy
 * @version :1
 * @CreateDate 2015年6月9日 下午3:32:06
 * @description :
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private int itemViewType;

    private ViewHolder(Context context, ViewGroup group, int layoutid, int itemViewType) {
        this.mConvertView = LayoutInflater.from(context).inflate(layoutid,
                group, false);
        mViews = new SparseArray<View>();
        this.itemViewType = itemViewType;
        this.mConvertView.setTag(this);
    }

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public static ViewHolder getHolder(Context context, View convertView,
                                       ViewGroup parent, int layoutId, int itemViewType) {
        if (convertView != null) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder != null && viewHolder.getItemViewType() == itemViewType) {
                return viewHolder;
            }
        }
        return new ViewHolder(context, parent, layoutId, itemViewType);
    }

    public ViewHolder(Context context, int layoutid) {
        this.mConvertView = LayoutInflater.from(context)
                .inflate(layoutid, null);
        mViews = new SparseArray<View>();
        this.mConvertView.setTag(this);
    }

    public <T extends View> T getConvertView() {
        return (T) this.mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 通过控件的Id控制控件是否显示
     *
     * @param viewId
     * @param visibility
     */
    public void setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
    }

    /**
     * 通过控件的Id控制控件是否可点击
     *
     * @param viewId
     */
    public void setClickable(int viewId, boolean clickable) {
        View view = getView(viewId);
        view.setClickable(clickable);
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public void setText(int viewId, String text) {
        TextView view = getView(viewId);
        if (view != null && text != null) {
            view.setText(text);
        }
    }

    public void setText(int viewId, int resid) {
        TextView view = getView(viewId);
        if (view != null) {
            view.setText(resid);
        }
    }

    public void setText(int viewId, Spanned spanned) {
        TextView view = getView(viewId);
        if (view != null && spanned != null) {
            view.setText(spanned);
        }
    }

    public void setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        if (view != null) {
            view.setTextColor(color);
        }
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public void setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageResource(drawableId);
        }
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public void setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageBitmap(bm);
        }
    }

    /**
     * 清空 mViews
     */
    public void clearViews() {
        mViews.clear();
        mViews = null;
    }
}
