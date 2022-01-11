package io.ramani.ramaniWarehouse.data.stockreceive.mappers

import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper

class GoodsReceivedItemRemoteMapper : ModelMapper<GoodsReceivedItemRemoteModel, GoodsReceivedItemModel> {
    override fun mapFrom(from: GoodsReceivedItemRemoteModel): GoodsReceivedItemModel =
        GoodsReceivedItemModel.Builder()
            .id(from.id)
            .productId(from.productId)
            .productName(from.productName)
            .qtyAccepted(from.qtyAccepted)
            .qtyDeclined(from.qtyDeclined)
            .declinedReason(from.declinedReason)
            .temperature(from.temperature)
            .build()

    override fun mapTo(to: GoodsReceivedItemModel): GoodsReceivedItemRemoteModel =
        GoodsReceivedItemRemoteModel(
            to.id,
            to.productId,
            to.productName,
            to.qtyAccepted,
            to.qtyDeclined,
            to.declinedReason,
            to.temperature
        )
}