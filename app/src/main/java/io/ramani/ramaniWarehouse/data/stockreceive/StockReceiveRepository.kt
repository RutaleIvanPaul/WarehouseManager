package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.domain.stockreceive.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.reactivex.Single

class StockReceiveRepository(
    private val remoteStockReceiveDataSource: StockReceiveDataSource,
    private val localStockReceiveDataSource: StockReceiveDataSource
) : StockReceiveDataSource {

    override fun getSuppliers(companyId: String, page: Int, size: Int): Single<List<SupplierModel>> =
        remoteStockReceiveDataSource.getSuppliers(companyId, page, size)

    override fun getDeclineReasons(): Single<List<String>> =
        remoteStockReceiveDataSource.getDeclineReasons()

    override fun postGoodsReceived(
        invoiceId: String,
        warehouseManagerId: String,
        warehouseId: String,
        distributorId: String,
        date: String,
        time: String,
        deliveryPersonName: String
    ): Single<GoodsReceivedModel> =
        remoteStockReceiveDataSource.postGoodsReceived(invoiceId, warehouseManagerId, warehouseId, distributorId, date, time, deliveryPersonName)
}