package io.ramani.ramaniWarehouse.data.stockreceive.model

import com.google.gson.annotations.SerializedName

data class SupplierProductRemoteModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("units")
    val units: String = "",
    @SerializedName("secondaryUnitName")
    val secondaryUnitName: String = "",
    @SerializedName("hasSecondaryUnitConversion")
    val hasSecondaryUnitConversion: Boolean = false,
)