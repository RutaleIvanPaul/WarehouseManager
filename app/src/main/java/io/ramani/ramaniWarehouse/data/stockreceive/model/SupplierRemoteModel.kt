package io.ramani.ramaniWarehouse.data.stockreceive.model

import com.google.gson.annotations.SerializedName
import io.ramani.ramaniWarehouse.data.stockreceive.model.SupplierProductRemoteModel

data class SupplierRemoteModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("products")
    val products: List<SupplierProductRemoteModel> = ArrayList(),
)