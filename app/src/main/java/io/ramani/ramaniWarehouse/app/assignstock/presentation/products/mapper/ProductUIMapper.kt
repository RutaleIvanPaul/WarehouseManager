package io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper

import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.AllProducts
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper

class ProductUIMapper: ModelMapper<AllProducts, ProductsUIModel>{
    override fun mapFrom(from: AllProducts): ProductsUIModel = ProductsUIModel(from.remoteProductModel)

    override fun mapTo(to: ProductsUIModel): AllProducts {
        TODO("Not yet implemented")
    }
}