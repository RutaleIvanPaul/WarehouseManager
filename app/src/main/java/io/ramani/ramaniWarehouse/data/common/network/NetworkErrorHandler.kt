package io.ramani.ramaniWarehouse.data.common.network

/**
 * Created by Amr on 10/12/17.
 */
interface NetworkErrorHandler {
    fun handleError(throwable: Throwable): Throwable
}