package io.ramani.ramaniWarehouse.app.returnstock.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val returnStockModule = Kodein.Module("returnStockModule") {

    bind<ReturnStockViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, ReturnStockViewModel.Factory(
                instance(), instance(), instance()
            )
        ).get(ReturnStockViewModel::class.java)
    }

    bind<SalesPersonViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, SalesPersonViewModel.Factory(
                instance(), instance(), instance()
            )
        ).get(SalesPersonViewModel::class.java)
    }

}