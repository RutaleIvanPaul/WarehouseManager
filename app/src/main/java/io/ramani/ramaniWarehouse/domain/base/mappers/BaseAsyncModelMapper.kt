package io.ramani.ramaniWarehouse.domain.base.mappers

import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.reactivex.Flowable
import io.reactivex.Single

abstract class BaseAsyncModelMapper<From, To> protected constructor(protected val threadExecutor: ThreadExecutor
                                                                    , protected val postThreadExecutor: PostThreadExecutor
) : AsyncModelMapper<From, To> {
    abstract fun buildMapFrom(from: From): Single<To>

    abstract fun buildMapFrom(list: List<From>): Flowable<List<To>>

    abstract fun buildMapTo(to: To): Single<From>

    abstract fun buildMapTo(list: List<To>): Flowable<List<From>>

    override fun mapFrom(from: From): Single<To> =
            buildMapFrom(from)
                    .subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)

    override fun mapFrom(list: List<From>): Flowable<List<To>> =
            buildMapFrom(list)
                    .subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)

    override fun mapTo(to: To): Single<From> =
            buildMapTo(to)
                    .subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)

    override fun mapTo(list: List<To>): Flowable<List<From>> =
            buildMapTo(list)
                    .subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)
}