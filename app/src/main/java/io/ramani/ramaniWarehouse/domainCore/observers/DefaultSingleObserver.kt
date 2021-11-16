package io.ramani.ramaniWarehouse.core.domain.observers

import io.ramani.ramaniWarehouse.core.domain.presentation.ErrorHandlerView


/**
 * Created by Amr on 10/27/17.
 */
class DefaultSingleObserver<T>(doOnSuccess: (T) -> Unit = {},
                               doOnError: (Throwable) -> Unit = {},
                               private val errorHandler: ErrorHandlerView?) : BaseSingleObserver<T>(doOnSuccess, doOnError) {
    override fun onError(e: Throwable) {
        if (errorHandler?.handleError(e) == false) {
            super.onError(e)
        }
    }
}