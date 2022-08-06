package io.ramani.ramaniWarehouse.app.stockreport.di

import io.ramani.ramaniWarehouse.data.stockreport.StockReportApi
import io.ramani.ramaniWarehouse.data.stockreport.StockReportLocalDataSource
import io.ramani.ramaniWarehouse.data.stockreport.StockReportRemoteDataSource
import io.ramani.ramaniWarehouse.data.stockreport.StockReportRepository
import io.ramani.ramaniWarehouse.data.stockreport.mappers.DistributorDateRemoteMapper
import io.ramani.ramaniWarehouse.data.stockreport.model.DistributorDateRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.domain.stockreport.StockReportDataSource
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val stockReportDataModule = Kodein.Module("stockReportDataModule") {

    bind<StockReportApi>() with provider {
        ServiceHelper.createService<StockReportApi>(instance())
    }

    bind<StockReportDataSource>("stockReportDataSource") with singleton {
        StockReportRepository(
            instance("remoteStockReportDataSource"),
            instance("localStockReportDataSource")
        )
    }

    bind<StockReportDataSource>("remoteStockReportDataSource") with singleton {
        StockReportRemoteDataSource(
            instance(),
            instance()
        )
    }

    bind<StockReportDataSource>("localStockReportDataSource") with singleton {
        StockReportLocalDataSource(
            instance()
        )
    }

    bind<ModelMapper<DistributorDateRemoteModel, DistributorDateModel>>() with provider {
        DistributorDateRemoteMapper(instance("GoodsReceivedItemRemoteMapper"))
    }
}