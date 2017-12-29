package com.fxtx.framework.widgets.scrolltitle.its;

import android.view.View;

/**
 * @ClassName: OnScrollTitleSelectInterface.java
 * @author djh-zy
 * @Date 2015年5月6日 下午5:25:58
 * @Description: scrollTitle 选中时 布局样式
 */
public interface OnScrollTitleListener {
	void lastViewSelect(View view);

	void currentViewSelect(View view);
}
