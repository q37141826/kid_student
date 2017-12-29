package com.fxtx.framework.platforms.jpush;

import android.content.Context;

import com.fxtx.framework.text.StringUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author djh-zy
 * @ClassName: JpushUtil.java
 * @Date 2015年4月20日 下午4:10:26
 * @Description: 极光推送 使用工具类
 */
public class JpushUtil {
    private static TagAliasCallback callback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> strings) {
        }
    };

    /**
     * 初始化极光推送基本信息
     *
     * @param context
     */
    public static void infoJpush(Context context) {
        JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(context); // 初始化 JPush
        resumeJpush(context);
    }

    public static void resumeJpush(Context context) {
        if (JPushInterface.isPushStopped(context)) {
            JPushInterface.resumePush(context); // 重启服务
        }
    }

    /**
     * 注册极光alias推送工具
     *
     * @param context
     * @param id
     */
    public static void statJpushAlias(Context context, String id) {
        if (!StringUtil.isEmpty(id)) {
            JPushInterface.setAlias(context.getApplicationContext(), id, callback);
        }
    }

//    /**
//     * 注册极光推送设备标签实现分组推送服务
//     *
//     * @param cntext
//     */
//    public static void startJpush(Context cntext) {
//        Set<String> typeSet = new HashSet<String>();
//        typeSet.add("Android");//
//        JPushInterface.setTags(cntext, typeSet, callback);
//    }

    /**
     * 停止Alias
     */
    public static void stopJpushAlias(Context context) {
        JPushInterface.setAlias(context.getApplicationContext(), "", callback);
    }

    /**
     * 停止极光推送功能
     */
    public static void stopJpush(Context context) {
        JPushInterface.stopPush(context.getApplicationContext());
    }

    /**
     * @param context
     */
    public static void onResume(Context context) {
        JPushInterface.onResume(context);
    }

    /**
     * @param context
     */
    public static void onPause(Context context) {
        JPushInterface.onPause(context);
    }

    /**
     * 清除推送通知
     *
     * @param context
     */
    public static void clearAllNotifications(Context context) {
        JPushInterface.clearAllNotifications(context.getApplicationContext());
    }
    /**
     * 清除指定的推送通知
     *
     * @param context
     */
    public static void clearNotificationsById(Context context, int id) {
        JPushInterface.clearNotificationById(context.getApplicationContext(), id);
    }
}
