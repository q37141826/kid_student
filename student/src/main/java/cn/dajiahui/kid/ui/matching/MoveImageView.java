package cn.dajiahui.kid.ui.matching;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import cn.dajiahui.kid.ui.myclass.bean.Matching;

/**
 * 自定义的可移动的view
 */


@SuppressLint("AppCompatCustomView")
public class MoveImageView extends TextView {

    private int lastX = 0;
    private int lastY = 0;

    private int screenWidth = 0;
    //屏幕宽度
    private int screenHeight = 0;
    //屏幕高度
    private Matching matching;
    private Context context;


    public MoveImageView(Context context, int screenWidth, int screenHeight, Matching matching) {
        super(context);
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.matching = matching;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
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
                break;
            case MotionEvent.ACTION_UP:
                /*getWidth()  单位 px*/
//                Log.d("majin", " 状态栏:  " + statusBarHeight);
//                Log.d("majin", " 手指抬起X:  " + lastX + " 手指抬起Y: " + (lastY - statusBarHeight));
//
//                Log.d("majin", " 控件宽:  " + matching.getTextView().getWidth() + " 控件高：" + matching.getTextView().getHeight());
//
//                Log.d("majin", " textview2X:  " + matching.getTextView().getX() + " textvie2Y: " + matching.getTextView().getY());


                float wx = (matching.getTextView().getX() + matching.getTextView().getWidth());
                float wy = (matching.getTextView().getY() + matching.getTextView().getHeight());

                boolean XT = (float) lastX > matching.getTextView().getX();
                boolean YT = (float) (lastY - getStatusBarHeight(context)) > matching.getTextView().getY();
                boolean WT = (float) lastX < wx;
                boolean HT = (float) (lastY - getStatusBarHeight(context)) < wy;


                if (XT && YT && WT && HT) {
                    Toast.makeText(context, "恭喜您回答正确！" + matching.getTextView().getText(), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(context, "回答错误！！", Toast.LENGTH_SHORT).show();

                    this.setTop(matching.getTopMargin());
                    this.setLeft(matching.getLeftMargin());
                    this.setWidth(matching.getTextView().getWidth());
                    this.setHeight(matching.getTextView().getHeight());
//                    invalidate();
                }


                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }


}
