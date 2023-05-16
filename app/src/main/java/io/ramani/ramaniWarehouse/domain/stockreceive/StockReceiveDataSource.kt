package io.ramani.ramaniWarehouse.domain.stockreceive

import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
import io.reactivex.Single
import okhttp3.RequestBody

interface StockReceiveDataSource {
    fun getSuppliers(companyId: String, page: Int, size: Int): Single<List<SupplierModel>>
    fun getDeclineReasons(): Single<List<String>>
    fun postGoodsReceived(body: RequestBody): Single<GoodsReceivedModel>
    fun putMoreSupportingDocs(body: RequestBody): Single<String>

}