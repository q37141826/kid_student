package com.fxtx.framework.weekCalender;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fxtx.framework.R;
import com.fxtx.framework.text.ParseUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.time.TimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wdj on 2016/3/30.
 */
public class WeekPagerAdapter extends PagerAdapter {
    private Calendar week;
    private Context context;
    private String timeTime;

    public WeekPagerAdapter(Calendar calendar, Context context) {
        this.week = calendar;
        timeTime = TimeUtil.timeFormat(calendar.getTimeInMillis() + "", TimeUtil.yyMD);
        this.context = context;
    }

    public String getTime() {
        return timeTime;
    }

    public void setCalendar(Calendar calendar) {
        this.week = calendar;
        timeTime = TimeUtil.timeFormat(calendar.getTimeInMillis() + "", TimeUtil.yyMD);
        this.notifyDataSetChanged();
    }

    private OnWeekItemClick click;

    public void setMyItemClick(OnWeekItemClick myItemClick) {
        this.click = myItemClick;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        WeekCard myCalenderCard = new WeekCard(context);
        myCalenderCard.getMyGroup().setMyItemClick(click);
        myCalenderCard.setTag(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(week.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, (position - WeekCalenderPager.oneIndex) * 7);
       int month= (calendar.get(Calendar.MONTH) + 1);
        myCalenderCard.setMonth(month + "");
        String monthDetail =TimeUtil.getWeekData(calendar);
        myCalenderCard.setTitle(monthDetail);
        List<String>numbers =StringUtil.getNumbers(monthDetail);
        List<Integer>dayofmonth= new ArrayList<Integer>();
        if (numbers.size()==3){
            dayofmonth = StringUtil.getDayOfMonth(ParseUtil.parseInt(numbers.get(1)), ParseUtil.parseInt(numbers.get(2)), ParseUtil.parseInt(numbers.get(0)));
        }else {
            dayofmonth = StringUtil.getDayOfMonth(ParseUtil.parseInt(numbers.get(1)), ParseUtil.parseInt(numbers.get(3)), ParseUtil.parseInt(numbers.get(0)));
        }
        List<TextView> tvDays = myCalenderCard.getTvDays();
        for (int i = 0;i<7;i++){
            tvDays.get(i).setText(tvDays.get(i).getText()+"\n\n"+dayofmonth.get(i));
            if (month==(week.get(Calendar.MONTH)+1)){
//                int w = week.get(Calendar.DATE);
                if (dayofmonth.get(i)==week.get(Calendar.DATE)){
                    tvDays.get(i).setBackgroundColor(context.getResources().getColor(R.color.app_bg));
                }
            }
        }
        container.addView(myCalenderCard);
        return myCalenderCard;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((WeekCard) object);
    }

    @Override
    public int getCount() {
        return 54;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((WeekCard) object);
    }

    private boolean isClean = false;

    public void cleanView(boolean isClean) {
        this.isClean = isClean;
    }

    @Override
    public int getItemPosition(Object object) {
//        return isClean ? POSITION_NONE : super.getItemPosition(object);
        return POSITION_NONE;
    }

    public boolean isClean() {
        return isClean;
    }
}
