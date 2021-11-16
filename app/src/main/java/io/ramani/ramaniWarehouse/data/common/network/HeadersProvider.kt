package io.ramani.ramaniWarehouse.data.common.network

/**
 * Created by Amr on 9/11/17.
 */
interface HeadersProvider {
    fun getHeaders(): Map<String, String>

    fun getHeader(name: String): String
}