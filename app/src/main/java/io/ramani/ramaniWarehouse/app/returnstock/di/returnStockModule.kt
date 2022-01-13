package io.ramani.ramaniWarehouse.app.returnstock.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ConfirmReturnStockViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ReturnSuccessViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.mapper.ReturnItemsMapper
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.products.SelectReturnItemsViewmodel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.mapper.SalespersonRVMapper
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.model.SalespersonRVModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.mappers.WarehouseModelMapper
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import io.ramani.ramaniWarehouse.data.returnStock.model.OfProducts
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
                instance(), instance(), instance(),instance("postReturnedStockUseCase")
                ,instance(),instance(),instance()
            )
        ).get(ReturnStockViewModel::class.java)
    }

    bind<SalesPersonViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, SalesPersonViewModel.Factory(
                instance(), instance(), instance(), instance("getSalespeopleUseCase"), instance(),instance()
            )
        ).get(SalesPersonViewModel::class.java)
    }

    bind<SelectReturnItemsViewmodel>() with factory{ fragment: Fragment ->
        ViewModelProvider(
            fragment,SelectReturnItemsViewmodel.Factory(
                instance(), instance(), instance(),
                instance("getAvailableStockUseCase")
            )
        ).get(SelectReturnItemsViewmodel::class.java)

    }

    bind<ConfirmReturnStockViewModel>() with factory{ fragment: Fragment ->
        ViewModelProvider(
            fragment,ConfirmReturnStockViewModel.Factory(
                instance(), instance(), instance(), instance("postReturnedStockUseCase")
                , instance(), instance(), instance()
            )
        ).get(ConfirmReturnStockViewModel::class.java)

    }

    bind<ReturnSuccessViewModel>() with factory{ fragment: Fragment ->
        ViewModelProvider(
            fragment,ReturnSuccessViewModel.Factory(
                instance(), instance(), instance()
            )
        ).get(ReturnSuccessViewModel::class.java)

    }

    bind<ModelMapper<SalespeopleModel, SalespersonRVModel>>() with provider {
        SalespersonRVMapper()
    }

    bind<ModelMapper<AvailableProductItem, OfProducts>>() with provider {
        ReturnItemsMapper()
    }

}