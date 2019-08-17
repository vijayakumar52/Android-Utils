package com.vijay.androidutils.network

interface NetworkEventListener {

    fun onPreExecute()

    fun onProgressUpdate()

    fun onSuccess(response: Any)

    fun onError(errorObject: ErrorObject)
}