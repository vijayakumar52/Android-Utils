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
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

class NetworkRequest(private val requestData: RequestData, private val eventListener: NetworkEventListener?) {
    private var call: Call? = null
    var errorObject = ErrorObject()

    @Method
    private var method = requestData.method

    fun executeRequest(oAuthToken: String?) {
        eventListener?.onPreExecute()
        var responseCode = 0

        var postParams: RequestBody? = null
        val postParameters = requestData.postParameters
        if (postParameters != null) {
            if (postParameters.size != 0) {
                postParams = getRequestBody(postParameters)
            }
        }
        val requestBuilder = Request.Builder()

        if (oAuthToken != null && "" != oAuthToken) {
            requestBuilder.addHeader("Authorization", "$oAuthToken") //No i18N
        }

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
            requestBuilder.post(postParams)
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
        call = getOkHttpClient().newCall(request)
        call!!.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                if (e is SocketTimeoutException) {
                    errorObject = ErrorObject(ErrorObject.TIMEOUT)
                } else if (e is IOException) {
                    errorObject = ErrorObject(ErrorObject.TIMEOUT)
                }
                processCallback(null, errorObject)
            }

            override fun onResponse(call: Call?, response: Response?) {
                var networkResponse: Any? = null
                responseCode = response!!.code()
                val responseStream = response.body()!!.source()

                var errorResponse = ""
                try {
                    val returnBytes = requestData.returnBytes
                    if (returnBytes) {
                        networkResponse = responseStream.readByteArray()
                    } else {
                        networkResponse = responseStream.readUtf8()
                    }

                    if (responseCode == 200) {
                        processCallback(networkResponse, errorObject)
                        return
                    }

                    //Handling failure cases

                    if (!returnBytes) {
                        errorResponse = networkResponse as String
                    } else {
                        errorResponse = String((networkResponse as ByteArray?)!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (errorObject.reason == ErrorObject.UNKNOWN) {
                    errorObject.extraData = errorResponse
                }
                processCallback(null, errorObject)
            }
        })
    }

    fun processCallback(response: Any?, errorObject: ErrorObject) {
        AndroidSchedulers.mainThread().scheduleDirect {
            val httpClient = getOkHttpClient()
            Logger.d("OkHttp", "Total requests :${httpClient.dispatcher().runningCallsCount() + httpClient.dispatcher().queuedCallsCount()}")
            if (response == null) {
                eventListener?.onError(errorObject)
            } else {
                eventListener?.onSuccess(response)
            }
        }
    }

    private fun getUserAgent(): String {

        val androidVersion = Build.VERSION.RELEASE
        val deviceModel = Build.MODEL
        val appVersion = BuildConfig.VERSION_NAME

        val userAgent = StringBuilder()
        userAgent.append("TrendingNow") //No i18N
        try {
            userAgent.append("/").append(appVersion)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        userAgent.append("(").append("Android ").append(androidVersion).append("; ").append(deviceModel)
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
                    var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
                    mimeType = mimeType ?: "application/pdf" //No i18N
                    requestBody.addFormDataPart(key, value.name, RequestBody.create(MediaType.parse(mimeType), value))
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
        fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
            if (Build.VERSION.SDK_INT in 16..20) {
                try {
                    val sc = SSLContext.getInstance("TLSv1.2") //No i18N
                    sc.init(null, null, null)
                    client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory))

                    val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                            .tlsVersions(TlsVersion.TLS_1_2)
                            .build()

                    val specs = ArrayList<ConnectionSpec>()
                    specs.add(cs)
                    specs.add(ConnectionSpec.COMPATIBLE_TLS)
                    specs.add(ConnectionSpec.CLEARTEXT)

                    client.connectionSpecs(specs)
                } catch (exc: Exception) {

                }

            }

            return client
        }

        private fun getOkHttpClient(): OkHttpClient {
            if (client == null) {
                val clientBuilder = OkHttpClient.Builder()
                clientBuilder.connectTimeout(20, TimeUnit.SECONDS)
                clientBuilder.readTimeout(20, TimeUnit.SECONDS)
                clientBuilder.writeTimeout(20, TimeUnit.SECONDS)
                //Logging
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Logger.d("OkHttp", it) })
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
                    loggingInterceptor.redactHeader("Authorization")
                    loggingInterceptor.redactHeader("Cookie")
                    clientBuilder.addInterceptor(loggingInterceptor)
                }
                client = enableTls12OnPreLollipop(clientBuilder).build()
            }

            return client!!
        }

        @StringDef(GET, POST, DELETE)
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class Method
    }

    fun addQueryParams(url: Uri.Builder, queryParams: HashMap<String, String>?) {
        if (queryParams != null && queryParams.size > 0) {
            val keyPairs = queryParams.entries
            for ((key, value) in keyPairs) {
                url.appendQueryParameter(key, value)
            }
        }
    }
}