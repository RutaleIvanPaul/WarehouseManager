package io.ramani.ramaniWarehouse.data.stockassignment.model

data class RemoteProductModel(
    val _id: String? = "",
    val archived: Boolean? = false,
    val commission: Int? = 0,
    val currency: String? = "",
    val externalId: String? = "",
    val hasSecondaryUnitConversion: Boolean? = false,
    val imagePath: String? = "",
    val name: String? = "",
    val productCategories: List<ProductCategory>? = listOf(),
    val rewards: List<Reward>? = listOf(),
    val secondaryUnitConversion: Int? = 0,
    val secondaryUnitName: String? = "",
    val supplierName: String? = "",
    val supplierProductName: String? = "",
    val units: String? = "",
    val vat: String? = "",
    val vatCategory: String? = "",
    val isAssigned: Boolean? = false,
    val supplierProductId: String? = null
)