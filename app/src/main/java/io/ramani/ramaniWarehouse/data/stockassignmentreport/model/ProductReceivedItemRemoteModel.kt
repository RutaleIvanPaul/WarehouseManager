package io.ramani.ramaniWarehouse.data.stockassignmentreport.model

import com.google.gson.annotations.SerializedName

data class ProductReceivedItemRemoteModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("productId")
    val productId: String = "",
    @SerializedName("productName")
    val productName: String = "",

    @SerializedName("quantity")
    val quantity: Int = 0,

    @SerializedName("units")
    val units: String = "",

)