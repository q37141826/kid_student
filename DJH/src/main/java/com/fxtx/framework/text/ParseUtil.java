package com.fxtx.framework.text;

/**
 * @version :1
 * @CreateDate 2015年8月3日 上午11:47:14
 * @description :将文字转换解析为数字
 */
public class ParseUtil {
    /**
     * String 转 Float
     *
     * @param parse
     * @return
     */
    public static float parseFloat(String parse) {
        float m = 0;
        try {
            m = Float.parseFloat(parse);
        } catch (Exception e) {
        }
        return m;
    }

    public static double parseDouble(String parse) {
        double m = 0.00;
        try {
            m = Double.parseDouble(parse);
        } catch (Exception e) {
        }
        return m;
    }

    public static int parseInt(String parse) {
        int m = 0;
        try {
            m = Integer.parseInt(parse);
        } catch (Exception e) {
        }
        return m;
    }

    public static long parseLong(String parse) {
        long m = 0;
        try {
            m = Long.parseLong(parse);
        } catch (Exception e) {
        }
        return m;
    }
}
