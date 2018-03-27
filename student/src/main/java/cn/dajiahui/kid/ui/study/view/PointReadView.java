package cn.dajiahui.kid.ui.study.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fxtx.framework.anim.AnimUtil;
import com.fxtx.framework.util.BitmapUtil;

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

//    public Boolean animotion = false;//false 未显示动画  true 显示动画ing

    public int mPointViewWidth = 0;//放大图片的宽
    public int mPointViewHeight = 0;//放大图片的高

    public int mPointX;//放大的view的X點坐標
    public int mPointY;//放大的view的Y點坐標
    public ImageView scaleimageView;//设置动画的view
    private FrameLayout fr_read_show;//添加动画的父视图
    private Bitmap mScaleBitmap;//添加动画的bitmap
    public Animation mAnimation;//动画实例

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


    public BeReadingBookPageDataItem getBePlayReadingBook() {
        return bePlayReadingBook;
    }

    public PointReadView(Context context, GetPointReadView pointReadView, int position, BeReadingBookPageDataItem bePlayReadingBook, FrameLayout fr_read_show) {
        super(context);
        this.context = context;
        this.position = position;
        this.bePlayReadingBook = bePlayReadingBook;
        this.pointReadView = pointReadView;
        this.fr_read_show = fr_read_show;
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
    /*设置动画*/
    public void startAnimotion(Bitmap mItemBitmap) {

      /*设置放大动画*/
        Matrix matrix = new Matrix();
        matrix.postScale(3, 3);// 缩放比例
        mScaleBitmap = BitmapUtil.createBitmap(mItemBitmap, this.mPointX, this.mPointY, this.mPointViewWidth, this.mPointViewHeight, matrix, true);
        scaleimageView = new ImageView(context);
        scaleimageView.setImageBitmap(mScaleBitmap);

        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(this.mPointViewWidth, this.mPointViewHeight);
        /*设置动画显示位置*/
        params.setMargins(this.mPointX, this.mPointY, 0, 0);

//      Logger.d("pointReadView.mPointX:" + pointReadView.mPointX + "  pointReadView.mPointY" + pointReadView.mPointY);

        scaleimageView.setLayoutParams(params);
        /*添加view到父布局上*/
        fr_read_show.addView(scaleimageView);
        /*设置动画时间*/
//        int i1 = Integer.parseInt(bePlayReadingBook.getEnd_time()) - Integer.parseInt(bePlayReadingBook.getStart_time());

        AnimUtil.magnifyingAnimation(scaleimageView, 1000).
                setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mAnimation = animation;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mAnimation = animation;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

    }


    /*清除动画*/
    public void cleanAnimotion() {
        if (scaleimageView != null && mScaleBitmap != null && mAnimation != null) {
            mAnimation.cancel();
            scaleimageView.clearAnimation();
            fr_read_show.removeView(scaleimageView);
//            animotion = false;
            if (mScaleBitmap != null) {
                mScaleBitmap.recycle();
                mScaleBitmap = null;
            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pointReadView.getPointReadView((PointReadView) v, bePlayReadingBook, event);
        return true;
    }


    public interface GetPointReadView {
        public void getPointReadView(PointReadView pointReadView, BeReadingBookPageDataItem bePlayReadingBook, MotionEvent event);
    }
}
