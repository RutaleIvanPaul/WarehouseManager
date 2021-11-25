package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.reactivex.Single

class StockReceiveRepository(
    private val remoteStockReceiveDataSource: StockReceiveDataSource,
    private val localStockReceiveDataSource: StockReceiveDataSource
) : StockReceiveDataSource {

    override fun getSuppliers(companyId: String, page: Int, size: Int): Single<List<SupplierModel>> =
        remoteStockReceiveDataSource.getSuppliers(companyId, page, size)

}