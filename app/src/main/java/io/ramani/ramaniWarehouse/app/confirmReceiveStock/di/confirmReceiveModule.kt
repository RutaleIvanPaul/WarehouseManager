package io.ramani.ramaniWarehouse.app.confirmReceiveStock.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.host.StockReceiveMainViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val confirmReceiveModule = Kodein.Module("confirmReceiveModule") {
    bind<ConfirmReceiveViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, ConfirmReceiveViewModel.Factory(
                instance(), instance(), instance(), instance()
            )
        ).get(ConfirmReceiveViewModel::class.java)
    }
}