package io.ramani.ramaniWarehouse.data.stockreceive

import io.ramani.ramaniWarehouse.domain.stockreceive.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
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

    override fun postGoodsReceived(body: RequestBody): Single<GoodsReceivedModel> =
        remoteStockReceiveDataSource.postGoodsReceived(body)

    override fun putMoreSupportingDocs(body: RequestBody): Single<String>  =
        remoteStockReceiveDataSource.putMoreSupportingDocs(body)
}