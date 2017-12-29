package cn.dajiahui.kid.ui.homework.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author wdj
 * @date 2015-6-1
 * @time 下午2:23:35
 * @title NumTextView 自定义数字字体
 */
public class NumTextView extends TextView {

	public NumTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setTextTypeFace(context);
	}

	public NumTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTextTypeFace(context);
	}

	public NumTextView(Context context) {
		super(context);
		setTextTypeFace(context);
	}

	private void setTextTypeFace(Context context) {
		// 将字体文件保存在assets/fonts/目录下，创建Typeface对象
		Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/Candara.ttf");
		this.setTypeface(typeFace);
	}
}
