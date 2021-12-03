package io.ramani.ramaniWarehouse.app.returnstock.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.mapper.SalespersonRVMapper
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.model.SalespersonRVModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.mappers.WarehouseModelMapper
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val returnStockModule = Kodein.Module("returnStockModule") {

    import(returnStockDataModule)
    import(returnStockDomainModule)

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
                instance(), instance(), instance(), instance("getSalespeopleUseCase"), instance()
            )
        ).get(SalesPersonViewModel::class.java)
    }

    bind<ModelMapper<SalespeopleModel, SalespersonRVModel>>() with provider {
        SalespersonRVMapper()
    }
}