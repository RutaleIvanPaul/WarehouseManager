package io.ramani.ramaniWarehouse.domain.base.mappers

import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.reactivex.Flowable
import io.reactivex.Single

abstract class BaseAsyncUniModelMapper<From : Any, To : Any> protected constructor(private val threadExecutor: ThreadExecutor,
                                                                                   private val postThreadExecutor: PostThreadExecutor,
                                                                                   private val modelMapper: UniModelMapper<From, To>) : AsyncUniModelMapper<From, To> {
    open fun buildMapFrom(from: From): Single<To> =
            Single.fromCallable {
                from mapFromWith modelMapper
            }

    open fun buildMapFrom(list: List<From>): Flowable<List<To>> =
            Flowable.fromCallable {
                list mapFromWith modelMapper
            }

    override fun mapFrom(from: From): Single<To> =
            buildMapFrom(from)
                    .subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)

    override fun mapFrom(list: List<From>): Flowable<List<To>> =
            buildMapFrom(list)
                    .subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)
}