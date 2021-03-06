package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/1/13.
 */

public class Point implements Serializable {
    public int x = 0;
    public int y = 0;
    public String val;//view的值

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, String val) {
        this.x = x;
        this.y = y;
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
