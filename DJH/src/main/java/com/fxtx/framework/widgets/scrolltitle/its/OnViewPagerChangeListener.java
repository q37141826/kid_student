package com.fxtx.framework.widgets.scrolltitle.its;

import android.support.v4.view.ViewPager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.fxtx.framework.widgets.scrolltitle.ScrollTitleBar;

/**
 * 滑动状态接口
 *
 * @author Administrator
 *
 */
public class OnViewPagerChangeListener extends OnBasePageChangeListener {

	private ScrollTitleBar mScrollTitleBar;
	private ImageView mBar;
	private boolean isStatus = false;

	public OnViewPagerChangeListener(ViewPager pager, int itemWidth,
									 ScrollTitleBar mScrollTitleBar, ImageView bar) {
		super(pager, itemWidth);
		this.mScrollTitleBar = mScrollTitleBar;
		this.mBar = bar;
	}

	@Override
	public void moveNextFalse() {
		mBar.clearAnimation();
		Animation animation = new TranslateAnimation(endPosition,
				beginPosition, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(2);
		mBar.startAnimation(animation);
		mScrollTitleBar.invalidate();
		isStatus = false;
	}

	@Override
	public void moveing() {
		Animation mAnimation = new TranslateAnimation(beginPosition,
				endPosition, 0, 0);
		mAnimation.setFillAfter(true);
		mAnimation.setDuration(0);
		mBar.startAnimation(mAnimation);
		mScrollTitleBar.invalidate();
		isStatus = true;
	}

	@Override
	public void moveNextTrue() {
		focusedFragment(currentFragmentIndex, lastFragmentIndex);
		Animation animation;
		if (isStatus) {
			animation = new TranslateAnimation(endPosition, beginPosition, 0, 0);
			isStatus = false;
		} else {
			animation = new TranslateAnimation(itemWidth * lastFragmentIndex,
					beginPosition, 0, 0);
		}
		animation.setFillAfter(true);
		animation.setDuration(0);
		mBar.startAnimation(animation);
		mScrollTitleBar.smoothScrollTo((currentFragmentIndex - 1) * itemWidth,
				0);
		mScrollTitleBar.setTitleBarColor(currentFragmentIndex,
				lastFragmentIndex);
	}

	public void focusedFragment(int selectPosition, int lastPosition) {
	}
}
