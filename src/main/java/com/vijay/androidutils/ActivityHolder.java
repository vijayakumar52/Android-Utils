package com.vijay.androidutils;

import android.app.Activity;

/**
 * Created by vijay-3593 on 30/12/17.
 */

public class ActivityHolder {
    private static ActivityHolder instance = new ActivityHolder();

    public static ActivityHolder getInstance() {
        return instance;
    }

    Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
