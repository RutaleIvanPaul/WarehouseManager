package io.ramani.ramaniWarehouse.data.warehouses

import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.warehouses.WarehousesDataSource
import io.ramani.ramaniWarehouse.domain.warehouses.models.GetWarehousesRequestModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.Single

class WarehousesRepository(
    private val remoteWarehousesDataSource: WarehousesDataSource,
    private val localWarehousesDataSource: WarehousesDataSource
) :
    WarehousesDataSource {
    override fun getWarehouses(getWarehousesRequestModel: GetWarehousesRequestModel): Single<PagedList<WarehouseModel>> =
        remoteWarehousesDataSource.getWarehouses(getWarehousesRequestModel)

    override fun getInvoices(getWarehousesRequestModel: GetWarehousesRequestModel): Single<PagedList<InvoiceModel>> =
        remoteWarehousesDataSource.getInvoices(getWarehousesRequestModel)
}