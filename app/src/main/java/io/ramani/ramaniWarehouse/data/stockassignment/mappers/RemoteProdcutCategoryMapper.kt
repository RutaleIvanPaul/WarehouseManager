package io.ramani.ramaniWarehouse.data.stockassignment.mappers

import io.ramani.ramaniWarehouse.data.stockassignment.model.ProductCategory
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductCategoryEntity

class RemoteProdcutCategoryMapper(): ModelMapper<ProductCategory, ProductCategoryEntity> {
    override fun mapFrom(from: ProductCategory): ProductCategoryEntity {
        return ProductCategoryEntity(from._id, from.categoryId, from.name, from.unitPrice)
    }

    override fun mapTo(to: ProductCategoryEntity): ProductCategory {
        return ProductCategory(to._id, to.categoryId, to.name, to.unitPrice)
    }
}