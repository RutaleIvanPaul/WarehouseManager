package io.ramani.ramaniWarehouse.domain.base.mappers

import io.reactivex.Flowable
import io.reactivex.Single

interface AsyncModelMapper<From, To> {
    fun mapFrom(from: From): Single<To>

    fun mapTo(to: To): Single<From>

    fun mapFrom(list: List<From>): Flowable<List<To>>

    fun mapTo(list: List<To>): Flowable<List<From>>
}