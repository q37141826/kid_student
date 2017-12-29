package com.fxtx.framework.click;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

/**
 * Created by z on 2016/3/23.
 */
public abstract class OnItemClick implements AdapterView.OnItemClickListener {
    private long lastClickTime = 0;


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        Log.e("curr", currentTime + "");
        if (currentTime - lastClickTime > DelatTime.MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
        } else {
            return;
        }
        Log.e("ssss", currentTime - lastClickTime + "");
    }
}
