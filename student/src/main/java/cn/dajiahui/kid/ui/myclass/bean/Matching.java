package cn.dajiahui.kid.ui.myclass.bean;

import android.widget.TextView;

/**
 * Created by lenovo on 2017/12/8.
 */

public class Matching {
    public Matching(TextView textView, int leftMargin, int topMargin, int color) {
        this.textView = textView;
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.color = color;
    }

    int color;
    TextView textView;
    int leftMargin;
    int topMargin;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }



    public int getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }


    public Matching() {
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }


    public Matching(TextView textView) {

        this.textView = textView;
    }


}
