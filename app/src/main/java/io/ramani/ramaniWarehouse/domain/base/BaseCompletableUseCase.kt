package io.ramani.ramaniWarehouse.domain.base

import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.reactivex.Completable

/**
 * Created by Amr on 9/4/17.
 */
abstract class BaseCompletableUseCase protected constructor(protected val threadExecutor: ThreadExecutor,
                                                            protected val postThreadExecutor: PostThreadExecutor
) : CompletableUseCase {
    protected abstract fun buildUseCaseCompletable(params: Params): Completable

    override fun getCompletable(params: Params): Completable =
            buildUseCaseCompletable(params).
                    subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)
}