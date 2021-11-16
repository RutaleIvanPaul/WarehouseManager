package io.ramani.ramaniWarehouse.domain.base

import io.ramani.ramaniWarehouse.domain.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.executor.ThreadExecutor
import io.reactivex.Flowable

/**
 * Created by Amr on 9/4/17.
 */
abstract class BaseFlowableUseCase<T> protected constructor(protected val threadExecutor: ThreadExecutor
                                                            , protected val postThreadExecutor: PostThreadExecutor) : FlowableUseCase<T> {
    protected abstract fun buildUseCaseFlowable(params: Params): Flowable<T>

    override fun getFlowable(params: Params): Flowable<T> =
            buildUseCaseFlowable(params).
                    subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)
}