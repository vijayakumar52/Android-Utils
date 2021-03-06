package com.vijay.androidutils;

/**
 * Created by vijay-3593 on 19/11/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class PrefUtils {
    private static final String SHARED_PREF = "PREFERENCE";


    static public void setPrefValueString(Context activity, String key, String value) {
        SharedPreferences sharedPreference = activity.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(key, value).apply();
    }

    static public void setPrefValueBoolean(Context activity, String key, boolean value) {
        final SharedPreferences sharedPreference = activity.getSharedPreferences(SHARED_PREF, 0);
        final SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(key, value).apply();
    }

    static public void setPrefValueLong(Context activity, String key, long value) {
        final SharedPreferences sharedPreference = activity.getSharedPreferences(SHARED_PREF, 0);
        final SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putLong(key, value).apply();
    }

    static public void setPrefValueInt(Context activity, String key, int value) {
        final SharedPreferences sharedPreference = activity.getSharedPreferences(SHARED_PREF, 0);
        final SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putInt(key, value).apply();
    }

    static public String getPrefValueString(Context activity, String key) {
        final SharedPreferences sharedPreference = activity.getSharedPreferences(SHARED_PREF, 0);
        return sharedPreference.getString(key, ""); //No i18N
    }

    static public String getStrictPrefValueString(Activity activity, String key) throws Exception {
        final SharedPreferences sharedPreference = activity.getSharedPreferences(SHARED_PREF, 0);
        if (sharedPreference.contains(key)) {
            return sharedPreference.getString(key, ""); //No i18N
        } else {
            throw new Exception();
        }
    }

    static public boolean getPrefValueBoolean(Context activity, String key) {
        final SharedPreferences sharedPreference = activity.getSharedPreferences(SHARED_PREF, 0);
        return sharedPreference.getBoolean(key, false);
    }

    static public long getPrefValueLong(Context activity, String key) {
        final SharedPreferences sharedPreference = activity.getSharedPreferences(SHARED_PREF, 0);
        return sharedPreference.getLong(key, -1);
    }

    static public int getPrefValueInt(Context activity, String key) {
        final SharedPreferences sharedPreference = activity.getSharedPreferences(SHARED_PREF, 0);
        return sharedPreference.getInt(key, -1);
    }

    static public void deletePrefValue(Context activity, String key) {
        SharedPreferences prefs = activity.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key).apply();
    }

    static public void putStringSet(Context context, String key, Set<String> stringSet) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet(key, stringSet).apply();
    }

    static public Set<String> getStringSet(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF, 0);
        return prefs.getStringSet(key, null);
    }
}
