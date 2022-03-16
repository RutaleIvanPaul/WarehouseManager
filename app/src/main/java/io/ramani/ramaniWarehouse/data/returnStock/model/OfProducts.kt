package io.ramani.ramaniWarehouse.data.returnStock.model



data class OfProducts(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val units: String = "",
    val supplierProductId: String? = null
)