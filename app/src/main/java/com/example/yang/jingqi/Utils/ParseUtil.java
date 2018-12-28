package com.example.yang.jingqi.Utils;

import android.graphics.Color;
import android.text.Html;

/**
 * @author zg
 * @version 2.0
 * @time 16/8/11
 * @introduction 强转类型
 */
public class ParseUtil {
    private final static int DEFAULT_COLOR = Color.BLACK; // 默认颜色值
    private final static int DEFAULT_INT = 0;
    private final static long DEFAULT_LONG = 0;
    private final static float DEFAULT_FLOAT = 0f;
    private final static double DEFAULT_DOUBLE = 0;

    /**
     * String to color
     *
     * @param color
     * @return
     */
    public static int parseColor(String color) {
        return parseColor(color, DEFAULT_COLOR);
    }

    public static int parseColor(String color, int defaultcolor) {
        if (StringUtils.isNull(color)) {
            return defaultcolor;
        }
        int value;
        try {
            value = Color.parseColor(color);
        } catch (Exception e) {
            value = defaultcolor;
        }
        return value;
    }

    /**
     * String to int
     *
     * @param s
     * @return
     */
    public static int parseInt(String s) {
        return parseInt(s, DEFAULT_INT);
    }

    public static int parseInt(String s, int defaultInt) {
        if (StringUtils.isNull(s)) {
            return defaultInt;
        }
        int value;
        try {
            value = Integer.parseInt(s);
        } catch (Exception e) {
            value = defaultInt;
        }
        return value;
    }

    /**
     * string to long
     * @param s
     * @return
     */
    public static long parseLong(String s) {
        return parseLong(s, DEFAULT_LONG);
    }

    public static long parseLong(String s, long defultLong) {
        if (StringUtils.isNull(s)) {
            return defultLong;
        }
        long value;
        try {
            value = Long.parseLong(s);
        } catch (Exception e) {
            value = defultLong;
        }
        return value;
    }

    /**
     * String to float
     *
     * @param s
     * @return float
     */
    public static float parseFloat(String s) {
        return parseFloat(s, DEFAULT_FLOAT);
    }

    public static float parseFloat(String s, float defaultFloat) {
        if (StringUtils.isNull(s)) {
            return defaultFloat;
        }
        float value;
        try {
            value = Float.parseFloat(s);
        } catch (Exception e) {
            value = defaultFloat;
        }
        return value;
    }

    /**
     * string to double
     *
     * @param s
     * @return
     */
    public static double parseDouble(String s) {
        return parseDouble(s, DEFAULT_DOUBLE);
    }

    public static double parseDouble(String s, double defaultDouble) {
        if (StringUtils.isNull(s)) {
            return defaultDouble;
        }
        double value;
        try {
            value = Double.parseDouble(s);
        } catch (Exception e) {
            value = defaultDouble;
        }
        return value;
    }

    /**
     * 统一处理Html.fromHtml == null 崩溃问题
     * @param s
     * @return
     */
    public static CharSequence parseHtml(String s) {
        if(s == null) {
            return "";
        } else {
            return Html.fromHtml(s);
        }
    }
}
