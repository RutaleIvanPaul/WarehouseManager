package io.ramani.ramaniWarehouse.data.main.models

import com.google.gson.annotations.SerializedName

data class WarehouseRemoteModel(
    @SerializedName("_id")
    private val id: Int? = null,

    @SerializedName("name")
    private val name: String? = null,

    @SerializedName("category")
    private val category: String? = null,

    @SerializedName("dimensions")
    private val dimensions: WarehouseDimensionsRemoteModel? = null
)