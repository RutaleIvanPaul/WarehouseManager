package io.ramani.ramaniWarehouse.domain.executor.test

import io.ramani.ramaniWarehouse.domain.executor.ThreadExecutor
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by Amr on 9/11/17.
 */
class TestExecutor : ThreadExecutor {
    override val scheduler: Scheduler = Schedulers.single()
}