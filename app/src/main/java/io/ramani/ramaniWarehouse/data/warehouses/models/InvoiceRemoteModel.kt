package io.ramani.ramaniWarehouse.data.warehouses.models

import com.google.gson.annotations.SerializedName

data class InvoiceRemoteModel(

    @SerializedName("invoiceId")
    val invoiceId: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("distributorName")
    val distributorName: String? = null,
    @SerializedName("supplierName")
    val supplierName: String? = null,
    @SerializedName("invoiceAmount")
    val invoiceAmount: Double? = null,
    @SerializedName("products")
    val products: List<ProductRemoteModel>? = null
)