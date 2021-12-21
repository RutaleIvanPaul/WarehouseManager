package io.ramani.ramaniWarehouse.app.assignstock.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.mapper.SalesPersonRVMapper
import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.model.SalesPersonRVModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val assignStockModule = Kodein.Module("assignStockModule") {

    import(assignStockDataModule)
    import(assignStockDomainModule)

    bind<AssignStockViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, AssignStockViewModel.Factory(
                instance(), instance(), instance()
            )
        ).get(AssignStockViewModel::class.java)
    }

    bind<AssignStockSalesPersonViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, AssignStockSalesPersonViewModel.Factory(
                instance(),
                instance(),
                instance(),
                instance("getSalesPersonUseCase"),
                instance(),
                instance()
            )
        ).get(AssignStockSalesPersonViewModel::class.java)
    }

    bind<ModelMapper<SalesPersonModel, SalesPersonRVModel>>() with provider {
        SalesPersonRVMapper()
    }
}