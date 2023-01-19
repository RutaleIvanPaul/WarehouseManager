package io.ramani.ramaniWarehouse.app.viewstockbalance.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.viewstockbalance.model.ViewStockBalanceViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val viewStockBalanceModule = Kodein.Module("viewStockBalanceModule") {

    import(viewStockBalanceDataModule)
    import(viewStockBalanceDomainModule)

    bind<ViewStockBalanceViewModel>() with factory{ fragment: Fragment ->
        ViewModelProvider(
            fragment, ViewStockBalanceViewModel.Factory(
                instance(), instance(), instance(),
                instance("getCompanyProductsUseCase"),
                instance("reportsQueryUseCase"),
                instance(), instance()
            )
        ).get(ViewStockBalanceViewModel::class.java)

    }
}