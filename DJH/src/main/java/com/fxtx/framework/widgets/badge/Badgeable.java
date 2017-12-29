package com.fxtx.framework.widgets.badge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ViewParent;

public interface Badgeable {
    /**
     * 显示圆点角标
     */
    void showCirclePointBadge();

    /**
     * 显示文字角标
     *
     * @param badgeText
     */
    void showTextBadge(String badgeText);

    /**
     * 隐藏角标
     */
    void hiddenBadge();

    /**
     * 显示图像角标
     *
     * @param bitmap
     */
    void showDrawableBadge(Bitmap bitmap);

    /**
     * 调用父类的onTouchEvent方法
     *
     * @param event
     * @return
     */
    boolean callSuperOnTouchEvent(MotionEvent event);

    /**
     * 拖动后抬起手指角标消失的代理
     *
     * @param delegate
     */
    void setDragDismissDelegage(DragDismissDelegate delegate);

    int getWidth();

    int getHeight();

    void postInvalidate();

    ViewParent getParent();

    int getId();

    boolean getGlobalVisibleRect(Rect r);

    Context getContext();
}