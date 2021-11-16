package io.ramani.ramaniWarehouse.app.main.di

import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.main.presentation.MainViewModel
import io.ramani.ramaniWarehouse.app.main.presentation.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val mainModule = Kodein.Module("mainModule") {
    bind<MainViewModel>() with factory { activity: BaseActivity ->
        ViewModelProvider(
            activity, MainViewModelFactory(
                instance(), instance(), instance()
            )
        ).get(MainViewModel::class.java)
    }
}