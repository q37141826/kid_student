package com.fxtx.framework.pickerview.view;

import android.content.Context;
import android.view.View;

import com.fxtx.framework.R;
import com.fxtx.framework.pickerview.TimePickerView;
import com.fxtx.framework.pickerview.adapter.NumericWheelAdapter;
import com.fxtx.framework.pickerview.lib.WheelView;
import com.fxtx.framework.pickerview.listener.OnItemSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class WheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;

    private TimePickerView.Type type;
    public static final int DEFULT_START_YEAR = 1900;
    public static final int DEFULT_END_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static final int DEFULT_START_MONTH = Calendar.getInstance().get(Calendar.MONTH);
    private int startYear = DEFULT_START_YEAR;
    private int endYear = DEFULT_END_YEAR;
    private int endMonth = DEFULT_START_MONTH;
    private int year_num;
    private int year1 = 0;
    private int month1 = 0;
    private int day1 = 0;


    public WheelTime(View view) {
        super();
        this.view = view;
        type = TimePickerView.Type.ALL;
        setView(view);
    }

    public WheelTime(View view, TimePickerView.Type type) {
        super();
        this.view = view;
        this.type = type;
        setView(view);
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0);
    }

    /**
     * majin fix bug 2017.9.20 修改生日不能有未来时间bug
     * 判断日期
     * setMonth:true-设置月份  false-设置日
     * cYear，，year ，month，cMonth：判断条件
     * day：今年今月的天数
     * all：整月天数
     */
    private void JudgementDate(boolean setMonth, int cYear, int year, int cMonth, int month, int day, int all) {

        if (setMonth == true) {//设置month
            if (cYear == year) {
                wv_month.setAdapter(new NumericWheelAdapter(1, month + 1));
            } else {
                wv_month.setAdapter(new NumericWheelAdapter(1, all));
            }
        } else {//设置day
            if (cYear == year && month == cMonth) {
                wv_day.setAdapter(new NumericWheelAdapter(1, day));
            } else {
                wv_day.setAdapter(new NumericWheelAdapter(1, all));
            }
        }
    }

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void setPicker(int year, int month, int day, int h, int m) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        Context context = view.getContext();
        this.year1 = year;
        this.month1 = month;
        this.day1= day;
        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
//		wv_year.setLabel(context.getString(R.string.pickerview_year));// 添加文字
        wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);


        JudgementDate(true, endYear, year, endMonth, month, day, 12);
//		wv_month.setLabel(context.getString(R.string.pickerview_month));
        wv_month.setCurrentItem(month);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {

            JudgementDate(false, endYear, year, endMonth, month, day, 31);
        } else if (list_little.contains(String.valueOf(month + 1))) {

            JudgementDate(false, endYear, year, endMonth, month, day, 30);
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                JudgementDate(false, endYear, year, endMonth, month, day, 29);
            } else {

                JudgementDate(false, endYear, year, endMonth, month, day, 28);
            }
        }
//		wv_day.setLabel(context.getString(R.string.pickerview_day));
        wv_day.setCurrentItem(day - 1);


        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字
        wv_hours.setCurrentItem(h);

        wv_mins = (WheelView) view.findViewById(R.id.min);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
        wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字
        wv_mins.setCurrentItem(m);

        // 添加"年"监听
        OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                year_num = index + startYear;
                JudgementDate(true, year_num, year1, endMonth, month1, day1, 12);
                // 判断大小月及是否闰年,用来确定"日"的数据
                int maxItem = 30;
                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    JudgementDate(false, year_num, year1, wv_month.getCurrentItem() + 1, month1 + 1, day1, 31);
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {

                    JudgementDate(false, year_num, year1, wv_month.getCurrentItem() + 1, month1 + 1, day1, 30);
                    maxItem = 30;
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0) {

                        JudgementDate(false, year_num, year1, wv_month.getCurrentItem() + 1, month1 + 1, day1, 29);
                        maxItem = 29;
                    } else {

                        JudgementDate(false, year_num, year1, wv_month.getCurrentItem() + 1, month1 + 1, day1, 28);
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }
            }
        };
        // 添加"月"监听
        OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;
                int maxItem = 30;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {

                    JudgementDate(false, wv_year.getCurrentItem()+startYear, year1, month_num, month1 + 1, day1, 31);
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(month_num))) {

                    JudgementDate(false, wv_year.getCurrentItem()+startYear, year1, month_num, month1 + 1, day1, 30);
                    maxItem = 30;
                } else {
                    if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
                            .getCurrentItem() + startYear) % 100 != 0)
                            || (wv_year.getCurrentItem() + startYear) % 400 == 0) {

                        JudgementDate(false, wv_year.getCurrentItem()+startYear, year1, month_num, month1 + 1, day1, 29);
                        maxItem = 29;
                    } else {

                        JudgementDate(false, wv_year.getCurrentItem()+startYear, year1, month_num, month1 + 1, day1, 28);
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

            }
        };
        wv_year.setOnItemSelectedListener(wheelListener_year);
        wv_month.setOnItemSelectedListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 6;
        switch (type) {
            case ALL:
                textSize = textSize * 3;
                break;
            case YEAR_MONTH_DAY:
                textSize = textSize * 4;
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                break;
            case HOURS_MINS:
                textSize = textSize * 4;
                wv_year.setVisibility(View.GONE);
                wv_month.setVisibility(View.GONE);
                wv_day.setVisibility(View.GONE);
                break;
            case MONTH_DAY_HOUR_MIN:
                textSize = textSize * 3;
                wv_year.setVisibility(View.GONE);
                break;
            case YEAR_MONTH:
                textSize = textSize * 4;
                wv_day.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
        }
        wv_day.setTextSize(textSize);
        wv_month.setTextSize(textSize);
        wv_year.setTextSize(textSize);
        wv_hours.setTextSize(textSize);
        wv_mins.setTextSize(textSize);

    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_year.setCyclic(cyclic);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_mins.setCyclic(cyclic);
    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                .append((wv_month.getCurrentItem() + 1)).append("-")
                .append((wv_day.getCurrentItem() + 1)).append(" ")
                .append(wv_hours.getCurrentItem()).append(":")
                .append(wv_mins.getCurrentItem());
        return sb.toString();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
}
