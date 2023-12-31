package io.ramani.ramaniWarehouse.app.returnstock.di


import io.ramani.ramaniWarehouse.data.returnStock.model.*
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domain.returnStock.useCase.GetAvailableStockUsecase
import io.ramani.ramaniWarehouse.domain.returnStock.useCase.GetSalesPeopleUsecase
import io.ramani.ramaniWarehouse.domain.returnStock.useCase.PostReturnedStockUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val returnStockDomainModule = Kodein.Module("returnStockDomainModule"){

    bind<BaseSingleUseCase<List<SalespeopleModel>, GetSalespeopleRequestModel>>("getSalespeopleUseCase") with provider {
        GetSalesPeopleUsecase(instance(), instance(),instance("returnStockRemoteDataSource"))
    }

    bind<BaseSingleUseCase<List<AvailableStockReturnedListItem>, GetAvailableStockRequestModel>>("getAvailableStockUseCase") with provider {
        GetAvailableStockUsecase(
            instance(), instance(),instance("returnStockRemoteDataSource")
        )
    }

    bind<BaseSingleUseCase<PostReturnItemsResponse, PostReturnItems>>("postReturnedStockUseCase")with provider{
        PostReturnedStockUseCase(
            instance(),instance(),instance("returnStockRemoteDataSource")
        )
    }
}