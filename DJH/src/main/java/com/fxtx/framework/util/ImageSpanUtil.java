package com.fxtx.framework.util;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

/**
 * Created by z on 2016/3/15.
 * 图片样式替换管理工具
 */
public class ImageSpanUtil {

    public static void setImageSpan(TextView tc, int resId, String ev, Boolean line, Boolean space) {
        final SpannableString ss = new SpannableString(ev);
        //得到drawable对象，即所要插入的图片
        Drawable d = tc.getResources().getDrawable(resId);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, ev.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tc.append(ss);
        if (line)
            tc.append("\n ");
        else
            tc.append(" ");
        if (space)
            tc.append(" ");
    }
}
