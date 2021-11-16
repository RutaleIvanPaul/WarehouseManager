package io.ramani.ramaniWarehouse.domain.base.v2

import io.reactivex.Single

/**
 * Created by Amr on 9/11/17.
 */
interface SingleUseCase<T, P : Params> {
    fun getSingle(params: P? = null): Single<T>
}