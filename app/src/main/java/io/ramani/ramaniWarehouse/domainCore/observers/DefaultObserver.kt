package io.ramani.ramaniWarehouse.domainCore.observers

import io.ramani.ramaniWarehouse.core.domain.observers.BaseObserver
import io.ramani.ramaniWarehouse.domainCore.presentation.ErrorHandlerView


/**
 * Created by Amr on 10/27/17.
 */
class DefaultObserver<T>(doOnNext: (T) -> Unit = {},
                         doOnComplete: () -> Unit = {},
                         doOnError: (Throwable) -> Unit = {},
                         private val errorHandler: ErrorHandlerView?) : BaseObserver<T>(doOnNext, doOnComplete, doOnError) {
    override fun onError(e: Throwable) {
        if (errorHandler?.handleError(e) == false) {
            super.onError(e)
        }
    }
}