package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.fxtx.framework.util.BaseUtil;

/**
 * 自定义的可移动的view
 */


@SuppressLint("AppCompatCustomView")
public class MoveImageView extends ImageView {

    private int lastX = 0;
    private int lastY = 0;

    private int screenWidth = 0;
    //屏幕宽度
    private int screenHeight = 0;
    private Context context;
    //屏幕高度

    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public MoveImageView(Context context) {
        super(context);
        screenWidth = BaseUtil.getWidthPixels((Activity) context);
        screenHeight = BaseUtil.getHeightPixels((Activity) context);
        this.context = context;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
//                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;

                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }
                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(true);

                break;
            case MotionEvent.ACTION_OUTSIDE:

                break;
            default:
                return false;
        }
        return true;
    }

}

