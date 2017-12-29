package com.fxtx.framework.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.view.Gravity.CENTER;


public class CalendarCard extends RelativeLayout {
    private TextView cardTitle;
    private int itemLayout = R.layout.card_item_simple;
    private OnItemRender mOnItemRender;
    private OnItemRender mOnItemRenderDefault;
    private OnCellItemClick mOnCellItemClick;
    private Calendar dateDisplay;
    private ArrayList<CheckableLayout> cells = new ArrayList<CheckableLayout>();
    private LinearLayout cardGrid;

    public CalendarCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CalendarCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarCard(Context context) {
        super(context);
        init(context);
    }

    private void init(Context ctx) {
        if (isInEditMode()) return;
        View layout = LayoutInflater.from(ctx).inflate(R.layout.card_view, null, false);
        if (dateDisplay == null)
            dateDisplay = Calendar.getInstance();
        cardTitle = (TextView) layout.findViewById(R.id.cardTitle);
        cardGrid = (LinearLayout) layout.findViewById(R.id.cardGrid);
        cardTitle.setText(new SimpleDateFormat("yyyy年M月", Locale.getDefault()).format(dateDisplay.getTime()));
        LayoutInflater la = LayoutInflater.from(ctx);
        for (int y = 0; y < cardGrid.getChildCount(); y++) {
            LinearLayout row = (LinearLayout) cardGrid.getChildAt(y);
            for (int x = 0; x < row.getChildCount(); x++) {
                //创建layout 对象
                CheckableLayout cell = (CheckableLayout) row.getChildAt(x);
                cell.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getOnCellItemClick() != null)
                            getOnCellItemClick().onCellClick(v, (CardGridItem) v.getTag());
                    }
                });
                la.inflate(itemLayout, cell);
                cells.add(cell);
            }
        }
        addView(layout);
        mOnItemRenderDefault = new OnItemRender() {
            @Override
            public void onRender(CheckableLayout v, CardGridItem item) {

            }
        };
        updateCells();
    }


    private int getDaySpacing(int dayOfWeek) {
        if (Calendar.SUNDAY == dayOfWeek)
            return 6;
        else
            return dayOfWeek - 2;
    }

    private int getDaySpacingEnd(int dayOfWeek) {
        return 8 - dayOfWeek;
    }

    private void updateCells() {
        Calendar cal;
        Integer counter = 0;
        if (dateDisplay != null)
            cal = (Calendar) dateDisplay.clone();
        else
            cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);//一月
        int daySpacing = getDaySpacing(cal.get(Calendar.DAY_OF_WEEK));
        if (daySpacing > 0) {//如是不是周一
            //显示上周的周日历
            //上一个月的数据不显示
            for (int i = 0; i < daySpacing; ++i) {
                CheckableLayout cell = cells.get(counter);
                cell.setEnabled(false);
                cell.setBackgroundResource(R.color.white);
                ((TextView) cell.getChildAt(0)).setText("");
                counter++;
            }
        }
        int firstDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastDay = cal.get(Calendar.DAY_OF_MONTH) + 1;
        for (int i = firstDay; i < lastDay; i++) {
            //显示本月数据
            cal.set(Calendar.DAY_OF_MONTH, i - 1);
            Calendar date = (Calendar) cal.clone();
            date.add(Calendar.DAY_OF_MONTH, 1);
            CheckableLayout cell = cells.get(counter);
            CardGridItem s = new CardGridItem(i).setEnabled(true).setDate(date);
            cell.setTag(s);
            cell.setEnabled(true);
            cell.setGravity(CENTER);
            cell.setBackgroundResource(R.drawable.card_item_bg);
            ((TextView) cell.getChildAt(0)).setText(i + "");
            cell.setVisibility(View.VISIBLE);
            counter++;
        }

        if (dateDisplay != null)
            cal = (Calendar) dateDisplay.clone();
        else
            cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        daySpacing = getDaySpacingEnd(cal.get(Calendar.DAY_OF_WEEK));
        if (daySpacing > 0 && daySpacing != 7) {
            for (int i = 0; i < daySpacing; i++) {
                CheckableLayout cell = cells.get(counter);
                cell.setBackgroundResource(R.color.white);
                cell.setEnabled(false);
                cell.setVisibility(View.VISIBLE);
                ((TextView) cell.getChildAt(0)).setText("");
                counter++;
            }
        }
        for (int i = counter; i < cells.size(); i++) {
            CheckableLayout cell = cells.get(i);
            ((TextView)cell.getChildAt(0)).setText("");
            cell.setBackgroundResource(R.color.white);
            cell.setEnabled(false);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getMeasuredWidth() / 7;
        for (CheckableLayout cell : cells) {
            cell.getLayoutParams().height = size;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && cells.size() > 0) {
            int size = (r - l) / 7;
            for (CheckableLayout cell : cells) {
                cell.getLayoutParams().height = size;
            }
        }
    }

    public int getItemLayout() {
        return itemLayout;
    }

    public void setItemLayout(int itemLayout) {
        this.itemLayout = itemLayout;
    }

    public OnItemRender getOnItemRender() {
        return mOnItemRender;
    }

    public void setOnItemRender(OnItemRender mOnItemRender) {
        this.mOnItemRender = mOnItemRender;
    }

    public Calendar getDateDisplay() {
        return dateDisplay;
    }

    public void setDateDisplay(Calendar dateDisplay) {
        this.dateDisplay = dateDisplay;
        for (CheckableLayout cell :
                cells) {
            CardGridItem s = (CardGridItem) cell.getTag();
            if (s != null) {
                Calendar time = s.getDate();
                if (dateDisplay.get(Calendar.YEAR) == time
                        .get(Calendar.YEAR) && dateDisplay.get(Calendar.MONTH) == time.get(Calendar.MONTH) && dateDisplay.get(Calendar.DAY_OF_MONTH) == time
                        .get(Calendar.DAY_OF_MONTH)) {
                    cell.setChecked(true);
                    break;
                }
            }
        }
        cardTitle.setText(new SimpleDateFormat("yyyy年M月", Locale.getDefault()).format(dateDisplay.getTime()));
    }

    public OnCellItemClick getOnCellItemClick() {
        return mOnCellItemClick;
    }

    public void setOnCellItemClick(OnCellItemClick mOnCellItemClick) {
        this.mOnCellItemClick = mOnCellItemClick;
    }

    /**
     * call after change any input data - to refresh view
     */
    public void notifyChanges() {
        updateCells();
    }

    public void notifyRender() {
        for (int i = 0; i < cells.size(); i++) {
            CheckableLayout cell = cells.get(i);
            if (cell.getTag() != null)
                (mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem) cell.getTag());
        }
    }
}
