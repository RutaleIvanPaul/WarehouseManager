package io.ramani.ramaniWarehouse.data.returnStock.model


import com.google.gson.annotations.SerializedName

data class AvailableProductItem(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("productId")
    val productId: String = "",
    @SerializedName("productName")
    val productName: String = "",
    @SerializedName("quantity")
    var quantity: Int = 0,
    @SerializedName("secondaryUnitConversion")
    val secondaryUnitConversion: Int = 0,
    @SerializedName("secondaryUnitQuantity")
    val secondaryUnitQuantity: Int = 0,
    @SerializedName("secondaryUnits")
    val secondaryUnits: String = "",
    @SerializedName("units")
    val units: String = "",
    @SerializedName("supplierProductId")
    val supplierProductId: String? = null

)