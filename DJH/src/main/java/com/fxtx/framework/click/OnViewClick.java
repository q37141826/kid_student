package com.fxtx.framework.click;

import android.view.View;

import java.util.Calendar;

/**
 * Created by z on 2016/3/23.
 */
public class OnViewClick implements View.OnClickListener {
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > DelatTime.MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
        } else {
            return;
        }
    }
}
