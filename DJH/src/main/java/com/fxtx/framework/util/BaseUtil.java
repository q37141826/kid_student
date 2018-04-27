package com.fxtx.framework.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fxtx.framework.text.StringUtil;


/**
 * @author djh-zy
 * @version :1
 * @CreateDate 2015年8月3日 上午11:49:47
 * @description :
 */

@SuppressWarnings("ALL")
public class BaseUtil {


    /**
     * 收起键盘
     */
    public static void hideSoftInput(Activity context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView()
                    .getWindowToken(), 0);
        }
    }

    /**
     * 弹出键盘
     *
     * @param editText 输入框
     */
    public static void showSoftInput(EditText editText) {
        editText.requestFocus();
        InputMethodManager manager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }


    public static float limitValue(float a, float b) {
        float valve = 0;
        final float min = Math.min(a, b);
        final float max = Math.max(a, b);
        valve = valve > min ? valve : min;
        valve = valve < max ? valve : max;
        return valve;
    }

    /**
     * 获取软件版本名称
     *
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "v 1.0.0";

        try {
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取软件版本号
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNum
     */
    public static void callPhone(Context context, String phoneNum) {
        if (!StringUtil.isEmpty(phoneNum)) {
            Uri uri = Uri.parse("tel:" + phoneNum);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(intent);
        }
    }

    /**
     * 判断sd卡是否挂载
     */
    public static boolean isSDCardMount() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    //  dp  转  px(像素)
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /*px转dp*/
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取手机宽度
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getPhoneWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取手机高度
     *
     * @param context
     */
    @SuppressWarnings("deprecation")
    public static int getPhoneHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();

    }

    /**
     * 获取屏幕宽度
     */
    public static int getWidthPixels(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getHeightPixels(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 屏幕密度（0.75 / 1.0 / 1.5） dp缩放因子
     */
    public static float getScreenDensity(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.density;
    }

    /**
     * 屏幕密度DPI（120 / 160 / 240）  广义密度
     */
    public static int getScreenDensityDpi(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.densityDpi;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getDaoHangHeight(Context context) {
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 获取屏幕尺寸大小(英寸)
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public static double getScreenInch(Activity context) {

        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        int densityDpi = metric.densityDpi;// 屏幕密度DPI（120 / 160 / 240）
        double diagonalPixels = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        double screenInches = diagonalPixels / densityDpi;

        return screenInches;
    }

    public static void clipboard(Context context, String text) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text);
    }

    /*计算距离(适配宽)
    * @param context
    * @param  controlItselfwidth  控件本身大小  单位px
    * @param  controlnumber      控件数量
    * @param    rightandleft      控件位于左右边的距离  px

    * */
    public static int ComputationalDistanceWidth(Activity activity, int controlItselfwidth, int controlnumber, int rightandleft) {

        int Rweight = 0;
        if (controlnumber >= 1) {
            if (controlnumber == 1) {
                Rweight = 0 + rightandleft;
            } else {

                int widthPixels = (getWidthPixels(activity) - (rightandleft * 2)) / controlItselfwidth;
                int i = widthPixels * controlItselfwidth;
                int i1 = getWidthPixels(activity) - i;
                float i5 = (float) (1.0 / ((float) widthPixels - 1));
                int i2 = (int) ((float) i1 * i5);
                Rweight = (i2 * (controlnumber - 1)) + (controlItselfwidth * (controlnumber - 1));
                if (controlnumber == widthPixels) {
                    Rweight = (i2 * (controlnumber - 1)) + (controlItselfwidth * (controlnumber - 1) - rightandleft);
                }
            }
        } else {
//            Toast.makeText(activity, "请输入控件个数", Toast.LENGTH_SHORT).show();

        }
        return Rweight;
    }

    /*计算距离(适配高)
        * @param context
        * @param  controlItselfheight  控件本身大小  单位px
        * @param  controlnumber      控件数量

        * */
    public static int ComputationalDistanceHeight(Activity activity, int controlItselfheight, int controlnumber) {
        int RHeight = 0;
        //状态栏高度
        int statusBarHeight = getStatusBarHeight(activity);
        int heightPixels = getHeightPixels(activity);
        //竖着能装多少个控件
        int i = (heightPixels - statusBarHeight) / controlItselfheight;

        if (controlnumber == 1) {
            RHeight = statusBarHeight + controlItselfheight;
        } else {
            int i1 = i * controlItselfheight;

            RHeight = statusBarHeight + controlnumber * controlItselfheight;
        }
        return RHeight;
    }


    /*判断是否显示导航栏*/
    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }
}
