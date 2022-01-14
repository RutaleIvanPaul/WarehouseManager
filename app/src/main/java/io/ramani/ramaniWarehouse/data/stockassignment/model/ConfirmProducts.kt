package io.ramani.ramaniWarehouse.data.stockassignment.model



data class ConfirmProducts(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int? = 0,
    val units: String? = ""
)