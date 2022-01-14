package io.ramani.ramaniWarehouse.data.stockassignment.model

data class AllProducts(
    val message: String,
    val remoteProductModel: List<RemoteProductModel>,
    val status: Int
)