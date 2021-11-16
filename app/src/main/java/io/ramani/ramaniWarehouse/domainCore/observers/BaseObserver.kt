package io.ramani.ramaniWarehouse.core.domain.observers

import io.reactivex.observers.DisposableObserver

/**
 * Created by Amr on 10/27/17.
 */
open class BaseObserver<T>(private val doOnNext: (T) -> Unit = {},
                           private val doOnComplete: () -> Unit = {},
                           private val doOnError: (Throwable) -> Unit = {}) : DisposableObserver<T>() {
    override fun onNext(t: T) {
        doOnNext(t)
    }

    override fun onComplete() {
        doOnComplete()
    }

    override fun onError(e: Throwable) {
        doOnError(e)
    }
}