package io.ramani.ramaniWarehouse.data.stockreceive.model

import com.google.gson.annotations.SerializedName
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedItemRemoteModel

data class GoodsReceivedRemoteModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("invoiceId")
    val invoiceId: String = "",
    @SerializedName("distributorId")
    val distributorId: String = "",
    @SerializedName("supplierId")
    val supplierId: String = "",
    @SerializedName("warehouseId")
    val warehouseId: String = "",
    @SerializedName("warehouseManagerId")
    val warehouseManagerId: String = "",
    @SerializedName("deliveryPersonName")
    val deliveryPersonName: String = "",
    @SerializedName("date")
    val date: String = "",
    @SerializedName("time")
    val time: String = "",

    @SerializedName("supportingDocument")
    val supportingDocument: List<String> = ArrayList(),
    @SerializedName("storeKeeperSignature")
    val storeKeeperSignature: List<String> = ArrayList(),
    @SerializedName("deliveryPersonSignature")
    val deliveryPersonSignature: List<String> = ArrayList(),
    @SerializedName("items")
    val items: List<GoodsReceivedItemRemoteModel> = ArrayList()
)