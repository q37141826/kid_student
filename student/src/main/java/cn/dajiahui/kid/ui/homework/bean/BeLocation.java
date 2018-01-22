package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/1/16.
 * <p>
 * 排序位置
 */

public class BeLocation implements Serializable {

    private int getLeft;//距离左边
    private int getTop;//上部
    private int getRight;//右边
    private int getBottom;//底部
    private int width;//宽
    private int height;//高

    public BeLocation(int getLeft, int getTop, int getRight, int getBottom, int width, int height) {
        this.getLeft = getLeft;
        this.getTop = getTop;
        this.getRight = getRight;
        this.getBottom = getBottom;
        this.width = width;
        this.height = height;
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

                ", getLeft=" + getLeft +
                ", getTop=" + getTop +
                ", getRight=" + getRight +
                ", getBottom=" + getBottom +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
