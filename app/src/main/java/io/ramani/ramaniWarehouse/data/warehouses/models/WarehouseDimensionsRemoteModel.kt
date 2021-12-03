package io.ramani.ramaniWarehouse.data.warehouses.models

import com.google.gson.annotations.SerializedName

data class WarehouseDimensionsRemoteModel(
    @SerializedName("height")
    val height: Int? = null,

    @SerializedName("width")
    val width: Int? = null,

    @SerializedName("length")
    val length: Int? = null
)