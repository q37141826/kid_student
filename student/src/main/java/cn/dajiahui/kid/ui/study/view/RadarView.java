package cn.dajiahui.kid.ui.study.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * 雷达图
 *
 * @author Administrator
 *
 */
public class RadarView extends View {

    private Paint mPaint = new Paint();
    private TextPaint tPaint = new TextPaint();

    private float[] mData = new float[]{90.0f, 80.0f, 70.0f, 60.0f, 50.0f, 40.0f};

    //外边颜色
    private final int OUT_BORDER_COLOR = Color.parseColor("#919AA4");
    //内边颜色
    private final int IN_BORDER_COLOR = Color.parseColor("#E0E0E0");
    //数字文字颜色
    private final int TEXT_NUMBER_COLOR = Color.parseColor("#647D91");
    //汉字文字颜色
    private final int TEXT_COLOR = Color.parseColor("#3B454E");
    //填充颜色
    private final int FILL_COLOR = Color.parseColor("#CED6DC");

    //文字与图的间距
    private final int SPACE = 18;
    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);

        tPaint.setTextSize(40f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int maxRound = (int) (Math.min(getWidth(), getHeight()) / 2 * 0.75);

        //绘制蜘蛛网
        canvas.save();

        canvas.translate(getWidth() / 2, getHeight() / 2);

        for(int i = 0; i < 6; i++) {

            mPaint.setColor(IN_BORDER_COLOR);
            mPaint.setStrokeWidth(2f);
            canvas.drawLine(0, 0, maxRound, 0, mPaint);

            for(int j = 3; j >= 1; j--) {
                canvas.drawLine((float)(maxRound * j / 4.0), 0, (float)(maxRound / 2.0 * j / 4.0), (float)(maxRound * Math.sqrt(3) / 2 * j / 4.0), mPaint);
            }

            mPaint.setColor(OUT_BORDER_COLOR);
            mPaint.setStrokeWidth(4f);
            canvas.drawLine(maxRound, 0, maxRound/2, (float) (-Math.sqrt(3)/2*maxRound-0.5), mPaint);

            canvas.rotate(60);
        }

        canvas.restore();

        //绘制文字
        canvas.save();

        canvas.translate(getWidth() / 2, getHeight() / 2);

        tPaint.setColor(TEXT_COLOR);
        canvas.drawText("发育", maxRound + SPACE, 0, tPaint);
        tPaint.setColor(TEXT_NUMBER_COLOR);
        canvas.drawText(mData[0] + "", maxRound + SPACE, tPaint.getTextSize() + SPACE / 4, tPaint);

        tPaint.setColor(TEXT_COLOR);
        canvas.drawText("推进", maxRound / 2 + SPACE, (float) (maxRound * Math.sqrt(3) / 2), tPaint);
        tPaint.setColor(TEXT_NUMBER_COLOR);
        canvas.drawText(mData[1] + "", maxRound / 2 + SPACE, (float) (maxRound * Math.sqrt(3) / 2 + tPaint.getTextSize() + SPACE / 4), tPaint);

        tPaint.setColor(TEXT_COLOR);
        canvas.drawText("生存", -maxRound / 2 - SPACE - tPaint.getTextSize() * 2, (float) (maxRound * Math.sqrt(3) / 2), tPaint);
        tPaint.setColor(TEXT_NUMBER_COLOR);
        canvas.drawText(mData[2] + "", -maxRound / 2 - SPACE - tPaint.getTextSize() * 2, (float) (maxRound * Math.sqrt(3) / 2 + tPaint.getTextSize() + SPACE / 4), tPaint);

        tPaint.setColor(TEXT_COLOR);
        canvas.drawText("输出", -maxRound - SPACE - tPaint.getTextSize() * 2, 0, tPaint);
        tPaint.setColor(TEXT_NUMBER_COLOR);
        canvas.drawText(mData[3] + "", -maxRound - SPACE - tPaint.getTextSize() * 2,  tPaint.getTextSize() + SPACE / 4, tPaint);

        tPaint.setColor(TEXT_COLOR);
        canvas.drawText("综合", -maxRound / 2 - SPACE * 2 - tPaint.getTextSize() * 2, -(float) (maxRound * Math.sqrt(3) / 2), tPaint);
        tPaint.setColor(TEXT_NUMBER_COLOR);
        canvas.drawText(mData[4] + "", -maxRound / 2 - SPACE * 2 - tPaint.getTextSize() * 2, -(float) (maxRound * Math.sqrt(3) / 2 - tPaint.getTextSize() + SPACE / 4), tPaint);

        tPaint.setColor(TEXT_COLOR);
        canvas.drawText("KDA", maxRound / 2 + SPACE * 2, -(float) (maxRound * Math.sqrt(3) / 2), tPaint);
        tPaint.setColor(TEXT_NUMBER_COLOR);
        canvas.drawText(mData[5] + "", maxRound / 2 + SPACE * 2, -(float) (maxRound * Math.sqrt(3) / 2 - tPaint.getTextSize() + SPACE / 4), tPaint);
        canvas.restore();

        //绘制内容区域
        canvas.save();

        canvas.translate(getWidth() / 2, getHeight() / 2);

        Paint paint = new Paint();
        paint.setColor(FILL_COLOR);
        paint.setAlpha(0x88);
        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.moveTo(mData[0] / 100 * maxRound, 0f);
        path.lineTo(mData[1] / 100 / 2 * maxRound, (float) (mData[1] / 100 * Math.sqrt(3) / 2 * maxRound));
        path.lineTo(-mData[2] / 100 / 2 * maxRound, (float) (mData[2] / 100 * Math.sqrt(3) / 2 * maxRound));
        path.lineTo(-mData[3] / 100 * maxRound, 0f);
        path.lineTo(-mData[4] / 100 / 2 * maxRound, (float) (-mData[4] / 100 * Math.sqrt(3) / 2 * maxRound));
        path.lineTo(mData[5] / 100 / 2 * maxRound, (float) (-mData[5] / 100 * Math.sqrt(3) / 2 * maxRound));

        path.close();

        canvas.drawPath(path, paint);

        canvas.restore();

    }

    //设置数据，需要在ui线程中调用
    public void setData(float[] data) {
        if(6 != data.length) {
            return;
        }
        this.mData = data;
        invalidate();
    }
}