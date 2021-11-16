package io.ramani.ramaniWarehouse.domain.builder

import io.ramani.ramaniWarehouse.domain.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.executor.ThreadExecutor
import io.reactivex.Completable

/**
 * Created by Amr on 12/30/17.
 */
abstract class CompletableUseCaseBuilder protected constructor(private val threadExecutor: ThreadExecutor
                                                               , private val postThreadExecutor: PostThreadExecutor) {
    protected fun buildCompletable(completable: Completable) =
            completable.subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)
}