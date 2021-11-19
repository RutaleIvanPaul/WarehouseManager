package io.ramani.ramaniWarehouse.domain.base

import io.reactivex.Observable

/**
 * Created by Amr on 9/11/17.
 */
interface ObservableUseCase<T> {
    fun getObservable(params: Params = emptyParams()): Observable<T>
}