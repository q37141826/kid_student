package com.fxtx.framework.widgets.scrolltitle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxtx.framework.R;
import com.fxtx.framework.util.BaseUtil;
import com.fxtx.framework.widgets.scrolltitle.its.OnScrollTitleListener;
import com.fxtx.framework.widgets.scrolltitle.its.OnTitleClickListener;
import com.fxtx.framework.widgets.scrolltitle.its.OnTitleView;

import java.util.List;

public class ScrollTitleBar<T extends BeScorllTitle> extends HorizontalScrollView implements
		View.OnClickListener {

	private Context mContext;
	private int itemWidth;// 每项标题的宽度
	private int screenWidth;// 屏幕的宽度
	private int itemCounts;// 设置一屏幕显示的标题个数
	private LinearLayout titleContent;// 用于放置标题的容器
	private ImageView titleBar;// 滑动的bar
	private int length = 0;// 标题个数
	private OnTitleClickListener titleClickListener;// 标题点击回调接口事件
	private View inner;// 子view
	private float x;// 点击时x坐标
	private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画)
	private boolean isCount = false;// 是否开始计算;
	private List<T> title;// 标题

	private OnScrollTitleListener onScroll;

	public ScrollTitleBar(Context context) {
		this(context, null);
	}

	public ImageView getTitleBar() {
		return titleBar;
	}

	public void setTitleBar(ImageView titleBar) {
		this.titleBar = titleBar;
	}

	public ScrollTitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.mContext = context;
	}

	public ScrollTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.scrolltitl);
		itemCounts = ta.getInt(R.styleable.scrolltitl_itemCounts, 3);
		ta.recycle();
		init();
	}

	private void init() {
		this.screenWidth = new BaseUtil().getPhoneWidth(mContext);
		this.itemWidth = (int) ((screenWidth / (float) itemCounts + 0.5f));
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.view_scroll_title, null);
		this.titleContent = (LinearLayout) view.findViewById(R.id.titleContent);
		this.titleBar = (ImageView) view.findViewById(R.id.titleBar);
		this.titleBar.getLayoutParams().width = itemWidth;
		this.addView(view);
	}

	public void removeAllItemView() {
		titleContent.removeAllViews();
	}

	public void setTitleView(List<T> titles,
							 OnScrollTitleListener onScroll ,OnTitleView onTitleView) {
		this.title = titles;
		this.onScroll = onScroll;
		if (titles != null) {
			length = titles.size();
			for (int i = 0; i < length; i++) {
				titleContent.addView(onTitleView.initTitleView(title.get(i),i));
			}
			setTitleBarColor(0, 0);
		}
	}
	public void setTitleView(List<T> titles,
							 OnScrollTitleListener onScroll) {
		setTitleView(titles, onScroll, new OnTitleView<T>() {
			@Override
			public View initTitleView(T t,int index) {
				TextView title = new TextView(mContext);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						itemWidth, LinearLayout.LayoutParams.MATCH_PARENT);
				title.setLayoutParams(params);
				title.setGravity(Gravity.CENTER);
				title.setTextSize(14);
				title.setTextColor(mContext.getResources().getColor(
						R.color.gray));
				title.setText(t.getTitle());
				title.setTag(index);
				title.setOnClickListener(ScrollTitleBar.this);
				return title;
			}
		});
	}

	@Override
	public void onClick(View view) {
		if (titleClickListener != null) {
			titleClickListener.titleOnClickListener((Integer) view.getTag());
		}
	}

	/**
	 * 返回标题栏的标题总个数
	 */
	public int getTitleCounts() {
		return length;
	}

	/**
	 * 返回当前位置的标题目录
	 *
	 * @return
	 */
	public T getTitle(int i) {
		if (i < length) {
			return title.get(i);
		}
		return null;
	}

	/**
	 * 返回每个标题的宽度
	 */
	public int getItemWidth() {
		return itemWidth;
	}

	/**
	 * 修改当前选中的View 的布局样式
	 */
	public void setTitleBarColor(int currentFragmentIndex, int lastFragmentIndex) {
		View lastView = titleContent.getChildAt(lastFragmentIndex);
		View currentView = titleContent.getChildAt(currentFragmentIndex);
		if (onScroll != null) {
			if (lastView != null && currentFragmentIndex != lastFragmentIndex) {
				onScroll.lastViewSelect(lastView);
			}
			if (currentView != null) {
				onScroll.currentViewSelect(currentView);
			}
		}
	}

	/**
	 * 获取指定的标题View对象
	 *
	 * @param index
	 * @return
	 */
	public View getTitleBar(int index) {
		return titleContent.getChildAt(index);
	}

	/**
	 * 设置标题栏点击事件
	 */
	public void setTitleClickListener(OnTitleClickListener listener) {
		this.titleClickListener = listener;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner != null) {
			handlerTouchEvcnt(ev);
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 事件监听处理
	 */
	private void handlerTouchEvcnt(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				if (isNeedAnimation()) {
					animation();
					isCount = false;
				}
				break;

			case MotionEvent.ACTION_MOVE:
				float preX = x;
				float nowX = ev.getX();
				int deltaX = (int) (preX - nowX);// 滑动距离
				if (!isCount) {
					deltaX = 0;
				}
				x = nowX;
				if (isNeedMove()) {
					if (normal.isEmpty()) {
						normal.set(inner.getLeft(), inner.getTop(),
								inner.getRight(), inner.getBottom());
					}
					inner.layout(inner.getLeft() - deltaX / 2, inner.getTop(),
							inner.getRight() - deltaX / 2, inner.getBottom());
					// 当前Title 样式修改
				}
				isCount = true;
				break;
		}
	}

	/**
	 * 回缩动画
	 */
	private void animation() {
		// 开启移动动画
		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
				normal.top);
		ta.setDuration(1000);
		inner.startAnimation(ta);
		// 设置回到正常的布局位置
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);
		normal.setEmpty();
	}

	/**
	 * 是否需要开启动画
	 */
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	/**
	 * 是否需要移动布局
	 */
	public boolean isNeedMove() {
		int offset = inner.getMeasuredWidth() - getWidth();
		int scrollX = getScrollX();
		if (scrollX == 0 || scrollX == offset) {
			return true;
		}
		return false;
	}
}
