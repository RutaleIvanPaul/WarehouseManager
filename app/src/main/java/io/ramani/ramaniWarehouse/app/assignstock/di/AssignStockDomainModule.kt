package io.ramani.ramaniWarehouse.app.assignstock.di

import io.ramani.ramaniWarehouse.data.stockassignment.model.GetSalesPersonRequestModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.ramani.ramaniWarehouse.domain.stockassignment.usecases.GetSalesPersonUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val assignStockDomainModule = Kodein.Module("assignStockDomainModule") {

    bind<BaseSingleUseCase<List<SalesPersonModel>, GetSalesPersonRequestModel>>("getSalesPersonUseCase") with provider {
        GetSalesPersonUseCase(instance(), instance(), instance("assignStockRemoteDataSource"))
    }
}