package com.vijay.androidutils

import android.content.Context
import android.content.SharedPreferences

class PreferenceAPIImpl(val context: Context) : PreferenceAPI {
    private val sharedPreference: SharedPreferences = context.getSharedPreferences("SHARED_PREF", 0)

    override fun getString(key: String): String? {
        return sharedPreference.getString(key, null)
    }

    override fun putString(key: String, value: String) {
        sharedPreference.edit().putString(key, value).apply()
    }

    override fun getInt(key: String): Int {
        return sharedPreference.getInt(key, -1)
    }

    override fun putInt(key: String, value: Int) {
        sharedPreference.edit().putInt(key, value).apply()
    }

    override fun getLong(key: String): Long {
        return sharedPreference.getLong(key, -1L)
    }

    override fun putLong(key: String, value: Long) {
        sharedPreference.edit().putLong(key, value).apply()
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPreference.getBoolean(key, false)
    }

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreference.edit().putBoolean(key, value).apply()
    }
}