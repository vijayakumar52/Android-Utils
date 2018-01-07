package com.vijay.androidutils;

import android.text.format.DateFormat;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/**
 * Created by vijay-3593 on 30/12/17.
 */

public class DateUtils {
    public static String getRelativeTime(Long time) {
        PrettyTime prettyTime = new PrettyTime();
        return prettyTime.format(new Date(time));
    }

    public static String getDate(Long dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }

    public static String getDate(Long dateInMilliseconds) {
        String format = "dd/MM/yyyy hh:mm:ss";
        return DateFormat.format(format, dateInMilliseconds).toString();
    }
}
