package com.fxtx.framework.time;

import android.text.TextUtils;

import com.fxtx.framework.text.ParseUtil;
import com.fxtx.framework.text.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author djh
 * @ClassName: TimeUtil.java
 * @Date 2015年4月27日 下午2:22:20
 * @Description: 时间格式工具类
 */
public class TimeUtil {
    public static String YYYYMD = "yyyy-M-d";
    public static String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static String yyMD = "yyyy-MM-dd";
    public static String HHmm = "HH:mm";
    public static String MMDDHHSS = "MM-dd HH:mm";
    public static final String[] month = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

    /**
     * 获取毫秒的long类型time
     *
     * @param server_time
     * @return
     */
    public static long getTimeMillis(String server_time) {
        if (TextUtils.isEmpty(server_time)) {
            return 0;
        }
        long time = 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(server_time);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    public static String getRecordingTimeFromMillis(int seconds) {
        return String.format("%02d:%02d", seconds / 60 % 60, seconds % 60);
    }

    /**
     * 获得当天0点时间
     *
     * @return
     */
    public static long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获得昨天0点时间
     *
     * @return
     */
    public static long getTimesYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.DAY_OF_MONTH - 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //毫秒转标准格式
    public static String timeFormat(String server_time, String format) {
        long serverTime = new ParseUtil().parseLong(server_time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(serverTime));
    }

    public static String timeDateFormat(String server_time) {
        long serverTime = new ParseUtil().parseLong(server_time);
        Calendar currCalendar = Calendar.getInstance();
        currCalendar.setTimeInMillis(serverTime);
        return String.format("%s\n%s月", currCalendar.get(Calendar.DAY_OF_MONTH), month[currCalendar.get(Calendar.MONTH)]);
    }


    /**
     * 时间格式序列化
     *
     * @param server_time
     * @return: 是否是数字
     */
    public static String TimeFormat(String server_time, boolean islong) {
        String strtime = "";
        long MINUTE = 60000;
        Calendar currCalendar = Calendar.getInstance();
        long serverTime;
        if (islong) {
            serverTime = new ParseUtil().parseLong(server_time);
        } else {
            serverTime = getTimeMillis(server_time);
        }
        long currTime = System.currentTimeMillis();
        currCalendar.setTimeInMillis(currTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        /**
         * 加一个10秒钟的容错处理
         */
        long diffTime = currTime - serverTime + 10000;

        if (diffTime < 0) {
            strtime = sdf.format(new Date(serverTime));
            if (strtime.contains("年0"))
                strtime = strtime.replace("年0", "年");
            if (strtime.contains("月0"))
                strtime = strtime.replace("月0", "月");
            if (strtime.contains("日 0"))
                strtime = strtime.replace("日 0", "日 ");
        } else if (diffTime > 0 && diffTime < MINUTE) {
            strtime = "现在";
        } else if (diffTime >= MINUTE && diffTime < MINUTE * 60) {
            sdf.applyPattern("mm分钟前");
            strtime = sdf.format(new Date(diffTime));
            if (strtime.startsWith("0"))
                strtime = strtime.substring(1);
        } else if (diffTime >= MINUTE * 60 && diffTime < MINUTE * 60 * 24
                && serverTime >= getTimesmorning()) {
            sdf.applyPattern("今天 HH:mm");
            strtime = sdf.format(new Date(serverTime));
            if (strtime.contains("今天 0"))
                strtime = strtime.replace("今天 0", "今天 ");
        } else if (diffTime >= MINUTE * 60 && diffTime < MINUTE * 60 * 24 * 2
                && serverTime < getTimesmorning()
                && serverTime >= getTimesYesterday()) {
            sdf.applyPattern("昨天 HH:mm");
            strtime = sdf.format(new Date(serverTime));
            if (strtime.contains("昨天 0"))
                strtime = strtime.replace("昨天 0", "昨天 ");
        } else {
            SimpleDateFormat year = new SimpleDateFormat("yyyy");
            // 如果服务器的年份与本地年份相同 则仅显示月日
            if (year.format(new Date(getTimeMillis(server_time))).equals(
                    year.format(new Date(currTime)))) {
                sdf.applyPattern("MM月dd日 HH:mm");
                strtime = sdf.format(new Date(serverTime));
                if (strtime.startsWith("0"))
                    strtime = strtime.substring(1);
                if (strtime.contains("月0"))
                    strtime = strtime.replace("月0", "月");
                if (strtime.contains("日 0"))
                    strtime = strtime.replace("日 0", "日 ");
            } else {
                sdf.applyPattern("yyyy年MM月dd日 HH:mm");
                strtime = sdf.format(new Date(serverTime));
                if (strtime.contains("年0"))
                    strtime = strtime.replace("年0", "年");
                if (strtime.contains("月0"))
                    strtime = strtime.replace("月0", "月");
                if (strtime.contains("日 0"))
                    strtime = strtime.replace("日 0", "日 ");
            }
        }
        return strtime;
    }

    public static String timePeriod(String start, String end) {
        long startlong = new ParseUtil().parseLong(start);
        long endLong = new ParseUtil().parseLong(end);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyLocalizedPattern("M月d日 H:mm");
        String text = sdf.format(startlong);
        sdf.applyLocalizedPattern("H:mm");
        text = text + "~" + sdf.format(endLong);
        return text;
    }


    //判断两个时间是否相等
    public static boolean sameTime(String time1, long time2, String forment) {
        boolean isSize = false;
        SimpleDateFormat sdf = new SimpleDateFormat(forment);
        String t1 = sdf.format(new ParseUtil().parseLong(time1));
        String t2 = sdf.format(time2);
        isSize = StringUtil.sameStr(t1, t2);
        return isSize;
    }

    public static String getWeekData(Calendar calendar) {
        String str = "";
        Calendar one = dayOfMonday(calendar.getTime());
        Calendar two = dayOfSunday(calendar.getTime());
        int Month1 = one.get(Calendar.MONTH);
        int Month2 = two.get(Calendar.MONTH);
        if (Month1 == Month2) {
            str = (Month1 + 1) + "月" + one.get(Calendar.DAY_OF_MONTH) + "日-" + two.get(Calendar.DAY_OF_MONTH) + "日";
        } else {
            str = (Month1 + 1) + "月" + one.get(Calendar.DAY_OF_MONTH) + "日-" + (Month2 + 1) + "月" + two.get(Calendar.DAY_OF_MONTH) + "日";
        }
        return str;
    }

    /**
     * 获得指定日期所在周周一的 date对象
     * 一周为 周一到周日
     *
     * @param date
     * @return
     */
    public static Calendar dayOfMonday(Date date) {
        int dayOfWeek = dayOfWeek(date);
        Calendar outDate;
        if (dayOfWeek == 1) {
            outDate = dateGapDays(date, -6);
        } else {
            outDate = dateGapDays(date, -(dayOfWeek - 2));
        }
        return outDate;
    }

    /**
     * 获得指定日期所在周周日的 date对象
     * 一周为 周一到周日
     *
     * @param date
     * @return
     */
    public static Calendar dayOfSunday(Date date) {
        int dayOfWeek = dayOfWeek(date);
        Calendar outDate;
        if (dayOfWeek == 1) {
            outDate = dateGapDays(date, 0);
        } else {
            outDate = dateGapDays(date, 6 - (dayOfWeek - 2));
        }
        return outDate;
    }

    /**
     * 获得指定日期的前一天或者后一天（由传入的num决定）的 date 对象 后几天的日期
     *
     * @param date
     * @param num：0表示当前天 -1 表示上一天，1表示后一天，其他以此类推
     * @return
     */
    public static Calendar dateGapDays(Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        return cal;
    }

    /**
     * 获取传入日期是星期几
     *
     * @param date
     * @return 范围 1~7,1=星期日 7=星期六，其他类推
     */
    public static Integer dayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 范围 1~7,1=星期日 7=星期六，其他类推
        return c.get(Calendar.DAY_OF_WEEK);
    }
    public static long dateToLong (String in) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(in);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.getTime().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 时间戳转时间字符串
     *
     * @param stamp
     * @return
     */
    public static String stampToString(String stamp, String YYYYMD) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYYMD);
        long lt = new Long(stamp);
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }
}
