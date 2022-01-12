package io.ramani.ramaniWarehouse.data.stockreceive.model

import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import java.io.File

data class GoodsReceivedRequestModel(
    val invoiceId: String,
    val warehouseManagerId: String,
    val warehouseId: String,
    val distributorId: String,
    val date: String,
    val time: String,
    val deliveryPersonName: String,
    val supplierId: String? = null,
    val items: List<ProductModelView>? = null,
    val storeKeeperSignature: File? = null,
    val deliveryPersonSignature: File? = null
) : Params {

}