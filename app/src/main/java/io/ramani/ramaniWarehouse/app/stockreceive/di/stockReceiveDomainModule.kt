package io.ramani.ramaniWarehouse.app.stockreceive.di

import io.ramani.ramaniWarehouse.data.stockreceive.model.GetSupplierRequestModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.stockreceive.useCase.GetDeclineReasonsUseCase
import io.ramani.ramaniWarehouse.domain.stockreceive.useCase.GetSupplierUseCase
import io.ramani.ramaniWarehouse.domain.stockreceive.useCase.PostGoodsReceivedUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val stockReceiveDomainModule = Kodein.Module("stockReceiveDomainModule") {

    // Get Supplier
    bind<BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>>("getSupplierUseCase") with provider {
        GetSupplierUseCase(instance(), instance(), instance("stockReceiveDataSource"))
    }

    bind<BaseSingleUseCase<List<String>, Params>>("getDeclineReasonsUseCase") with provider {
        GetDeclineReasonsUseCase(instance(), instance(), instance("stockReceiveDataSource"))
    }

    bind<BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>>("postGoodsReceivedUseCase") with provider {
        PostGoodsReceivedUseCase(instance(), instance(), instance("stockReceiveDataSource"))
    }

}