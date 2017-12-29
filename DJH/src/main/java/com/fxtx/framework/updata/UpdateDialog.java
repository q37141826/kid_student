package com.fxtx.framework.updata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.fxtx.framework.R;
import com.fxtx.framework.widgets.dialog.FxDialog;


/**
 * 版本更新dialog
 *
 * @author Administrator
 *
 */
public abstract class UpdateDialog extends FxDialog {
	private ProgressBar progressBar;

	public UpdateDialog(Context context) {
		super(context);
		// 创建布局 和数据
		View contextView = LayoutInflater.from(context).inflate(
				R.layout.dialog_update, null);
		progressBar = (ProgressBar) contextView.findViewById(R.id.dialog_pb);
		this.addView(contextView);
		this.setCancelable(false);// 不能使用返回键
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}


}
