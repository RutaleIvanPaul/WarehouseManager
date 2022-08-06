package io.ramani.ramaniWarehouse.data.stockassignmentreport.mappers

import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.ProductReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.StockAssignmentReportDistributorDateRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.ProductReceivedItemModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import kotlin.collections.ArrayList

class StockAssignmentReportDistributorDateRemoteMapper(
    private val goodsReceivedItemRemoteMapper: ModelMapper<ProductReceivedItemRemoteModel, ProductReceivedItemModel>,
) : ModelMapper<StockAssignmentReportDistributorDateRemoteModel, StockAssignmentReportDistributorDateModel> {
    override fun mapFrom(from: StockAssignmentReportDistributorDateRemoteModel): StockAssignmentReportDistributorDateModel {

        val items:ArrayList<ProductReceivedItemModel> = ArrayList()
        for (remoteItem in from.listOfProducts) {
            items.add(goodsReceivedItemRemoteMapper.mapFrom(remoteItem))
        }

        return StockAssignmentReportDistributorDateModel.Builder()
            .id(from.id)
            .assigner(from.assigner)
            .dateStockTaken(from.dateStockTaken)
            .timeStamp(from.timestamp)
            .listOfProducts(items)
            .name(from.name)
            .storeKeeperSignature(from.storeKeeperSignature)
            .salesPersonSignature(from.salesPersonSignature)
            .salesPersonUUID(from.salesPersonUID)
            .stockAssignmentType(from.stockAssignmentType)
            .build()
        }

    override fun mapTo(to: StockAssignmentReportDistributorDateModel): StockAssignmentReportDistributorDateRemoteModel {
        val items:ArrayList<ProductReceivedItemRemoteModel> = ArrayList()
        for (item in to.listOfProducts) {
            items.add(goodsReceivedItemRemoteMapper.mapTo(item))
        }

        return StockAssignmentReportDistributorDateRemoteModel(
                to.id,
                to.__v,
                to.assigner,
                to.comment,
                to.companyId,
                to.dateStockTaken,
                items,
                to.name,
                to.salesPersonUID,
                to.stockAssignmentType,
                to.timestamp,
                to.storeKeeperSignature,
                to.salesPersonSignature,
            )
    }
}