package cn.dajiahui.kid.ui.study.kinds.practice.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fxtx.framework.R;

/**
 * 退出弹框
 */
public abstract class SignOutDialog extends Dialog {
    protected Context mContext;
    protected View rootView;

    public SignOutDialog(Context c, int resId) {
        super(c, R.style.transparentDialog);
        this.mContext = c;
        initPop(resId);
    }

    /**
     * 初始化
     */
    private void initPop(int resId) {
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();

        window.setWindowAnimations(R.style.pop_anim_style);
        rootView = LayoutInflater.from(mContext).inflate(resId, null);
        initView();
        this.setContentView(rootView);
    }

    public abstract void initView();
}
