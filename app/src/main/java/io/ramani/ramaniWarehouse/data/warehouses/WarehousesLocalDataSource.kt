package io.ramani.ramaniWarehouse.data.warehouses

import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.warehouses.WarehousesDataSource
import io.ramani.ramaniWarehouse.domain.warehouses.models.GetWarehousesRequestModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.Single

class WarehousesLocalDataSource : WarehousesDataSource, BaseRemoteDataSource() {
    override fun getWarehouses(getWarehousesRequestModel: GetWarehousesRequestModel): Single<PagedList<WarehouseModel>> {
        TODO("Not yet implemented")
    }

    override fun getInvoices(getWarehousesRequestModel: GetWarehousesRequestModel): Single<PagedList<InvoiceModel>> {
        TODO("Not yet implemented")
    }


}