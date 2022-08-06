package io.ramani.ramaniWarehouse.data.stockassignment.model

import com.google.gson.annotations.SerializedName


data class ConfirmProducts(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int? = 0,
    val units: String? = "",
    val supplierProductId: String? = null
)