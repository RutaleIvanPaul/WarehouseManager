package io.ramani.ramaniWarehouse.app.warehouses.di

import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.data.warehouses.WarehouseApi
import io.ramani.ramaniWarehouse.data.warehouses.WarehousesLocalDataSource
import io.ramani.ramaniWarehouse.data.warehouses.WarehousesRemoteDataSource
import io.ramani.ramaniWarehouse.data.warehouses.WarehousesRepository
import io.ramani.ramaniWarehouse.data.warehouses.mappers.WarehouseDimensionsRemoteModelMapper
import io.ramani.ramaniWarehouse.data.warehouses.mappers.WarehouseRemoteModelMapper
import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseDimensionsRemoteModel
import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.warehouses.WarehousesDataSource
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseDimensionsModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val warehousesDataModule = Kodein.Module("warehousesDataModule") {

    bind<WarehouseApi>() with provider {
        ServiceHelper.createService(instance())
    }

    bind<WarehousesDataSource>("warehousesDataSource") with provider {
        WarehousesRepository(
            instance("remoteWarehousesDataSource"),
            instance("localWarehousesDataSource")
        )
    }

    bind<WarehousesDataSource>("remoteWarehousesDataSource") with provider {
        WarehousesRemoteDataSource(
            instance(),
            instance()
        )
    }

    bind<WarehousesDataSource>("localWarehousesDataSource") with provider {
        WarehousesLocalDataSource()
    }

    bind<ModelMapper<WarehouseDimensionsRemoteModel, WarehouseDimensionsModel>>() with provider {
        WarehouseDimensionsRemoteModelMapper()
    }

    bind<ModelMapper<WarehouseRemoteModel, WarehouseModel>>() with provider {
        WarehouseRemoteModelMapper(instance())
    }
}