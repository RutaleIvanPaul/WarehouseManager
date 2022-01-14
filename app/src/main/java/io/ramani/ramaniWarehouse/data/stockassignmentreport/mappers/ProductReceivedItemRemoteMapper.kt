package io.ramani.ramaniWarehouse.data.stockassignmentreport.mappers

import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.ProductReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.ProductReceivedItemModel

class ProductReceivedItemRemoteMapper : ModelMapper<ProductReceivedItemRemoteModel, ProductReceivedItemModel> {
    override fun mapFrom(from: ProductReceivedItemRemoteModel): ProductReceivedItemModel =
        ProductReceivedItemModel.Builder()
            .id(from.id)
            .productId(from.productId)
            .productName(from.productName)
            .quantity(from.quantity)
            .units(from.units)
            .build()

    override fun mapTo(to: ProductReceivedItemModel): ProductReceivedItemRemoteModel =
        ProductReceivedItemRemoteModel(
            to.id,
            to.productId,
            to.productName,
            to.quantity,
            to.units
        )
}