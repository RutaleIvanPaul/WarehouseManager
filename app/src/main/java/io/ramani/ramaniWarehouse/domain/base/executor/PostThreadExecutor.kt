package io.ramani.ramaniWarehouse.domain.base.executor

import io.reactivex.Scheduler

/**
 * Created by Amr on 6/1/17.
 */
interface PostThreadExecutor {
    val scheduler: Scheduler
}
