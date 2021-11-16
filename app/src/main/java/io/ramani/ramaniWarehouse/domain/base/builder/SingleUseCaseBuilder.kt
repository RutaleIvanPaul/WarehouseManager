package io.ramani.ramaniWarehouse.domain.builder

import io.ramani.ramaniWarehouse.domain.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.executor.ThreadExecutor
import io.reactivex.Single

/**
 * Created by Amr on 12/30/17.
 */
abstract class SingleUseCaseBuilder<T> protected constructor(private val threadExecutor: ThreadExecutor
                                                          , private val postThreadExecutor: PostThreadExecutor) {
    protected fun buildSingle(single: Single<T>) =
            single.subscribeOn(threadExecutor.scheduler)
                    .observeOn(postThreadExecutor.scheduler)
}