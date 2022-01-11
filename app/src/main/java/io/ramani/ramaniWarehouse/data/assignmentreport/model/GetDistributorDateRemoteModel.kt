package io.ramani.ramaniWarehouse.data.assignmentreport.model

import com.google.gson.annotations.SerializedName
import io.ramani.ramaniWarehouse.data.auth.model.SupplierRemoteModel

data class GetDistributorDateRemoteModel(
    @SerializedName("docs")
    val stocks: List<DistributorDateRemoteModel> = ArrayList(),
    @SerializedName("totalDocs")
    val totalDocs: Int = 0,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean = false
)