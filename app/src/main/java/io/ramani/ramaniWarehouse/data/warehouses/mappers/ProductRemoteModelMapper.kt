package io.ramani.ramaniWarehouse.data.warehouses.mappers

import io.ramani.ramaniWarehouse.data.warehouses.models.ProductRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.warehouses.models.ProductModel

class ProductRemoteModelMapper : UniModelMapper<ProductRemoteModel, ProductModel> {
    override fun mapFrom(from: ProductRemoteModel): ProductModel =
        ProductModel.Builder()
            .productId(from.productId)
            .productName(from.productName)
            .price(from.price)
            .quantity(from.quantity)
            .unit(from.unit)
            .build()
}