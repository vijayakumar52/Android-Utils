/*
 * $Id$
 * 02/03/16
 */
package com.vijay.androidutils

import android.util.Log

object Logger {
    //You are so done
    fun d(tag: String, message: String) {
        d(tag, message, null)
    }

    fun d(tag: String, message: String, throwable: Throwable?) {
        if (isLoggable()) {
            Log.d(tag, message, throwable)
        }
    }

    //Oops!
    fun e(tag: String, errorMsg: String) {
        e(tag, errorMsg, null)
    }

    fun e(tag: String, errorMsg: String, throwable: Throwable?) {
        if (isLoggable()) {
            Log.e(tag, errorMsg, throwable)
        }
    }


    //Ahh I see
    fun i(tag: String, info: String) {
        i(tag, info, null)
    }

    fun i(tag: String, info: String, throwable: Throwable?) {
        if (isLoggable()) {
            Log.i(tag, info, throwable)
        }
    }

    //Watch out!!
    fun w(tag: String, message: String) {
        w(tag, message, null)
    }

    fun w(tag: String, message: String, throwable: Throwable?) {
        if (isLoggable()) {
            Log.w(tag, message, throwable)
        }
    }

    //Gone crazy?
    fun v(tag: String, message: String) {
        v(tag, message, null)
    }

    fun v(tag: String, message: String, throwable: Throwable?) {
        if (isLoggable()) {
            Log.v(tag, message, throwable)
        }
    }


    /* //How in the world this happened?
     fun wtf(tag: String, message: String) {
         wtf(tag, message, null)
     }

     fun wtf(tag: String, message: String, throwable: Throwable?) {
         if (isLoggable()) {
             Log.wtf(tag, message, throwable)
         }
         ZAnalyticsNonFatal.setNonFatalException(throwable, JSONObject().apply { put("EXTRA", message) })
     }*/

    private fun isLoggable(): Boolean {
        return BuildConfig.DEBUG
    }

    fun printLog(e: Exception) {
        e("error", e.message + "")
    }
}
