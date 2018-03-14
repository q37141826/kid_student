package cn.dajiahui.kid.ui.study.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 排版用TextView
 * Created by wangzhi on 2018/3/14.
 */
@SuppressLint("AppCompatCustomView")
public class CompositionTextView extends TextView {
    public CompositionTextView(Context context) {
        super(context);
    }

    //xml创建时调用这个构造函数
    public CompositionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //new CompositionTextView调用这个构造函数
    public CompositionTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 一行显示的最大宽度
     * @return
     */
    private int getAvailableWidth()
    {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * 判断文字是否超过textView的宽度
     * @return
     */
    public boolean isOverFlowed()
    {
        Paint paint = getPaint();
        float width = paint.measureText(getText().toString());
        if (width > getAvailableWidth()) {
            return true;
        } else {
            return false;
        }
    }
}
