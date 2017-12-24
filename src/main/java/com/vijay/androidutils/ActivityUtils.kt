package com.vijay.androidutils

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

/**
 * Created by vijay-3593 on 24/12/17.
 */
class ActivityUtils {
    fun <T : View> Activity.bind(@IdRes res: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findViewById(res) as T
    }
}