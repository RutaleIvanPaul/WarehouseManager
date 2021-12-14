package io.ramani.ramaniWarehouse.domain.auth

import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.reactivex.Single
import okhttp3.RequestBody

interface StockReceiveDataSource {
    fun getSuppliers(companyId: String, page: Int, size: Int): Single<List<SupplierModel>>
    fun getDeclineReasons(): Single<List<String>>
    fun postGoodsReceived(bodyMaps: Map<String, RequestBody>): Single<GoodsReceivedModel>

}