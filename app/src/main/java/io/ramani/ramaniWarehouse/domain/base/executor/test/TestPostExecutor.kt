package io.ramani.ramaniWarehouse.domain.executor.test

import io.ramani.ramaniWarehouse.domain.executor.PostThreadExecutor
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by Amr on 9/11/17.
 */
class TestPostExecutor : PostThreadExecutor {
    override val scheduler: Scheduler = Schedulers.single()
}