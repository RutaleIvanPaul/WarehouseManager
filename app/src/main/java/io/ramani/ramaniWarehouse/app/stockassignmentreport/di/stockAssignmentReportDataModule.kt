package io.ramani.ramaniWarehouse.app.stockstockAssignmentReport.di

import io.ramani.ramaniWarehouse.data.stockassignmentreport.StockAssignmentReportApi
import io.ramani.ramaniWarehouse.data.stockassignmentreport.StockAssignmentReportLocalDataSource
import io.ramani.ramaniWarehouse.data.stockassignmentreport.StockAssignmentReportRepository
import io.ramani.ramaniWarehouse.data.stockassignmentreport.mappers.StockAssignmentReportDistributorDateRemoteMapper
import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.StockAssignmentReportDistributorDateRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.data.stockStockAssignmentReport.StockStockAssignmentReportRemoteDataSource
import io.ramani.ramaniWarehouse.data.stockassignmentreport.mappers.ProductReceivedItemRemoteMapper
import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.ProductReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.mappers.GoodsReceivedItemRemoteMapper
import io.ramani.ramaniWarehouse.data.stockreceive.mappers.GoodsReceivedRemoteMapper
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRemoteModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.StockAssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.ProductReceivedItemModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val stockAssignmentReportDataModule = Kodein.Module("stockAssignmentReportDataModule") {

    bind<StockAssignmentReportApi>() with provider {
        ServiceHelper.createService<StockAssignmentReportApi>(instance())
    }

    bind<StockAssignmentReportDataSource>("stockAssignmentReportDataSource") with singleton {
        StockAssignmentReportRepository(
            instance("remoteStockAssignmentReportDataSource"),
            instance("localStockAssignmentReportDataSource")
        )
    }

    bind<StockAssignmentReportDataSource>("remoteStockAssignmentReportDataSource") with singleton {
        StockStockAssignmentReportRemoteDataSource(
            instance(),
            instance(),
            instance()
        )
    }

    bind<StockAssignmentReportDataSource>("localStockAssignmentReportDataSource") with singleton {
        StockAssignmentReportLocalDataSource(
            instance()
        )
    }

    bind<ModelMapper<StockAssignmentReportDistributorDateRemoteModel, StockAssignmentReportDistributorDateModel>>() with provider {
        StockAssignmentReportDistributorDateRemoteMapper(instance("GoodsReceivedItemRemoteMapper"))
    }

    // Goods Received Mapper
    bind<ModelMapper<ProductReceivedItemRemoteModel, ProductReceivedItemModel>>("GoodsReceivedItemRemoteMapper") with provider {
        ProductReceivedItemRemoteMapper()
    }

}