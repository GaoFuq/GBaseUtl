package com.gfq.gbaseutl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * create by 高富强
 * on {2019/11/1} {14:27}
 * desctapion:
 */
public class DateFormatUtil {
    private static DateFormat dateFormat;
    //    = new SimpleDateFormat("yy-MM-dd hh:mm:ss", Locale.CHINA);
    private static Date date;

    //日期格式，精确到日 2017-4-16
    public static String format(long timeMills) {
        date = new Date(timeMills);
        dateFormat = DateFormat.getDateInstance();
        return dateFormat.format(date);
    }

    //可以精确到秒  2017-4-16 12:43:37
    DateFormat df2 = DateFormat.getDateTimeInstance();

    //只显示出时时分秒 12:43:37
    DateFormat df3 = DateFormat.getTimeInstance();

    //显示日期，周，上下午，时间（精确到秒）
//2017年4月16日 星期日 下午12时43分37秒 CST
    DateFormat df4 = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

    //显示日期,上下午，时间（精确到秒）
//2017年4月16日 下午12时43分37秒
    DateFormat df5 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

    //显示日期,上下午，时间（精确到秒）
//2017年4月16日 下午12时43分37秒
    DateFormat df5_1 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.CHINA);
    //显示日期，上下午,时间（精确到分） 17-4-16 下午12:43
    DateFormat df6 = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    //显示日期，时间（精确到秒） 2017-4-16 12:43:37
    DateFormat df7 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);


    /**
     * 获取指定日期的时间戳
     *
     * @param dateStr yyyy-MM-dd hh:mm:ss
     * @return 时间戳
     */
    public static long getTimeMillsFromDateString(String dateStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        Date date;
        Calendar cal;
        long timestamp = 0;
        try {
            date = df.parse(dateStr);
            cal = Calendar.getInstance();
            if (date != null) {
                cal.setTime(date);
            }
            timestamp = cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}
