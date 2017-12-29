package com.fxtx.framework.http;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.fxtx.framework.text.StringUtil;

/**
 * 网络状态监听工具类
 */
public class NetworkUtil {
    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    private static final int NETWORK_TYPE_WIFI = -101;
    /**
     * wifi
     */
    public static final int NETWORK_CLASS_WIFI = -101;
    /**
     * 没有网络
     */
    public static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Unknown network class. 未知网络
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks.
     */
    public static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks.
     */
    public static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks.
     */
    public static final int NETWORK_CLASS_4_G = 3;
    /**
     * 当前网络制式
     */
    public static int netWorkClass = NETWORK_CLASS_UNAVAILABLE;
    /**
     * 当前网络状态，是否有网络
     */
    public static boolean isNetwork = false;//

    /**
     * 调用网络设置界面
     *
     * @param context
     */
    public void startSetNetwork(Context context) {
        Intent intent = null;
        /**
         * 判断手机系统的版本！如果API大于10 就是3.0 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
         */
        if (Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings",
                    "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    /**
     * 获取网络格式
     *
     * @param context
     * @return
     */
    public int getNetworkClass(Context context) {
        int networkType = TelephonyManager.NETWORK_TYPE_UNKNOWN;
        try {
            NetworkInfo network = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                isNetwork = true;
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) context
                            .getSystemService(Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
                isNetwork = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        netWorkClass = getNetworkClassByType(networkType);
        return netWorkClass;
    }

    private int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE; // 无网络
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;// 未知
        }
    }

    /**
     * 获取手机的IMEI识别码
     */
    public String getIMEICode(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMEICode = mTelephonyManager.getDeviceId();
        if (StringUtil.isEmpty(IMEICode)
                || StringUtil.sameStr(IMEICode, "000000000000000")) {
            // 没有获取IMEI
            IMEICode = getMac(context);
        }
        return IMEICode;
    }

    /**
     * 获取手机的Mac地址
     *
     * @param context
     * @return
     */
    public String getMac(Context context) {
        String macAddress = null;
        WifiManager wifiMgr = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        if (macAddress != null) {
            macAddress = macAddress.replaceAll(":|-", "");
        } else {
            macAddress = "0000000000000000";
        }
        return macAddress;
    }

}
