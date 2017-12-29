package com.fxtx.framework.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CalendarCardPager extends ViewPager {

    private CardPagerAdapter mCardPagerAdapter;
    private OnCellItemClick mOnCellItemClick;
    private OnItemRender onItemReander;


    public CalendarCardPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init(context);
    }

    public CalendarCardPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarCardPager(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mCardPagerAdapter = new CardPagerAdapter(context);
        setAdapter(mCardPagerAdapter);
        this.setCurrentItem(mCardPagerAdapter.INDEX);
    }

    public void currentItem(boolean gTo) {
        int index = this.getCurrentItem();
        if (gTo) {
            //左侧滑动
            if (index > 0)
                this.setCurrentItem(index - 1, true);
        } else {
            //右侧滑动
            if (index < mCardPagerAdapter.MAX_VALUE - 1) {
                this.setCurrentItem(index + 1, true);
            }
        }
    }

    public CardPagerAdapter getCardPagerAdapter() {
        return mCardPagerAdapter;
    }

    public OnCellItemClick getOnCellItemClick() {
        return mOnCellItemClick;
    }

    public void setOnCellItemClick(OnCellItemClick mOnCellItemClick) {
        this.mOnCellItemClick = mOnCellItemClick;
        mCardPagerAdapter.setDefaultOnCellItemClick(this.mOnCellItemClick);
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v instanceof CalendarCard) {
                    ((CalendarCard) v).setOnCellItemClick(this.mOnCellItemClick);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
//            if (h > height)
                height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnItemReander(OnItemRender onItemReander) {
        this.onItemReander = onItemReander;
        mCardPagerAdapter.setOnItemRender(onItemReander);
        mCardPagerAdapter.notifyDataSetChanged();
    }

    //刷新当前的布局
    public void refreshItem() {
        CalendarCard card = (CalendarCard) this.findViewWithTag(getCurrentItem());
        if (card != null)
            card.notifyRender();
    }
}
