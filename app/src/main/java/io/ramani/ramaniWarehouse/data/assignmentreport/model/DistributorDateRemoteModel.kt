package io.ramani.ramaniWarehouse.data.assignmentreport.model

import com.google.gson.annotations.SerializedName
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedItemRemoteModel

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

    @SerializedName("deliveryPersonName")
    val deliveryPersonName: String = "",
    @SerializedName("warehouseManagerName")
    val warehouseManagerName: String = "",

    @SerializedName("supportingDocument")
    val supportingDocument: List<String> = ArrayList(),
    @SerializedName("storeKeeperSignature")
    val storeKeeperSignature: List<String> = ArrayList(),
    @SerializedName("deliveryPersonSignature")
    val deliveryPersonSignature: List<String> = ArrayList(),
)