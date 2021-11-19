package io.ramani.ramaniWarehouse.domain.base

import io.reactivex.Completable

/**
 * Created by Amr on 9/11/17.
 */
interface CompletableUseCase {
    fun getCompletable(params: Params = emptyParams()): Completable
}