package io.ramani.ramaniWarehouse.domain.base.v2

import io.reactivex.Completable

interface CompletableUseCase<P : Params> {
    fun getCompletable(params: P? = null): Completable
}