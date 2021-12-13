package io.ramani.ramaniWarehouse.data.stockassignment.model

import com.google.gson.annotations.SerializedName


data class SalesPersonRemoteModel(
    @SerializedName("companyId")
    val companyId: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("isAdmin")
    val isAdmin: Boolean = false,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("phoneNumber")
    val phoneNumber: String = "",
    @SerializedName("userType")
    val userType: String = ""
)