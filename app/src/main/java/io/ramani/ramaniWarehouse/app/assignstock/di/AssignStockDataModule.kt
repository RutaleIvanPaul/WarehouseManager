package io.ramani.ramaniWarehouse.app.assignstock.di

import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.data.stockassignment.AssignStockAPI
import io.ramani.ramaniWarehouse.data.stockassignment.AssignStockRemoteDataSource
import io.ramani.ramaniWarehouse.data.stockassignment.AssignStockRepository
import io.ramani.ramaniWarehouse.data.stockassignment.mappers.SalesPersonRemoteMapper
import io.ramani.ramaniWarehouse.data.stockassignment.model.SalesPersonRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val assignStockDataModule = Kodein.Module("assignStockDataModule") {
    bind<AssignStockAPI>() with provider {
        ServiceHelper.createService(instance())
    }

    bind<AssignStockDataSource>("assignStockRemoteDataSource") with provider {
        AssignStockRemoteDataSource(instance(), instance(), instance())
    }

    bind<ModelMapper<SalesPersonRemoteModel, SalesPersonModel>>() with provider {
        SalesPersonRemoteMapper()
    }

    bind<AssignStockDataSource>("assignStockRepository") with provider {
        AssignStockRepository(instance())
    }
}