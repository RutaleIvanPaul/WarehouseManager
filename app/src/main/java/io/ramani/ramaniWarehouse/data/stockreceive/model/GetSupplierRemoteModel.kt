package io.ramani.ramaniWarehouse.data.auth.model

import com.google.gson.annotations.SerializedName

data class GetSupplierRemoteModel(
    @SerializedName("docs")
    val suppliers: List<SupplierRemoteModel> = ArrayList(),
    @SerializedName("totalDocs")
    val totalDocs: Int = 0,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean = false
)