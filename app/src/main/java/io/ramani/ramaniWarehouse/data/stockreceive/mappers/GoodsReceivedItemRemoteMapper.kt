package io.ramani.ramaniWarehouse.data.auth.mappers

import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.SupplierProductRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper

class GoodsReceivedItemRemoteMapper : ModelMapper<GoodsReceivedItemRemoteModel, GoodsReceivedItemModel> {
    override fun mapFrom(from: GoodsReceivedItemRemoteModel): GoodsReceivedItemModel =
        GoodsReceivedItemModel.Builder()
            .id(from.id)
            .productId(from.productId)
            .qtyAccepted(from.qtyAccepted)
            .qtyDeclined(from.qtyDeclined)
            .declinedReason(from.declinedReason)
            .temperature(from.temperature)
            .build()

    override fun mapTo(to: GoodsReceivedItemModel): GoodsReceivedItemRemoteModel =
        GoodsReceivedItemRemoteModel(
            to.id,
            to.productId,
            to.qtyAccepted,
            to.qtyDeclined,
            to.declinedReason,
            to.temperature
        )
}