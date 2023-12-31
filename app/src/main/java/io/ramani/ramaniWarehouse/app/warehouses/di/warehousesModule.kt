package io.ramani.ramaniWarehouse.app.warehouses.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.warehouses.invoices.mappers.InvoiceModelMapper
import io.ramani.ramaniWarehouse.app.warehouses.invoices.mappers.ProductModelMapper
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import io.ramani.ramaniWarehouse.app.warehouses.invoices.presentation.InvoicesViewModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.mappers.WarehouseModelMapper
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.MainNavViewModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.ProductModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider


val warehousesModule = Module("warehousesModule") {

    import(warehousesDataModule)
    import(warehousesDomainModule)

    bind<MainNavViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, MainNavViewModel.Factory(
                instance(), instance(), instance(), instance("loadWarehousesUseCase"), instance(),
                instance()
            )
        ).get(MainNavViewModel::class.java)
    }

    bind<InvoicesViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, InvoicesViewModel.Factory(
                instance(), instance(), instance(), instance("loadInvoicesUseCase"), instance()
            )
        ).get(InvoicesViewModel::class.java)
    }

    bind<ModelMapper<WarehouseModel, WarehouseModelView>>() with provider {
        WarehouseModelMapper()
    }

    bind<UniModelMapper<InvoiceModel, InvoiceModelView>>() with provider {
        InvoiceModelMapper(instance(),instance())
    }

    bind<UniModelMapper<ProductModel, ProductModelView>>() with provider {
        ProductModelMapper()
    }
}