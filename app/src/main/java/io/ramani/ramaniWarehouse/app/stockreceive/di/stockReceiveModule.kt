package io.ramani.ramaniWarehouse.app.stockreceive.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.host.StockReceiveMainViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

/**
 * @description     Stock Receiving Module
 *
 * @author          Adrian
 */
val stockReceiveModule = Kodein.Module("stockReceiveModule") {
    import(stockReceiveDataModule)
    import(stockReceiveDomainModule)


    bind<StockReceiveMainViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, StockReceiveMainViewModel.Factory(
                instance(), instance(), instance()
            )
        ).get(StockReceiveMainViewModel::class.java)
    }

    // Receive Now Pages
    bind<StockReceiveNowViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, StockReceiveNowViewModel.Factory(
                instance(), instance(), instance(), instance("getSupplierUseCase"), instance("getDeclineReasonsUseCase")
            )
        ).get(StockReceiveNowViewModel::class.java)
    }

}