package cn.dajiahui.kid.ui.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import cn.dajiahui.kid.ui.study.bean.BeReadingBookPageDataItem;

/**
 * Created by lenovo on 2018/1/23.
 * <p>
 * 点读的view
 */

public class PointReadView extends RelativeLayout implements View.OnTouchListener {

    private Context context;
    private int position;
    private BeReadingBookPageDataItem bePlayReadingBook;
    private GetPointReadView pointReadView;
    /*图片在手机上显示的宽高*/
    public int mReallyWidth = 0;
    public int mReallyWeight = 0;

    public int mPointViewWidth = 0;//放大图片的宽
    public int mPointViewHeight = 0;//放大图片的高

    public int  mPointX;//放大的view的X點坐標
    public int  mPointY;//放大的view的Y點坐標

    public void setmPointX(int mPointX) {
        this.mPointX = mPointX;
    }

    public void setmPointY(int mPointY) {
        this.mPointY = mPointY;
    }

    public void setmPointViewWidth(int mPointViewWidth) {
        this.mPointViewWidth = mPointViewWidth;
    }

    public void setmPointViewHeight(int mPointViewHeight) {
        this.mPointViewHeight = mPointViewHeight;
    }

    public void setmReallyWidth(int mReallyWidth) {
        this.mReallyWidth = mReallyWidth;
    }

    public void setmReallyWeight(int mReallyWeight) {
        this.mReallyWeight = mReallyWeight;
    }

    public BeReadingBookPageDataItem getBePlayReadingBook() {
        return bePlayReadingBook;
    }

    public PointReadView(Context context, GetPointReadView pointReadView, int position, BeReadingBookPageDataItem bePlayReadingBook) {
        super(context);
        this.context = context;
        this.position = position;
        this.bePlayReadingBook = bePlayReadingBook;
        this.pointReadView = pointReadView;
        bePlayReadingBook.getWidth();
//        this.setBackgroundResource(R.drawable.select_readingbook_bg_red);
//        this.setOnClickListener(this);
        this.setOnTouchListener(this);

    }


    public PointReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


//    @Override
//    public void onClick(View v) {
//        pointReadView.getPointReadView((PointReadView) v, bePlayReadingBook);
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pointReadView.getPointReadView((PointReadView) v, bePlayReadingBook,event);
        return true;
    }


    public interface GetPointReadView {
        public void getPointReadView(PointReadView pointReadView, BeReadingBookPageDataItem bePlayReadingBook,MotionEvent event);
    }
}
