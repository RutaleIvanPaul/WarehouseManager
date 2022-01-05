package io.ramani.ramaniWarehouse.data.stockassignment.model

data class RemoteProductModel(
    val _id: String,
    val archived: Boolean,
    val commission: Int,
    val currency: String,
    val externalId: String,
    val hasSecondaryUnitConversion: Boolean,
    val imagePath: String,
    val name: String,
    val productCategories: List<ProductCategory>,
    val rewards: List<Reward>,
    val secondaryUnitConversion: Int,
    val secondaryUnitName: String,
    val supplierName: String,
    val supplierProductName: String,
    val units: String,
    val vat: String,
    val vatCategory: String
)