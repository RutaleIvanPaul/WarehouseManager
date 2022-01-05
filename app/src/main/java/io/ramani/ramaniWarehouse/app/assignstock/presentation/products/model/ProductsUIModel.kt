package io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model

import io.ramani.ramaniWarehouse.data.stockassignment.model.AllProducts
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel

data class ProductsUIModel(
    val allProducts: List<RemoteProductModel>
)
