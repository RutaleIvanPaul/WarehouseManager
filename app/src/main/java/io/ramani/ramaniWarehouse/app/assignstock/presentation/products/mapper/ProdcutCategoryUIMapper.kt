package io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper

import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductCategoryUIModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.ProductCategory
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductCategoryEntity

class ProdcutCategoryUIMapper(): ModelMapper<ProductCategoryEntity, ProductCategoryUIModel> {
    override fun mapFrom(from: ProductCategoryEntity): ProductCategoryUIModel {
        return ProductCategoryUIModel(from._id, from.categoryId, from.name, from.unitPrice)
    }

    override fun mapTo(to: ProductCategoryUIModel): ProductCategoryEntity {
        return ProductCategoryEntity(to._id, to.categoryId, to.name, to.unitPrice)
    }
}