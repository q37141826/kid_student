package com.fxtx.framework.platforms.umeng;

import android.content.Context;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: UmengUtil.java
 * @author djh-zy
 * @Date 2015年4月20日 下午4:42:59
 * @Description: 友盟统计工具类
 */
public class UmengUtil {
	/**
	 * 设置友盟统计信息发送策略和日志加密规则
	 *
	 * @param context
	 *
	 */
	public static void setUpdateOnlineConfig(Context context, boolean isEnable) {
		MobclickAgent.updateOnlineConfig(context);
		AnalyticsConfig.enableEncrypt(isEnable); // 设置日志加密
	}

	/**
	 * 程序调用Kill杀死系统时进行数据统计功能
	 */
	public static void killProcess(Context context) {
		MobclickAgent.onKillProcess(context);
	}

	/**
	 *
	 * @param context
	 */
	public static void onResume(Context context) {
		MobclickAgent.onResume(context);
	}

	/**
	 *
	 * @param context
	 */
	public static void onPause(Context context) {
		MobclickAgent.onPause(context);
	}
}
