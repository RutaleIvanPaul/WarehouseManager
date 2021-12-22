package io.ramani.ramaniWarehouse.data.auth.mappers

import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import kotlin.collections.ArrayList

class GoodsReceivedRemoteMapper(
    private val goodsReceivedItemRemoteMapper: ModelMapper<GoodsReceivedItemRemoteModel, GoodsReceivedItemModel>,
) : ModelMapper<GoodsReceivedRemoteModel, GoodsReceivedModel> {
    override fun mapFrom(from: GoodsReceivedRemoteModel): GoodsReceivedModel {

        val items:ArrayList<GoodsReceivedItemModel> = ArrayList()
        for (remoteItem in from.items) {
            items.add(goodsReceivedItemRemoteMapper.mapFrom(remoteItem))
        }

        return GoodsReceivedModel.Builder()
                .id(from.id)
                .invoiceId(from.invoiceId)
                .distributorId(from.distributorId)
                .supplierId(from.supplierId)
                .warehouseId(from.warehouseId)
                .warehouseManagerId(from.warehouseManagerId)
                .date(from.date)
                .time(from.time)
                .deliveryPersonName(from.deliveryPersonName)
                .supportingDocument(from.supportingDocument)
                .storeKeeperSignature(from.storeKeeperSignature)
                .deliveryPersonSignature(from.deliveryPersonSignature)
                .items(items)
                .build()
    }

    override fun mapTo(to: GoodsReceivedModel): GoodsReceivedRemoteModel {
        val items:ArrayList<GoodsReceivedItemRemoteModel> = ArrayList()
        for (item in to.items) {
            items.add(goodsReceivedItemRemoteMapper.mapTo(item))
        }

        return GoodsReceivedRemoteModel(
                to.id,
                to.invoiceId,
                to.distributorId,
                to.supplierId,
                to.warehouseId,
                to.warehouseManagerId,
                to.date,
                to.time,
                to.deliveryPersonName,
                to.supportingDocument,
                to.storeKeeperSignature,
                to.deliveryPersonSignature,
                items
            )
    }

}