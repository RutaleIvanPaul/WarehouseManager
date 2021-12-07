package io.ramani.ramaniWarehouse.app.returnstock.di

import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.data.returnStock.ReturnStockApi
import io.ramani.ramaniWarehouse.data.returnStock.ReturnStockRemoteDataSource
import io.ramani.ramaniWarehouse.data.returnStock.ReturnStockRepository
import io.ramani.ramaniWarehouse.data.returnStock.mappers.SalespeopleRemoteMapper
import io.ramani.ramaniWarehouse.data.returnStock.model.SalespeopleRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.returnStock.ReturnStockDataSource
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val returnStockDataModule = Kodein.Module("returnStockDataModule"){
    bind<ReturnStockApi>() with provider {
        ServiceHelper.createService(instance())
    }

    bind<ReturnStockDataSource>("returnStockRemoteDataSource") with provider {
        ReturnStockRemoteDataSource(instance(), instance())
    }

    bind<ModelMapper<SalespeopleRemoteModel,SalespeopleModel>>() with provider {
        SalespeopleRemoteMapper()
    }

    bind<ReturnStockDataSource>("returnStockRepository") with provider {
        ReturnStockRepository(instance())
    }
}