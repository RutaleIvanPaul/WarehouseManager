package io.ramani.ramaniWarehouse.domain.executor

import io.reactivex.Scheduler

/**
 * Created by Amr on 6/1/17.
 */
interface PostThreadExecutor {
    val scheduler: Scheduler
}
