package io.ramani.ramaniWarehouse.domain.executor

import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Amr on 6/4/17.
 */
class UiThreadExecutor : PostThreadExecutor {
    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}