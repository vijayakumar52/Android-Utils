/* $Id$ */
package com.vijay.androidutils.network

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.annotation.StringDef
import com.blankj.utilcode.util.FileUtils
import com.vijay.androidutils.BuildConfig
import com.vijay.androidutils.Logger
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class NetworkRequest {

    @Throws(Exception::class)
    fun getString(requestData: RequestData): String {
        return executeRequest(requestData, false) as String
    }

    @Throws(Exception::class)
    fun getBytes(requestData: RequestData): ByteArray {
        return executeRequest(requestData, true) as ByteArray
    }

    @Throws(Exception::class)
    private fun executeRequest(requestData: RequestData, returnBytes: Boolean): Any {
        var responseCode = 0
        val method = requestData.method
        var postParams: RequestBody? = null
        val postParameters = requestData.postParameters
        if (postParameters != null) {
            if (postParameters.size != 0) {
                postParams = getRequestBody(postParameters)
            }
        }
        val requestBuilder = Request.Builder()

        var url = requestData.url
        if (GET == method) {
            if (postParameters != null) {
                val urlBuilder = Uri.parse(url).buildUpon()
                addQueryParams(urlBuilder, getParams(postParameters))
                url = urlBuilder.build().toString()
            }
            requestBuilder.get()
        } else if (POST == method) {
            //Post params must be not null. Throw exception otherwise.
            requestBuilder.post(postParams!!)
        } else if (DELETE == method) {
            if (postParams != null) {
                requestBuilder.delete(postParams)
            } else {
                requestBuilder.delete()
            }
        }
        requestBuilder.url(url)
        requestBuilder.header("User-Agent", getUserAgent()) //No i18N
        val request = requestBuilder.build()
        val call = getOkHttpClient().newCall(request)

        try {
            val response = call.execute()
            var networkResponse: Any? = null
            responseCode = response.code
            val responseStream = response.body!!.source()

            var errorResponse = ""
            if (returnBytes) {
                networkResponse = responseStream.readByteArray()
            } else {
                networkResponse = responseStream.readUtf8()
            }

            if (responseCode == 200) {
                return networkResponse
            } else {
                //Handling failure cases
                errorResponse = if (!returnBytes) {
                    networkResponse as String
                } else {
                    String((networkResponse as ByteArray?)!!)
                }
                throw Exception(errorResponse)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun getUserAgent(): String {

        val androidVersion = Build.VERSION.RELEASE
        val deviceModel = Build.MODEL
        val appVersion = BuildConfig.VERSION_NAME

        val userAgent = StringBuilder()
        userAgent.append("OkHttp") //No i18N
        try {
            userAgent.append("/").append(appVersion)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        userAgent.append("(").append("Android ").append(androidVersion).append("; ")
            .append(deviceModel)
            .append(")") //No i18N
        return userAgent.toString()
    }

    private fun getRequestBody(postParameters: RequestPostParams<String, Any>): RequestBody {
        val requestBody = MultipartBody.Builder()
        requestBody.setType(MultipartBody.FORM)
        val keys = postParameters.paramsKeys
        for (key in keys) {
            val allValues = postParameters.getParams(key)
            for (value in allValues) {
                if (value is File) {
                    val fileExtension = FileUtils.getFileExtension(value.path)
                    var mimeType =
                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
                    mimeType = mimeType ?: "application/pdf" //No i18N
                    requestBody.addFormDataPart(
                        key,
                        value.name,
                        RequestBody.create(mimeType.toMediaTypeOrNull(), value)
                    )
                } else {
                    requestBody.addFormDataPart(key, value.toString())
                }
            }
        }
        return requestBody.build()
    }

    fun getParams(postParams: RequestPostParams<String, Any>): HashMap<String, String> {
        val newMap = HashMap<String, String>()
        val keyParams = postParams.paramsKeys
        for (key in keyParams) {
            val allValues = postParams.getParams(key)
            for (value in allValues) {
                if (value is String) {
                    newMap[key] = value
                }
            }
        }
        return newMap
    }

    companion object {

        const val GET = "GET" //No i18N
        const val POST = "POST" //No i18N
        const val DELETE = "delete" //No i18N

        private var client: OkHttpClient? = null

        private fun getOkHttpClient(): OkHttpClient {
            if (client == null) {
                val clientBuilder = OkHttpClient.Builder()
                clientBuilder.connectTimeout(20, TimeUnit.SECONDS)
                clientBuilder.readTimeout(20, TimeUnit.SECONDS)
                clientBuilder.writeTimeout(20, TimeUnit.SECONDS)
                //Logging
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor =
                        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                            Logger.d(
                                "OkHttp",
                                it
                            )
                        })
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
                    loggingInterceptor.redactHeader("Authorization")
                    loggingInterceptor.redactHeader("Cookie")
                    clientBuilder.addInterceptor(loggingInterceptor)
                }
                client = clientBuilder.build()
            }

            return client!!
        }

        @StringDef(GET, POST, DELETE)
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class Method

        fun addQueryParams(url: Uri.Builder, queryParams: HashMap<String, String>?) {
            if (queryParams != null && queryParams.size > 0) {
                val keyPairs = queryParams.entries
                for ((key, value) in keyPairs) {
                    url.appendQueryParameter(key, value)
                }
            }
        }
    }
}