package io.ramani.ramaniWarehouse.core.domain.presentation

/**
 * Created by Amr on 10/27/17.
 */
interface ErrorHandlerView {
    fun handleError(throwable: Throwable): Boolean
}