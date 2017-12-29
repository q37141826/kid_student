package com.fxtx.framework.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.fxtx.framework.R;
import com.fxtx.framework.time.TimeUtil;

/**
 * 环形进度框
 */
public class TasksCompletedView extends View {
    
    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    private Paint mRingPaintBg;//圆环背景色
    // 画字体的画笔
    private Paint mTextPaint;
    // 圆形颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    private int mRingColorBg;//圆环背景色
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 字的长度
    private float mTxtWidth;
    // 字的高度
    private float mTxtHeight;
    
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mProgress = 0;
    
    private boolean isShowBf;
    private boolean showText = true;
    
    public void setShowText(boolean showText) {
        this.showText = showText;
    }
    
    public TasksCompletedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }
    
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TasksCompletedView, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_task_radius, 80);
        isShowBf = typeArray.getBoolean(R.styleable.TasksCompletedView_task_bfb, true);
        mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_task_strokeWidth, 10);
        mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_task_circleColor, 0xFFFFFFFF);
        mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_task_ringColor, 0xFFFFFFFF);
        mRingColorBg = typeArray.getColor(R.styleable.TasksCompletedView_task_ringColor_bg, 0xFFFFFF);
        mRingRadius = mRadius + mStrokeWidth / 2;
    }
    
    private void initVariable() {
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        if (isShowBf) {
            mRingPaint.setStrokeWidth(mStrokeWidth);
        } else {
            mRingPaint.setStrokeWidth(mStrokeWidth / 3);
        }
        
        mRingPaintBg = new Paint();
        mRingPaintBg.setAntiAlias(true);
        mRingPaintBg.setColor(mRingColorBg);
        mRingPaintBg.setStyle(Paint.Style.STROKE);
        mRingPaintBg.setStrokeWidth(mStrokeWidth);
        
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setARGB(255, 255, 255, 255);
        mTextPaint.setTextSize(mRadius / 2);
        mTextPaint.setColor(mRingColor);
        FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
        
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        
        //绘制弧形
        RectF oval = new RectF();
        oval.left = (mXCenter - mRingRadius);
        oval.top = (mYCenter - mRingRadius);
        oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
        oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
        float j = ((float) mProgress / mTotalProgress) * 360;
        
        //绘制加载的数量
        if (isShowBf) {
            if (mProgress != mTotalProgress) {
                canvas.drawArc(oval, -90 + j, 360 - j, false, mRingPaintBg);//背景圆
            }
        } else {
            canvas.drawArc(oval, 0, 360, false, mRingPaintBg);//背景圆
        }
        if (mProgress > 0) {
            mRingPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawArc(oval, -90, j, false, mRingPaint); //前景圆
        }
        if (showText) {
            //绘制文字
            String txt;
            if (isShowBf) {
                txt = mProgress + (isShowBf ? "%" : "");
            } else {
                
                txt = TimeUtil.getRecordingTimeFromMillis(mProgress);
            }
            mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
            canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
        }
    }
    
    
    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();
    }
    
    public void setmTotalProgress(int mTotalProgress) {
        this.mTotalProgress = mTotalProgress;
    }
    
}  