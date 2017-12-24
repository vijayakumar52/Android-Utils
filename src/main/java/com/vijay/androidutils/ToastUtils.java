package com.vijay.androidutils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtils {


    public static void showToast(Context context, @StringRes int message) {
        showToastShort(context, message);
    }

    public static void showToastShort(Context context, @StringRes int message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, @StringRes int message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, @StringRes int message, int duration) {
        Toast.makeText(context, message, duration).show();
    }


    public static void showToast(Context context, String message) {
        showToastShort(context, message);
    }

    public static void showToastShort(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

}
