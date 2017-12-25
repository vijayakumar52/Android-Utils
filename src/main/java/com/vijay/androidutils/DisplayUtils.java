package com.vijay.androidutils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by vijay-3593 on 25/12/17.
 */

public class DisplayUtils {
    public static int getScreenWidthinDp(Activity context) {
        int pixel = context.getWindowManager().getDefaultDisplay().getWidth();
        return (int) (pixel * context.getResources().getDisplayMetrics().density);
    }

    public static int getScreenWidthinPx(Activity context) {
        int pixel = context.getWindowManager().getDefaultDisplay().getWidth();
        return pixel;
    }

    public static int getScreenHeightinDp(Activity context) {
        int pixel = context.getWindowManager().getDefaultDisplay().getHeight();
        return (int) (pixel * context.getResources().getDisplayMetrics().density);
    }

    public static int getScreenHeightinPx(Activity context) {
        int pixel = context.getWindowManager().getDefaultDisplay().getHeight();
        return pixel;
    }


    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float getDisplayHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static float inchToPx(float inch) {
        return inch * 96;
    }

    public static double mmToPx(float mm) {
        return (mm * 96) / 25.4;
    }

}
