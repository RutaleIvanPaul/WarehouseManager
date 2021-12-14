package io.ramani.ramaniWarehouse.data.warehouses.models

import com.google.gson.annotations.SerializedName

data class ProductRemoteModel(
    @SerializedName("productId")
     val productId: String? = null,
    @SerializedName("productName")
     val productName: String? = null,
    @SerializedName("price")
     val price: Double? = null,
    @SerializedName("qty")
     val quantity: Double? = null,
    @SerializedName("unit")
    val unit: String? = null
)