package cn.dajiahui.kid.ui.homework.view;

/**
 * Created by Administrator on 2016/3/22.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import cn.dajiahui.kid.ui.homework.myinterface.OnMyScrollListener;

public class ScroollBottom extends ScrollView {
    private OnMyScrollListener onMyScrollListener;
    private ViewGroup contentView;
    private View contentView1;
    private int location = 0;

    public void setLocation(int location) {
        this.location = location;

    }

    public ScroollBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScroollBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScroollBottom(Context context) {
        this(context, null);
    }

    public OnMyScrollListener getOnMyScrollListener() {
        return onMyScrollListener;
    }

    public void setOnMyScrollListener(OnMyScrollListener onMyScrollListener) {
        this.onMyScrollListener = onMyScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        contentView = (ViewGroup) getChildAt(0);

        if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
            if (onMyScrollListener != null) {

                onMyScrollListener.onBottom(l, t, oldl, oldt);
            }
        } else if (getScrollY() == 0) {
            if (onMyScrollListener != null) {
                onMyScrollListener.onTop(l, t, oldl, oldt);
            }
        }
        int[] a = new int[2];
        contentView.getChildAt(0).getLocationOnScreen(a);
        if (a[1] + contentView.getChildAt(0).getHeight() > location - 80) {
            if (onMyScrollListener != null) {
                onMyScrollListener.onLocation(l, t, oldl, oldt);
            }
        }
    }


    private void doOnBorderListener() {
    }


}

