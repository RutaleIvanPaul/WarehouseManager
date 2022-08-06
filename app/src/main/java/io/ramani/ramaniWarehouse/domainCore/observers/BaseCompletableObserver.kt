package io.ramani.ramaniWarehouse.core.domain.observers

import io.reactivex.observers.DisposableCompletableObserver

/**
 * Created by Amr on 10/27/17.
 */
open class BaseCompletableObserver(private val doOnComplete: () -> Unit = {},
                                   private val doOnErrror: (Throwable) -> Unit = {}) : DisposableCompletableObserver() {
    override fun onComplete() {
        doOnComplete()
    }

    override fun onError(e: Throwable) {
        doOnErrror(e)
    }
}