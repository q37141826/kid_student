package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by lenovo on 2018/1/16.
 */

@SuppressLint("AppCompatCustomView")
public class ProhibitMoveSeekbar extends SeekBar {


    public ProhibitMoveSeekbar(Context context) {
        super(context);
// TODO Auto-generated constructor stub
    }


    public ProhibitMoveSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }


    public ProhibitMoveSeekbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * onTouchEvent 是在 SeekBar 继承的抽象类 AbsSeekBar 里 你可以看下他们的继承关系
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
// 原来是要将TouchEvent传递下去的,我们不让它传递下去就行了
// return super.onTouchEvent(event);


        return false;
    }


}


