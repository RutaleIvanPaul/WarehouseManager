package io.ramani.ramaniWarehouse.data.common.network

import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor(private val headersProvider: HeadersProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()
        val builder = request.newBuilder()

        val headers = headersProvider.getHeaders()

        val authHeader = headers[Headers.HEADER_AUTH] ?: ""
        builder.addHeader(Headers.HEADER_AUTH, authHeader)

        val languageHeader = headers[Headers.HEADER_LANGUAGE] ?: "en"
        builder.addHeader(Headers.HEADER_LANGUAGE, languageHeader)

        builder.addHeader(Headers.HEADER_CONTENT_TYPE, Headers.HEADER_CONTENT_TYPE_VALUE)
        builder.addHeader(Headers.HEADER_ACCEPT, Headers.HEADER_CONTENT_TYPE_VALUE)
        builder.addHeader(Headers.HEADER_CLIENT, ApiConstants.CLIENT_HEADER)

        if(headers.get(Headers.INVALIDATE_CACHE) != null) {
            builder.addHeader(Headers.INVALIDATE_CACHE, headers.get(Headers.INVALIDATE_CACHE))
        }

        builder.removeHeader(CustomHeaders.IGNORE_AUTHORIZATION)

        return chain.proceed(builder.build())
    }
}