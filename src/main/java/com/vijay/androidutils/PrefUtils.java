package com.vijay.androidutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by vijay-3593 on 09/12/17.
 */


public class PrefUtils {

    private Context mContext;

    private SharedPreferences mSettings;
    private Editor mEditor;

    public PrefUtils(Context ctx, String prefFileName) {
        mContext = ctx;

        mSettings = mContext.getSharedPreferences(prefFileName,
                Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    public void setValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void setValue(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }


    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }


    public void setValue(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }


    public String getValue(String key, String defaultValue) {
        return mSettings.getString(key, defaultValue);
    }

    public int getIntValue(String key, int defaultValue) {
        return mSettings.getInt(key, defaultValue);
    }

    public long getLongValue(String key, long defaultValue) {
        return mSettings.getLong(key, defaultValue);
    }

    public boolean getValue(String key, boolean defValue) {
        return mSettings.getBoolean(key, defValue);
    }

    public void setValue(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void removeValue(String key) {
        if (mEditor != null) {
            mEditor.remove(key).commit();
        }
    }

    public boolean clear() {
        try {
            mEditor.clear().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}