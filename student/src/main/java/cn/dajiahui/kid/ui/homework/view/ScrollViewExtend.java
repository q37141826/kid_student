package cn.dajiahui.kid.ui.homework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by mj on 2018/3/9.
 * <p>
 * 处理连线题的滑动冲突
 * <p>
 * 判断是横竖画屏
 */

public class ScrollViewExtend extends ScrollView {
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    public ScrollViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }
}