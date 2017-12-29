package com.fxtx.framework.platforms.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fxtx.framework.json.HeadJson;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public abstract class JpushReceiver extends BroadcastReceiver {

    protected Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        this.context = context;

        int notificationID = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d("majin","JpushReceiver:收到消息");
            //收到推送消息
            sendToActivity(getMessageId(bundle), notificationID);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            //点击推送消息
            openActiviy(getMessageId(bundle));
        }
        if (isBundle()) {
           printBundle(bundle);

        }
    }

    /**
     * 重构 返回true 就会打印推送信息
     *
     * @return
     */
    protected boolean isBundle() {
        return false;
    }

    private HeadJson getMessageId(Bundle bundle) {
        HeadJson json = new HeadJson(bundle.getString(JPushInterface.EXTRA_EXTRA));
        return json;
    }

    protected abstract void sendToActivity(HeadJson json, int notificationID);
    protected abstract void openActiviy(HeadJson json);

    // 打印所有的 intent extra 数据
    protected String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
