package io.ramani.ramaniWarehouse.data.stockassignment.model

import com.google.gson.annotations.SerializedName

data class ReportsQueryRemoteModel(
    @SerializedName("headers")
    val headers: List<String> = listOf(),
    @SerializedName("rows")
    val rows: List<List<String>> = listOf()
)