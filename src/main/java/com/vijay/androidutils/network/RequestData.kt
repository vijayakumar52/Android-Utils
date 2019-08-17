/* $Id$ */
package com.vijay.androidutils.network

import com.vijay.androidutils.AsyncTaskListener

class RequestData(requestDataBuilder: RequestDataBuilder) {
    val url = requestDataBuilder.url
    val postParameters = requestDataBuilder.postParameters
    val returnBytes = requestDataBuilder.returnBytes
    @StringRequest.Companion.Method
    val method = requestDataBuilder.method
    val extras = requestDataBuilder.extras
    val progressView = requestDataBuilder.progressView
    val requestCallback = requestDataBuilder.requestCallback
    val who = requestDataBuilder.who
    val authTokenHandler = requestDataBuilder.authTokenHandler

    class RequestDataBuilder(val url: String) {
        internal var postParameters: RequestPostParams<String, Any>? = null
        internal var returnBytes = true
        @StringRequest.Companion.Method
        internal var method = StringRequest.GET
        internal var extras: Any? = null
        internal var progressView: NetworkProgressView? = null
        internal var requestCallback: AsyncTaskListener? = null
        internal var who: String? = null
        internal var authTokenHandler: AuthenticatorAPI? = null

        fun addPostParams(params: RequestPostParams<String, Any>?): RequestDataBuilder {
            this.postParameters = params
            return this
        }

        fun setReturnTypeAsBytes(returnBytes: Boolean): RequestDataBuilder {
            this.returnBytes = returnBytes
            return this
        }

        fun setRequestMethod(@StringRequest.Companion.Method method: String): RequestDataBuilder {
            this.method = method
            return this
        }

        fun setExtras(extrasData: Any?): RequestDataBuilder {
            this.extras = extrasData
            return this
        }

        fun setProgressView(progressView: NetworkProgressView?): RequestDataBuilder {
            this.progressView = progressView
            return this
        }

        fun setCallback(callback: AsyncTaskListener?): RequestDataBuilder {
            this.requestCallback = callback
            return this
        }

        fun setWho(who: String?): RequestDataBuilder {
            this.who = who
            return this
        }

        fun setAuthTokenHandler(authTokenHandler: AuthenticatorAPI?): RequestDataBuilder {
            this.authTokenHandler = authTokenHandler
            return this
        }

        fun build(): RequestData {
            return RequestData(this)
        }
    }
}