package io.ramani.ramaniWarehouse.domain.stockassignment.model

data class ProductCategoryEntity(
    val _id: String,
    val categoryId: String,
    val name: String,
    val unitPrice: Double
)