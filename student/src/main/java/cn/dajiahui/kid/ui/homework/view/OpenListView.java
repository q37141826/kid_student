package cn.dajiahui.kid.ui.homework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by lenovo on 2018/1/14.
 *
 * 解決ScrollView嵌套listview
 */

public class OpenListView extends ListView {
    public OpenListView(Context context) {
        super(context);
    }

    public OpenListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec
                ,MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST));
    }
}
