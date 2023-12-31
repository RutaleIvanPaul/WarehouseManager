package io.ramani.ramaniWarehouse.app.stockreceive.di

import io.ramani.ramaniWarehouse.data.stockreceive.mappers.GoodsReceivedItemRemoteMapper
import io.ramani.ramaniWarehouse.data.stockreceive.mappers.GoodsReceivedRemoteMapper
import io.ramani.ramaniWarehouse.data.stockreceive.mappers.SupplierProductRemoteMapper
import io.ramani.ramaniWarehouse.data.stockreceive.mappers.SupplierRemoteMapper
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.SupplierProductRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.SupplierRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.data.stockreceive.StockReceiveApi
import io.ramani.ramaniWarehouse.data.stockreceive.StockReceiveLocalDataSource
import io.ramani.ramaniWarehouse.data.stockreceive.StockReceiveRemoteDataSource
import io.ramani.ramaniWarehouse.data.stockreceive.StockReceiveRepository
import io.ramani.ramaniWarehouse.domain.stockreceive.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val stockReceiveDataModule = Kodein.Module("stockReceiveDataModule") {

    bind<StockReceiveApi>() with provider {
        ServiceHelper.createService<StockReceiveApi>(instance())
    }

    bind<StockReceiveDataSource>("stockReceiveDataSource") with singleton {
        StockReceiveRepository(
            instance("remoteStockReceiveDataSource"),
            instance("localStockReceiveDataSource")
        )
    }

    bind<StockReceiveDataSource>("remoteStockReceiveDataSource") with singleton {
        StockReceiveRemoteDataSource(
            instance(),
            instance(),
            instance()
        )
    }

    bind<StockReceiveDataSource>("localStockReceiveDataSource") with singleton {
        StockReceiveLocalDataSource(
            instance()
        )
    }

    // Supplier Mapper
    bind<ModelMapper<SupplierRemoteModel, SupplierModel>>() with provider {
        SupplierRemoteMapper(instance("SupplierProductRemoteMapper"))
    }

    bind<ModelMapper<SupplierProductRemoteModel, SupplierProductModel>>("SupplierProductRemoteMapper") with provider {
        SupplierProductRemoteMapper()
    }

    // Goods Received Mapper
    bind<ModelMapper<GoodsReceivedRemoteModel, GoodsReceivedModel>>() with provider {
        GoodsReceivedRemoteMapper(instance("GoodsReceivedItemRemoteMapper"))
    }

    bind<ModelMapper<GoodsReceivedItemRemoteModel, GoodsReceivedItemModel>>("GoodsReceivedItemRemoteMapper") with provider {
        GoodsReceivedItemRemoteMapper()
    }

}