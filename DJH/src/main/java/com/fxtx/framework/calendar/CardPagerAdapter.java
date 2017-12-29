package com.fxtx.framework.calendar;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.Calendar;

public class CardPagerAdapter extends PagerAdapter {
    public int MAX_VALUE = 120;//十年内循环  最多显示左边
    public int INDEX = 60;
    private Context mContext;
    private OnCellItemClick defaultOnCellItemClick;
    private OnItemRender onItemRender;

    public void setOnItemRender(OnItemRender onItemRender) {
        this.onItemRender = onItemRender;
    }

    public CardPagerAdapter(Context ctx) {
        mContext = ctx;
    }

    @Override
    public Object instantiateItem(View collection, final int position) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, position % MAX_VALUE - INDEX);
        CalendarCard card = new CalendarCard(mContext);
        card.setDateDisplay(cal);
        card.setOnItemRender(onItemRender);
        card.notifyChanges();
        if (card.getOnCellItemClick() == null)
            card.setOnCellItemClick(defaultOnCellItemClick);
        card.setTag(position);
        ((ViewPager) collection).addView(card, 0);
        return card;
    }


    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public int getCount() {
        return MAX_VALUE;
    }

    public OnCellItemClick getDefaultOnCellItemClick() {
        return defaultOnCellItemClick;
    }

    public void setDefaultOnCellItemClick(OnCellItemClick defaultOnCellItemClick) {
        this.defaultOnCellItemClick = defaultOnCellItemClick;
    }


}
