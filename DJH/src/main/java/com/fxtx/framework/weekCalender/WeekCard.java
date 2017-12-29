package com.fxtx.framework.weekCalender;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxtx.framework.R;

import java.util.ArrayList;
import java.util.List;

public class WeekCard extends LinearLayout {
    private TextView tvMonth;
    private TextView tvTitle;
    private WeekView myGroup; //周历数据
    private List<TextView>tvDays = new ArrayList<TextView>();
    private TextView tvDay1,tvDay2,tvDay3,tvDay4,tvDay5,tvDay6,tvDay7;
    public WeekView getMyGroup() {
        return myGroup;
    }

    public List<TextView> getTvDays() {
        return tvDays;
    }

    public WeekCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeekCard(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.setOrientation(VERTICAL);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.setOrientation(VERTICAL);
        this.setBackgroundResource(R.color.white);
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_week, this);
        tvMonth = (TextView) layout.findViewById(R.id.title_month);
        tvTitle = (TextView) layout.findViewById(R.id.weekcalender_title);
        myGroup = (WeekView) layout.findViewById(R.id.myGroup1);
        tvDay1= (TextView) layout.findViewById(R.id.tv_day1);
        tvDay2= (TextView) layout.findViewById(R.id.tv_day2);
        tvDay3= (TextView) layout.findViewById(R.id.tv_day3);
        tvDay4= (TextView) layout.findViewById(R.id.tv_day4);
        tvDay5= (TextView) layout.findViewById(R.id.tv_day5);
        tvDay6= (TextView) layout.findViewById(R.id.tv_day6);
        tvDay7= (TextView) layout.findViewById(R.id.tv_day7);
        tvDays.add(tvDay1);
        tvDays.add(tvDay2);
        tvDays.add(tvDay3);
        tvDays.add(tvDay4);
        tvDays.add(tvDay5);
        tvDays.add(tvDay6);
        tvDays.add(tvDay7);
    }


    public void setMonth(String month) {
        tvMonth.setText(month + "月");
    }

    public void setTitle(String str) {
        tvTitle.setText(str);
    }
}
