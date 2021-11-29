package io.ramani.ramaniWarehouse.data.warehouses.models

import com.google.gson.annotations.SerializedName

data class WarehouseRemoteModel(
    @SerializedName("_id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("category")
    var category: String? = null,

    @SerializedName("dimensions")
    var dimensions: WarehouseDimensionsRemoteModel? = null
)