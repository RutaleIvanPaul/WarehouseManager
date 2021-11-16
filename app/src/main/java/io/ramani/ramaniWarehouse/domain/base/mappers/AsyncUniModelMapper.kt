package io.ramani.ramaniWarehouse.domain.base.mappers

import io.reactivex.Flowable
import io.reactivex.Single

interface AsyncUniModelMapper<From, To> {
    fun mapFrom(from: From): Single<To>

    fun mapFrom(list: List<From>): Flowable<List<To>>
}