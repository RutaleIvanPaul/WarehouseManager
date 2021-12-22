package io.ramani.ramaniWarehouse.data.assignmentreport.mappers

import io.ramani.ramaniWarehouse.data.assignmentreport.model.DistributorDateRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.auth.model.*
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import kotlin.collections.ArrayList

class DistributorDateRemoteMapper(
    private val goodsReceivedItemRemoteMapper: ModelMapper<GoodsReceivedItemRemoteModel, GoodsReceivedItemModel>,
) : ModelMapper<DistributorDateRemoteModel, DistributorDateModel> {
    override fun mapFrom(from: DistributorDateRemoteModel): DistributorDateModel {

        val items:ArrayList<GoodsReceivedItemModel> = ArrayList()
        for (remoteItem in from.items) {
            items.add(goodsReceivedItemRemoteMapper.mapFrom(remoteItem))
        }

        return DistributorDateModel.Builder()
                .id(from.id)
                .supplierName(from.supplierName)
                .date(from.date)
                .time(from.time)
                .items(items)
                .storeKeeperSignature(from.storeKeeperSignature)
                .deliveryPersonSignature(from.deliveryPersonSignature)
                .deliveryPersonName(from.deliveryPersonName)
                .warehouseManagerName(from.warehouseManagerName)
                .build()
        }

    override fun mapTo(to: DistributorDateModel): DistributorDateRemoteModel {
        val items:ArrayList<GoodsReceivedItemRemoteModel> = ArrayList()
        for (item in to.items) {
            items.add(goodsReceivedItemRemoteMapper.mapTo(item))
        }

        return DistributorDateRemoteModel(
                to.id,
                to.supplierName,
                to.date,
                to.time,
                items,
                to.deliveryPersonName,
                to.warehouseManagerName,
                to.supportingDocument,
                to.storeKeeperSignature,
                to.deliveryPersonSignature,
            )
    }

}