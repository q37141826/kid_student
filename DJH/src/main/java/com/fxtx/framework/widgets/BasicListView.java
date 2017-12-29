package com.fxtx.framework.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class BasicListView extends ListView {

	private boolean haveScrollbar = true;

	/**
	 * @param context
	 */
	public BasicListView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BasicListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BasicListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
	}

	/**
	 * 设置是否有ScrollBar，当要在ScollView中显示时，应当设置为false。 默认为 true
	 *
	 * @param haveScrollbar
	 */
	public void setHaveScrollbar(boolean haveScrollbar) {
		this.haveScrollbar = haveScrollbar;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!haveScrollbar) {
			int expandSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
