package com.vijay.androidutils;

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
}
