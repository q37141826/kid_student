package cn.dajiahui.kid.ui.homework.bean;

import android.content.Context;

/**
 * Created by lenovo on 2018/1/16.
 */

public class BeLocation {
    private Context context;
    private int getLeft;
    private int getTop;
    private int getRight;
    private int getBottom;
    private int width;
    private int height;

    public BeLocation(int getLeft, int getTop, int getRight, int getBottom, int width, int height) {
        this.getLeft = getLeft;
        this.getTop = getTop;
        this.getRight = getRight;
        this.getBottom = getBottom;
        this.width = width;
        this.height = height;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getGetLeft() {
        return getLeft;
    }

    public void setGetLeft(int getLeft) {
        this.getLeft = getLeft;
    }

    public int getGetTop() {
        return getTop;
    }

    public void setGetTop(int getTop) {
        this.getTop = getTop;
    }

    public int getGetRight() {
        return getRight;
    }

    public void setGetRight(int getRight) {
        this.getRight = getRight;
    }

    public int getGetBottom() {
        return getBottom;
    }

    public void setGetBottom(int getBottom) {
        this.getBottom = getBottom;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "BeLocation{" +
                "context=" + context +
                ", getLeft=" + getLeft +
                ", getTop=" + getTop +
                ", getRight=" + getRight +
                ", getBottom=" + getBottom +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
