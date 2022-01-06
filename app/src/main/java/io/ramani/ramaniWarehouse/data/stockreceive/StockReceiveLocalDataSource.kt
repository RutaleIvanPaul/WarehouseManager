package io.ramani.ramaniWarehouse.data.stockreceive

import io.ramani.ramaniWarehouse.domainCore.prefs.Prefs
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.domain.stockreceive.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
import io.reactivex.Single

class StockReceiveLocalDataSource(
    private val prefsManager: Prefs
) : StockReceiveDataSource, BaseRemoteDataSource() {
    override  fun getSuppliers(companyId: String, page: Int, size: Int): Single<List<SupplierModel>> {
        TODO("Not yet implemented")
    }

    override fun getDeclineReasons(): Single<List<String>> {
        TODO("Not yet implemented")
    }

    override fun postGoodsReceived(
        invoiceId: String,
        warehouseManagerId: String,
        warehouseId: String,
        distributorId: String,
        date: String,
        time: String,
        deliveryPersonName: String
    ): Single<GoodsReceivedModel> {
        TODO("Not yet implemented")
    }


}