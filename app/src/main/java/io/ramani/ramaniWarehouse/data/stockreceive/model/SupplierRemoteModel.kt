package io.ramani.ramaniWarehouse.data.auth.model

import com.google.gson.annotations.SerializedName

data class SupplierRemoteModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("products")
    val products: List<SupplierProductRemoteModel> = ArrayList(),
)