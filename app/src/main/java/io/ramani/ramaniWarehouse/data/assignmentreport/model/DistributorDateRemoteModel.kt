package io.ramani.ramaniWarehouse.data.auth.model

import com.google.gson.annotations.SerializedName

data class DistributorDateRemoteModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("supplierName")
    val supplierName: String = "",
    @SerializedName("date")
    val date: String = "",
    @SerializedName("time")
    val time: String = "",

    @SerializedName("items")
    val items: List<GoodsReceivedItemRemoteModel> = ArrayList(),

    @SerializedName("storeKeeperSignature")
    val storeKeeperSignature: String = "",
    @SerializedName("deliveryPersonSignature")
    val deliveryPersonSignature: String = "",

    @SerializedName("deliveryPersonName")
    val deliveryPersonName: String = "",
    @SerializedName("warehouseManagerName")
    val warehouseManagerName: String = "",

)