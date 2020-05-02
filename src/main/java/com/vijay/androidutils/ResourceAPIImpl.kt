package com.vijay.androidutils

import android.content.Context

class ResourceAPIImpl(context: Context) : ResourcesAPI {
    val prefAPI = PreferenceAPIImpl(context)
    override fun getPrefAPI(): PreferenceAPI {
        return prefAPI
    }
}