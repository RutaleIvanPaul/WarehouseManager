package io.ramani.ramaniWarehouse.data.returnStock.model


import com.google.gson.annotations.SerializedName

data class AvailableStockReturnedListItem(
    @SerializedName("companyId")
    val companyId: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("lastUpdated")
    val lastUpdated: String = "",
    @SerializedName("products")
    val products: List<AvailableProductItem> = listOf(),
    @SerializedName("salesPersonUID")
    val salesPersonUID: String = "",
    @SerializedName("__v")
    val v: Int = 0,
    @SerializedName("warehouseId")
    val warehouseId: String = ""
)