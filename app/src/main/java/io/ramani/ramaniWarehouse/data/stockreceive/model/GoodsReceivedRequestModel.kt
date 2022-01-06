package io.ramani.ramaniWarehouse.data.stockreceive.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params
import okhttp3.RequestBody

data class GoodsReceivedRequestModel(
    val invoiceId: String,
    val warehouseManagerId: String,
    val warehouseId: String,
    val distributorId: String,
    val date: String,
    val time: String,
    val deliveryPersonName: String,
):Params {

}