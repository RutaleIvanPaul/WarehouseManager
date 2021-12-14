package io.ramani.ramaniWarehouse.data.auth.model

import com.google.gson.annotations.SerializedName

data class GoodsReceivedItemRemoteModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("productId")
    val productId: String = "",
    @SerializedName("qtyAccepted")
    val qtyAccepted: Int = 0,
    @SerializedName("qtyDeclined")
    val qtyDeclined: Int = 0,
    @SerializedName("declinedReason")
    val declinedReason: String = "",
    @SerializedName("temperature")
    val temperature: Int = 0
)