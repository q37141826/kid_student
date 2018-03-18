package com.fxtx.framework.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxtx.framework.R;
import com.fxtx.framework.util.BaseUtil;


/**
 * @author djh-zy
 * @version :1
 * @CreateDate 2015年8月11日 下午1:14:18
 * @description :
 */
public abstract class RecordingDialog extends Dialog implements
        View.OnClickListener {
    protected View contextView;
    protected LinearLayout contentPanel;// 居中布局对象
    private Button btnOne, btnTwo;// 两个按钮
    private View line;// 线
    private TextView tvTitle, tvMessage;//
    private int flag;
    private Object obj;

    public RecordingDialog(Context context) {
        this(context, R.layout.recording_dialog);
    }

    public RecordingDialog(Context context, int layoutid) {
        super(context, R.style.transparentDialog);
        initWindow();
        initView(layoutid);
    }

    public void initWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.height = LayoutParams.WRAP_CONTENT;
        wl.width = (int) (new BaseUtil().getPhoneWidth(getContext()) * 0.9f);
        wl.gravity = Gravity.CENTER;
        wl.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        window.setAttributes(wl);
        this.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域不关闭Dialog
    }

    /**
     * 设置窗体动画
     *
     * @param resId
     */
    public void setAnimation(int resId) {
        getWindow().setWindowAnimations(resId);
    }

    private void initView(int layout) {
        this.setCanceledOnTouchOutside(true);
        contextView = LayoutInflater.from(this.getContext()).inflate(
                layout, null);
        contentPanel = (LinearLayout) contextView
                .findViewById(R.id.contentPanel);
        btnOne = (Button) contextView.findViewById(R.id.dialog_sure);

        line = contextView.findViewById(R.id.dialog_line);
        tvTitle = (TextView) contextView.findViewById(R.id.title);
        tvMessage = (TextView) contextView.findViewById(R.id.message);
        btnOne.setOnClickListener(this);

        setContentView(contextView);
    }

    public void setLeftBtnHide(int visibi) {
        btnOne.setVisibility(visibi);
        line.setVisibility(visibi);
    }
    public void setRightAndLeftBtnColor(int color){
        btnOne.setTextColor(color);
        btnTwo.setTextColor(color);
    }
    public void setRightBtnHide(int visibi) {
        btnTwo.setVisibility(visibi);
        line.setVisibility(visibi);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btnOne.getLayoutParams();
        if (params != null) {
            params.rightMargin = 0;
            btnOne.setLayoutParams(params);
        }
    }

    // 给消息中添加
    public void addView(View view) {
        contentPanel.addView(view);
    }

    public LinearLayout getPanelView() {
        return contentPanel;
    }

    public Button getLeftButton() {
        return btnOne;
    }

    public Button getRightButton() {
        return btnTwo;
    }

    public TextView getTitle() {
        return tvTitle;
    }

    public TextView getMessage() {
        return tvMessage;
    }

    /**
     * 标题
     */
    public void setTitle(String text) {
        tvTitle.setText(text);
    }

    public void setTitle(int resid) {
        tvTitle.setText(resid);
    }

    /**
     * 内容
     */
    public void setMessage(String text) {
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(text);
    }

    public void setMessage(int resid) {
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(resid);
    }

    public void setMessageHide(int visible) {
        tvMessage.setVisibility(visible);
    }

    public void setRightBtnText(String text) {
        btnTwo.setText(text);
    }

    public void setRightBtnText(int redid) {
        btnTwo.setText(redid);
    }

    public void setLeftBtnText(int redid) {
        btnOne.setText(redid);
    }

    public void setLeftBtnText(String text) {
        btnOne.setText(text);

    }

    @Override
    public void onClick(View v) {
        if (isOnClickDismiss())
            this.dismiss();
        if (v.getId() == R.id.dialog_sure) {
            // 点击
            onSureBtn(flag);
        }

//        else if (v.getId() == R.id.dialog_two) {
//            // 点击
//            onRightBtn(flag);
//        }
    }

    public boolean isOnClickDismiss() {
        return true;
    }

    public void setFloag(int floag) {
        this.flag = floag;
    }

//    public abstract void onRightBtn(int flag);

    public abstract void onSureBtn(int flag);

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
