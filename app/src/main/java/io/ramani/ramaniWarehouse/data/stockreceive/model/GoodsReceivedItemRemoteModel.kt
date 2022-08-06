package io.ramani.ramaniWarehouse.data.stockreceive.model

import com.google.gson.annotations.SerializedName

data class GoodsReceivedItemRemoteModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("productId")
    val productId: String = "",
    @SerializedName("productName")
    val productName: String = "",
    @SerializedName("qtyAccepted")
    val qtyAccepted: Int = 0,
    @SerializedName("qtyDeclined")
    val qtyDeclined: Int = 0,
    @SerializedName("declinedReason")
    val declinedReason: String = "",
    @SerializedName("units")
    val units: String = "",
    @SerializedName("unitPrice")
    val unitPrice: Double = 0.0,
    @SerializedName("temperature")
    val temperature: Int = 0
)