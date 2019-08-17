package com.vijay.androidutils.network

import com.vijay.androidutils.network.ErrorObject

interface AsyncTaskListener {

    fun onSuccess(response: Any, extras: Any?)

    fun onError(errorObject: ErrorObject, extras: Any?)
}
