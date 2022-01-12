package io.ramani.ramaniWarehouse.domain.stockreceive

import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.reactivex.Single
import java.io.File

interface StockReceiveDataSource {
    fun getSuppliers(companyId: String, page: Int, size: Int): Single<List<SupplierModel>>
    fun getDeclineReasons(): Single<List<String>>
    fun postGoodsReceived(
        invoiceId: String,
        warehouseManagerId: String,
        warehouseId: String,
        distributorId: String,
        date: String,
        time: String,
        deliveryPersonName: String,
        supplierId: String? = null,
        items: String? = null,
        storeKeeperSignature: File? = null,
        deliveryPersonSignature: File? = null
    ): Single<GoodsReceivedModel>

}