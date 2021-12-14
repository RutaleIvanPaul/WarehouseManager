package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.domain.auth.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.reactivex.Single
import okhttp3.RequestBody

class StockReceiveRepository(
    private val remoteStockReceiveDataSource: StockReceiveDataSource,
    private val localStockReceiveDataSource: StockReceiveDataSource
) : StockReceiveDataSource {

    override fun getSuppliers(companyId: String, page: Int, size: Int): Single<List<SupplierModel>> =
        remoteStockReceiveDataSource.getSuppliers(companyId, page, size)

    override fun getDeclineReasons(): Single<List<String>> =
        remoteStockReceiveDataSource.getDeclineReasons()

    override fun postGoodsReceived(bodyMaps: Map<String, RequestBody>): Single<GoodsReceivedModel> =
        remoteStockReceiveDataSource.postGoodsReceived(bodyMaps)

}