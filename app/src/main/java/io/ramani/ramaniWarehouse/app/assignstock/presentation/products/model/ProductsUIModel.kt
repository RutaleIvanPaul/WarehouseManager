package io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model

import com.google.gson.annotations.SerializedName
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.data.stockassignment.model.AllProducts
import io.ramani.ramaniWarehouse.data.stockassignment.model.ProductCategory
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.Reward

data class ProductsUIModel(
    val _id: String,
    val archived: Boolean,
    val commission: Int,
    val currency: String,
    val externalId: String,
    val hasSecondaryUnitConversion: Boolean,
    val imagePath: String,
    val name: String,
    val productCategories: List<ProductCategoryUIModel>,
    val rewards: List<RewardUIModel>,
    val secondaryUnitConversion: Int,
    val secondaryUnitName: String,
    val supplierName: String,
    val supplierProductName: String,
    val units: String,
    val vat: String,
    val vatCategory: String,
    var displayText: String? = "Assign",
    var selectedUnits: String? = null,
    var isAssigned: Boolean? = false,
    var assignedNumber: Int? = 0,
    var assignedResource: Int? = 0,
    var assignedResourceID: Int? = R.drawable.button_round_coral_green,
    val supplierProductId: String? = null
)
