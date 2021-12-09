package io.ramani.ramaniWarehouse.app.returnstock.di

import io.ramani.ramaniWarehouse.data.returnStock.model.GetSalespeopleRequestModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domain.returnStock.useCase.GetSalesPeopleUsecase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val returnStockDomainModule = Kodein.Module("returnStockDomainModule"){

    bind<BaseSingleUseCase<List<SalespeopleModel>, GetSalespeopleRequestModel>>("getSalespeopleUseCase") with provider {
        GetSalesPeopleUsecase(instance(), instance(),instance("returnStockRemoteDataSource"))
    }
}