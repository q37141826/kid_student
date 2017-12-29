package com.fxtx.framework.widgets.scrolltitle.its;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * 滑动接口抽象类
 * @author Administrator
 *
 */
public abstract class OnBasePageChangeListener implements OnPageChangeListener {

	public ViewPager pager;
	public int itemWidth;
	public boolean isEnd;
	public int endPosition;
	public int beginPosition;
	public int lastFragmentIndex;
	public int currentFragmentIndex;

	public OnBasePageChangeListener(ViewPager pager,int itemWidth) {
		this.itemWidth = itemWidth;
		this.pager = pager;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_DRAGGING){
			isEnd = false;
		}else if(state == ViewPager.SCROLL_STATE_SETTLING){
			isEnd = true;
			beginPosition = currentFragmentIndex * itemWidth;
			if(pager.getCurrentItem() == currentFragmentIndex){
				moveNextFalse();
				endPosition = currentFragmentIndex * itemWidth;
			}
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
		if(!isEnd){
			if(currentFragmentIndex == position){
				endPosition = itemWidth * currentFragmentIndex + (int)(itemWidth * positionOffset);
			}
			if(currentFragmentIndex == position+1){
				endPosition = itemWidth * currentFragmentIndex - (int)(itemWidth * (1-positionOffset));
			}
			if(currentFragmentIndex == position-1){
				endPosition = itemWidth * currentFragmentIndex + (int)(itemWidth * (1-positionOffset));
			}
			moveing();
			beginPosition = endPosition;
		}
	}

	@Override
	public void onPageSelected(int position) {
		lastFragmentIndex = currentFragmentIndex;
		currentFragmentIndex = position;
		beginPosition = position * itemWidth;
		moveNextTrue();
	}

	public abstract void moveNextFalse();//未进入下一个页面
	public abstract void moveing();//滑动中
	public abstract void moveNextTrue();//进入下一个页面
}
