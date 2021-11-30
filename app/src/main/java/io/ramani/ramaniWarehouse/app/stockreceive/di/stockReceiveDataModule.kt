package io.ramani.ramaniWarehouse.app.stockreceive.di

import io.ramani.ramaniWarehouse.data.auth.*
import io.ramani.ramaniWarehouse.data.auth.mappers.SupplierProductRemoteMapper
import io.ramani.ramaniWarehouse.data.auth.mappers.SupplierRemoteMapper
import io.ramani.ramaniWarehouse.data.auth.model.SupplierProductRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.SupplierRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.domain.auth.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierProductModel
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

}