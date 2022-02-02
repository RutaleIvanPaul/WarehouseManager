package io.ramani.ramaniWarehouse.app.warehouses.di

import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.warehouses.models.GetWarehousesRequestModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.ramani.ramaniWarehouse.domain.warehouses.useCases.LoadInvoicesUseCase
import io.ramani.ramaniWarehouse.domain.warehouses.useCases.LoadWarehousesUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val warehousesDomainModule = Kodein.Module("warehousesDomainModule") {
    bind<BaseSingleUseCase<PagedList<WarehouseModel>, GetWarehousesRequestModel>>("loadWarehousesUseCase") with provider {
        LoadWarehousesUseCase(instance(), instance(), instance("warehousesDataSource"))
    }

    bind<BaseSingleUseCase<PagedList<InvoiceModel>, GetWarehousesRequestModel>>("loadInvoicesUseCase") with provider {
        LoadInvoicesUseCase(instance(), instance(), instance("warehousesDataSource"))
    }
}