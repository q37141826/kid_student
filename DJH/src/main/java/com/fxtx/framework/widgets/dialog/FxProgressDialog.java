package com.fxtx.framework.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.fxtx.framework.R;


/**
 * Created by z on 2015/12/3.
 */
public class FxProgressDialog extends Dialog {
    private View contextView;
    private View progressImage; // 进度条图片
    private TextView tv_msg;

    public FxProgressDialog(Context context) {
        this(context, R.style.transparentDialog);
    }

    public void initWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }

    public FxProgressDialog(Context context, int theme) {
        super(context, theme);
        // 创建布局 和数据
        initWindow();
        contextView = LayoutInflater.from(context).inflate(
                R.layout.dialog_progress, null);
        this.setContentView(contextView);// 布局设置
        tv_msg = (TextView) contextView.findViewById(R.id.tv_msg);
        progressImage = this.findViewById(R.id.dialog_iv_progress);

    }

    public void setTextMsg(String msg) {
        tv_msg.setText(msg);
    }
    public void setTextMsg(int resid){
        tv_msg.setText(resid);
    }

    @Override
    public void show() {
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                this.getContext(), R.anim.dialog_progress_anim);
        // 使用ImageView显示动画
        progressImage.startAnimation(hyperspaceJumpAnimation);
        super.show();
    }
}
