package io.ramani.ramaniWarehouse.data.assignmentreport.model

import com.google.gson.annotations.SerializedName

data class GetDistributorDateRemoteModel(
    @SerializedName("docs")
    val stocks: List<DistributorDateRemoteModel> = ArrayList(),
    @SerializedName("totalDocs")
    val totalDocs: Int = 0,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean = false
)