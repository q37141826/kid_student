package com.fxtx.framework.widgets.viewpage;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @ClassName: CustomDurationScroller.java
 * @author djh-zy
 * @Date 2015年5月14日 下午1:35:05
 * @Description: TODO
 */
public class CustomDurationScroller extends Scroller {

	private double scrollFactor = 1;

	public CustomDurationScroller(Context context) {
		super(context);
	}

	public CustomDurationScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	/**
	 * Set the factor by which the duration will change
	 */
	public void setScrollDurationFactor(double scrollFactor) {
		this.scrollFactor = scrollFactor;
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy,
				(int) (duration * scrollFactor));
	}
}
