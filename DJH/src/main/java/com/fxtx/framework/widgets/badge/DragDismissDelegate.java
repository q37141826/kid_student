
package com.fxtx.framework.widgets.badge;

public interface DragDismissDelegate {

    /**
     * 拖动大于BadgeViewHelper.mMoveHiddenThreshold后抬起手指角标消失的回调方法
     *
     * @param badgeable
     */
    void onDismiss(Badgeable badgeable);
}