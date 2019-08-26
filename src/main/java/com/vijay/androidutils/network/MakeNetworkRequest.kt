/* $Id$ */
package com.vijay.androidutils.network

import com.blankj.utilcode.util.NetworkUtils
import com.vijay.androidutils.Logger

class MakeNetworkRequest(private val requestData: RequestData) : NetworkEventListener {
    val authTokenHandler = requestData.authTokenHandler
    val who = requestData.who
    val progressView = requestData.progressView
    val callback = requestData.requestCallback
    val extras = requestData.extras
    var oAuthToken: String? = null

    var retryCount = 0

    fun executeAsyncTask() {
        addToQueue()
        if (NetworkUtils.isConnected()) {
            if (authTokenHandler == null) {
                executeTask(null)
            } else {
                executeTask(authTokenHandler.token)
            }
        } else {
            onError(ErrorObject(ErrorObject.NO_CONNECTION))
        }
    }

    private fun executeTask(authToken: String?) {
        this.oAuthToken = authToken
        NetworkRequest(requestData, this).executeRequest(authToken)
    }

    override fun onPreExecute() {
        showProgress()
    }

    override fun onProgressUpdate() {
        updateProgress()
    }

    override fun onSuccess(response: Any) {
        if (oAuthToken == null || authTokenHandler!!.isUserSignedIn) {
            onRequestEnded()

            if (progressView != null && progressView.dismissDialogOnSuccess()) {
                dismissProgress()
            }
            callback?.onSuccess(response, extras)
        } else {
            onError(ErrorObject(ErrorObject.UNKNOWN))
        }
    }

    override fun onError(errorObject: ErrorObject) {

        onRequestEnded()
        dismissProgress()

        if (errorObject.reason == ErrorObject.TIMEOUT) {
            if (retryCount <= maxRetryCount) {
                retryCount += 1
                executeAsyncTask()
                return
            } else {
                Logger.w(TAG, "Maximum retry reached for $who")
            }
        }

        callback?.onError(errorObject, extras)
        handleError(errorObject)
    }

    fun handleError(errorObject: ErrorObject) {
        //Handle common error
    }


    private fun addToQueue() {
        if (who != null) {
            allRequests.add(who)
        }
    }

    private fun removeFromQueue() {
        if (who != null) {
            allRequests.remove(who)
        }
    }

    private fun onRequestEnded() {
        removeFromQueue()
    }

    private fun showProgress() {
        progressView?.onStarted()
    }

    private fun updateProgress() {
        progressView?.onProgress()
    }

    private fun dismissProgress() {
        if (progressView != null && progressView.dismissDialogOnSuccess()) {
            progressView.onFinished()
        }
    }


    companion object {
        val TAG = MakeNetworkRequest::class.java.simpleName
        var allRequests: HashSet<String> = HashSet()
        val maxRetryCount = 2
    }

    fun cancel() {

    }
}