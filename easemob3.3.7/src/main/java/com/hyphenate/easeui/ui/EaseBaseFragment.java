package com.hyphenate.easeui.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.fxtx.framework.ui.FxFragment;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseTitleBar;

/*
* 基础Fragment
* */
public abstract class EaseBaseFragment extends FxFragment {
    protected EaseTitleBar titleBar;
    protected InputMethodManager inputMethodManager;


    protected Toolbar toolbar;
    private TextView titleView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
//        titleBar = (EaseTitleBar) getView().findViewById(R.id.title_bar);

        initView();
        setUpView();
    }


    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    @SuppressLint("ResourceAsColor")
    protected void setfxTtitle(int res) {
        if (titleView != null) {
            titleView.setText(res);
            titleView.setTextColor(R.color.black);

        }
    }

    protected void setfxTtitle(String title) {
        if (titleView != null)
            titleView.setText(title);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = getView(R.id.toolbar);
        titleView = getView(R.id.tool_title);
        if (toolbar != null) {
            toolbar.setTitle("");
        }
    }

    protected void onBackText() {
        if (toolbar != null) {
            TextView tv = getView(R.id.tool_left);
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_back, 0, 0, 0);
            tv.setText(R.string.back_text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishActivity();
                }
            });
        }
    }

    @SuppressLint("ResourceAsColor")
    protected void onRightBtn(int drawableId, int textId) {
        if (toolbar != null) {
            TextView tv = getView(R.id.tool_right);
            tv.setText(textId);
            tv.setTextColor(R.color.black);
            tv.setVisibility(View.VISIBLE);
            tv.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightBtnClick(v);
                }
            });
        }
    }

    protected void onRightBtnClick(View view) {
    }

    protected abstract void initView();

    /**
     * 设置属性，监听等
     */
    protected abstract void setUpView();


}
