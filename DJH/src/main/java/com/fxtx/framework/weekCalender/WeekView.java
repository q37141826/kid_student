package com.fxtx.framework.weekCalender;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fxtx.framework.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeekView extends ViewGroup {
    private OnWeekItemClick myItemClick;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    //  线的集合
    private List<View> lines = new ArrayList<View>();
    // 课程的集合
    private List<BeWeekReck> list = new ArrayList<BeWeekReck>();

    public void setGroupData(List<BeWeekReck> list) {
        this.list = list;
        this.init();
        this.requestLayout();
        this.forceLayout();
    }

    public void setMyItemClick(OnWeekItemClick myItemClick) {
        this.myItemClick = myItemClick;
    }

    public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context) {
        this(context, null);
    }

    public void init() {
        lines.clear();
        this.removeAllViews();

        // 文字的设置
        for (BeWeekReck reck : list) {
            TextView textView = new TextView(getContext());
            textView.setTag(reck.getObjectId());
            textView.setBackgroundResource(R.color.app_bg_a);
            textView.setOnClickListener(new MyClick(reck));
            this.addView(textView);
        }
        // 线的设置
        for (int i = 0; i < 6; i++) {
            View lineview = new View(getContext());
            lineview.setBackgroundResource(R.color.app_bg_a);
            lineview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
            this.addView(lineview);
            lines.add(lineview);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = r - l;
        int h = b - t;
        for (int i = 0; i < lines.size(); i++) {
            int x = i * 1;
            int heithc = h * (i + 1) / 7;
            lines.get(i).layout(0, heithc - 1, r, heithc);
        }
        for (BeWeekReck reck : list) {
            TextView textView = (TextView) this.findViewWithTag(reck.getObjectId());
            if (textView != null) {
                int weithc = w / 7;
                int c = reck.getWeek() * 1;
                int x = weithc * reck.getWeek();
                textView.layout(x + c - 1,
                        getStartDurationDistance(h, reck.getStartTime()),
                        x + weithc + c, getStartDurationDistance(h, reck.getStartTime())
                                + getDurationDistance(h, reck.getEndTime(), reck.getStartTime()));
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private class MyClick implements OnClickListener {
        private BeWeekReck reck;

        public MyClick(BeWeekReck reck) {
            this.reck = reck;
        }

        @Override
        public void onClick(View v) {
            if (myItemClick != null) {
                myItemClick.onMyItemClick(v, reck);
            }
        }
    }

    /**
     * 算出开始时间与结束时间的时间差 加上年月日（因为是同一天 无所谓真正的年月日）
     *
     * @param endTime
     * @param startTime
     */
    private int getDuration(String endTime, String startTime) {
        try {
            Date endDate = sdf.parse("2016-3-29 " + endTime);
            Date startDate = sdf.parse("2016-3-29 " + startTime);
            return (int) (endDate.getTime() - startDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 算出开始时间与8点的时间差 加上年月日（因为是同一天 无所谓真正的年月日）
     *
     * @param startTime
     * @return
     */
    private int getStartDurationTime(String startTime) {
        try {
            Date endDate = sdf.parse("2016-3-29 08:00");
            Date startDate = sdf.parse("2016-3-29 " + startTime);
            return (int) (startDate.getTime() - endDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 算出距离顶部的距离
     *
     * @param height    整个的高度
     * @param startTime
     * @return
     */
    private int getStartDurationDistance(int height, String startTime) {
        int startDurationTime = getStartDurationTime(startTime);
        int durationTime = getDuration("22:00", "8:00");
        int startDurationDistance = (int) (startDurationTime / (float) durationTime * height);
        return startDurationDistance;
    }

    /**
     * 算出高度
     *
     * @param height
     * @param endTime
     * @param startTime
     * @return
     */
    private int getDurationDistance(int height, String endTime, String startTime) {
        int viewDurationTime = getDuration(endTime, startTime);
        int durationTime = getDuration("22:00", "8:00");
        return (int) (viewDurationTime / (float) durationTime * height);
    }
}
