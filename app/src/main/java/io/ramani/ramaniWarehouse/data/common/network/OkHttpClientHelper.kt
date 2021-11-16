package io.ramani.ramaniWarehouse.data.common.network

import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by Amr on 9/22/17.
 */
object OkHttpClientHelper {
    fun buildClient(
        prefsManager: PrefsManager,
        connectionTimeOut: Long,
        readTimeOut: Long,
        writeTimeOut: Long,
        headersInterceptor: Interceptor,
        interceptor: Interceptor,
        chuckInterceptor: Interceptor
    ) =
        OkHttpClient.Builder()
            .apply {
                connectTimeout(connectionTimeOut, TimeUnit.SECONDS)
                readTimeout(readTimeOut, TimeUnit.SECONDS)
                writeTimeout(writeTimeOut, TimeUnit.SECONDS)
                addInterceptor(headersInterceptor)
                addInterceptor(chuckInterceptor)
                addInterceptor { chain ->
                    val original = chain.request()
                    val httpUrl = original.url()
                    val newUrl = httpUrl.newBuilder().addQueryParameter("lang", prefsManager.language).build()
                    val requestBuilder = original.newBuilder().url(newUrl)
                    val req = requestBuilder.build()
                    chain.proceed(req)
                }
                @Suppress("ConstantConditionIf")
                if (BuildConfig.HTTP_LOGGING_ENABLED) {
                    addInterceptor(interceptor)
                }
            }
            .build()
}