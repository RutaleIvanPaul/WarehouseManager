package io.ramani.ramaniWarehouse.domain.stockassignment.model

data class ProductEntity(
    val _id: String,
    val archived: Boolean,
    val commission: Int,
    val currency: String,
    val externalId: String,
    val hasSecondaryUnitConversion: Boolean,
    val imagePath: String,
    val name: String,
    val productCategoryEntities: List<ProductCategoryEntity>,
    val rewardEntities: List<RewardEntity>,
    val secondaryUnitConversion: Int,
    val secondaryUnitName: String,
    val supplierName: String,
    val supplierProductName: String,
    val units: String,
    val vat: String,
    val vatCategory: String,
    val supplierProductId: String? = null
)