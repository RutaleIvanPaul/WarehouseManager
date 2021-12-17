package io.ramani.ramaniWarehouse.domain.warehouses

import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.warehouses.models.GetWarehousesRequestModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.Single

interface WarehousesDataSource {
    fun getWarehouses(getWarehousesRequestModel: GetWarehousesRequestModel): Single<PagedList<WarehouseModel>>
    fun getInvoices(getWarehousesRequestModel: GetWarehousesRequestModel): Single<PagedList<InvoiceModel>>
}