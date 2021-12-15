package io.ramani.ramaniWarehouse.data.common.network

import io.ramani.ramaniWarehouse.domainCore.prefs.Prefs
import java.util.*

/**
 * Created by Amr on 9/11/17.
 */
class GenericHeadersProvider(private val prefsManager: Prefs) : HeadersProvider {

    companion object {
        const val HEADER_AUTH = "sessionToken"
        const val HEADER_LANGUAGE = "Accept-Language"
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val HEADER_ACCEPT = "Accept"
        const val HEADER_CONTENT_TYPE_VALUE = "application/json"
        const val HEADER_CLIENT = "client"
        const val INVALIDATE_CACHE = "invalidate_cache"

    }

    override fun getHeaders(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (prefsManager.hasAccessToken) {
            map[HEADER_AUTH] = "Bearer " + prefsManager.accessToken
        } else {
            map[HEADER_AUTH] = "Bearer 950ae00bcecbb98c40a9d493648ca2f6f1d2cbc6"
        }

//        map[HEADER_LANGUAGE] = prefsManager.language.lowercase(Locale.getDefault())
        map[HEADER_CONTENT_TYPE] = HEADER_CONTENT_TYPE_VALUE
        map[HEADER_ACCEPT] = HEADER_CONTENT_TYPE_VALUE
        map[HEADER_CLIENT] = ApiConstants.CLIENT_HEADER
        map[INVALIDATE_CACHE] = true.toString()
        return map
    }

    override fun getHeader(name: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}