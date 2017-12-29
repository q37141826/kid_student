package com.fxtx.framework.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity管理工具类
 *
 * @author Administrator
 */
public class ActivityUtil {

    private Stack<Activity> activityStack;
    private static ActivityUtil activityUtil;

    private ActivityUtil() {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
    }

    /**
     * 单一实例
     */
    public static ActivityUtil getInstance() {
        if (activityUtil == null) {
            synchronized (ActivityUtil.class) {
                if (activityUtil == null) {
                    activityUtil = new ActivityUtil();
                }
            }
        }
        return activityUtil;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }


    /**
     * 结束指定的Activity
     */
    public void finishThisActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        finishThisActivity(getActivity(cls));
    }

    /**
     * 获取指定activity
     *
     * @param cls
     * @return
     */
    public Activity getActivity(Class<?> cls) {
        Activity finishActivity = null;
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().getName().equals(cls.getName())) {
                    finishActivity = activity;
                }
            }
        }
        return finishActivity;
    }

    /**
     * 关闭所有的App
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

}
