package io.ramani.ramaniWarehouse.app.assignstock.di

import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.data.stockassignment.model.*
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.returnStock.useCase.PostReturnedStockUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.ramani.ramaniWarehouse.domain.stockassignment.usecases.GetCompanyProductsUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.usecases.GetSalesPersonUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.usecases.PostAssignedStockUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val assignStockDomainModule = Kodein.Module("assignStockDomainModule") {

    bind<BaseSingleUseCase<List<SalesPersonModel>, GetSalesPersonRequestModel>>("getSalesPersonUseCase") with provider {
        GetSalesPersonUseCase(instance(), instance(), instance("assignStockRepository"))
    }

    bind<BaseSingleUseCase<List<ProductEntity>, GetProductsRequestModel>>("getCompanyProductsUseCase") with provider {
        GetCompanyProductsUseCase(
            instance(), instance(),instance("assignStockRepository")
        )
    }

    bind<BaseSingleUseCase<PostAssignedItemsResponse, AssignProductsRequestModel>>("postAssignedStockUseCase")with provider{
        PostAssignedStockUseCase(
            instance(),instance(),instance("assignStockRepository")
        )
    }
}