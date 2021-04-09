package com.vivek.wo.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static final String PATTERN_YMD = "yyyy/MM/dd";
    public static final String PATTERN_YMDHM = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_YMDHMS = "yyyy/MM/dd HH:mm:ss";
    public static final String PATTERN_HM = "HH:mm";
    public static final String PATTERN_HMS = "HH:mm:ss";

    /**
     * 时间戳转换成字符串格式时间
     *
     * @param dateTime 时间戳
     * @param pattern  字符串的格式
     * @return
     */
    public static String longTimeToStringFormat(long dateTime, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date(dateTime));
    }

    /**
     * 字符串格式时间转换成时间戳
     *
     * @param dateTime 时间字符串
     * @param parrern  字符串的格式
     * @return
     */
    public static long stringDateTimeToLongTime(String dateTime, String parrern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(parrern);
        try {
            return dateFormat.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
