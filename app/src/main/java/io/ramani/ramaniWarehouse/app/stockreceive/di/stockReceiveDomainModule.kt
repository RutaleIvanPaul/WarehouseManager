package io.ramani.ramaniWarehouse.app.stockreceive.di

import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRequestModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.auth.useCase.GetSupplierUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val stockReceiveDomainModule = Kodein.Module("stockReceiveDomainModule") {

    // Get Supplier
    bind<BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>>("getSupplierUseCase") with provider {
        GetSupplierUseCase(instance(), instance(), instance("stockReceiveDataSource"))
    }
}