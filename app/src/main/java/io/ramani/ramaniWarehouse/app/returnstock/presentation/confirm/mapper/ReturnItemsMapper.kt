package io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.mapper

import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import io.ramani.ramaniWarehouse.data.returnStock.model.OfProducts
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper

class ReturnItemsMapper:ModelMapper<AvailableProductItem,OfProducts> {
    override fun mapFrom(from: AvailableProductItem): OfProducts =
        OfProducts(
            from.productId,
            from.productName,
            from.quantity,
            from.units
        )

    override fun mapTo(to: OfProducts): AvailableProductItem {
        TODO("Not yet implemented")
    }
}