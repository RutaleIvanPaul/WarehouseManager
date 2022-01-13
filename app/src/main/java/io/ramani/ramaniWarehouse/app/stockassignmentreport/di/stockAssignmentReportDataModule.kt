package io.ramani.ramaniWarehouse.app.stockstockAssignmentReport.di

import io.ramani.ramaniWarehouse.data.stockassignmentreport.StockAssignmentReportApi
import io.ramani.ramaniWarehouse.data.stockassignmentreport.StockAssignmentReportLocalDataSource
import io.ramani.ramaniWarehouse.data.stockassignmentreport.StockAssignmentReportRepository
import io.ramani.ramaniWarehouse.data.stockassignmentreport.mappers.StockAssignmentReportDistributorDateRemoteMapper
import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.StockAssignmentReportDistributorDateRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.data.stockStockAssignmentReport.StockStockAssignmentReportRemoteDataSource
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.StockAssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
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
}