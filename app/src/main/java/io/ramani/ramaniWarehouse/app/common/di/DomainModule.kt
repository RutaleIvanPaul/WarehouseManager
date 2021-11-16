package io.ramani.ramaniWarehouse.app.common.di

import io.ramani.ramaniWarehouse.domain.executor.BackgroundThreadExecutor
import io.ramani.ramaniWarehouse.domain.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.executor.UiThreadExecutor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

/**
 * Created by Amr on 9/11/17.
 */
val domainModule = Kodein.Module {
    bind<ThreadExecutor>() with provider { BackgroundThreadExecutor() }
    bind<PostThreadExecutor>() with provider { UiThreadExecutor() }
}