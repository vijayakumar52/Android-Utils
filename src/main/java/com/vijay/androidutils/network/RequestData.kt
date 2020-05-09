/* $Id$ */
package com.vijay.androidutils.network

class RequestData(requestDataBuilder: RequestDataBuilder) {
    val url = requestDataBuilder.url
    val postParameters = requestDataBuilder.postParameters
    @NetworkRequest.Companion.Method
    val method = requestDataBuilder.method

    class RequestDataBuilder(val url: String) {
        internal var postParameters: RequestPostParams<String, Any>? = null
        @NetworkRequest.Companion.Method
        internal var method = NetworkRequest.GET

        fun addPostParams(params: RequestPostParams<String, Any>?): RequestDataBuilder {
            this.postParameters = params
            return this
        }

        fun build(): RequestData {
            return RequestData(this)
        }
    }
}