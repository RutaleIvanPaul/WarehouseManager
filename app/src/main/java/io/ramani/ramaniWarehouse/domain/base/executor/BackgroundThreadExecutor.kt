package io.ramani.ramaniWarehouse.domain.executor

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by Amr on 6/4/17.
 */
class BackgroundThreadExecutor : ThreadExecutor {
    override val scheduler: Scheduler = Schedulers.io()
}