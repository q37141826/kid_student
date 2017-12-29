package cn.dajiahui.kid.ui.task.myinterface;

/**
 * Created by Administrator on 2016/3/22.
 */
public interface OnMyScrollListener {
    void onBottom(int l, int t, int oldl, int oldt);

    void onTop(int l, int t, int oldl, int oldt);
    void onLocation(int l, int t, int oldl, int oldt);
}
