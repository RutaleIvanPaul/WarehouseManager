package io.ramani.ramaniWarehouse.app.stockreport.di

import io.ramani.ramaniWarehouse.data.stockreport.model.DistributorDateRequestModel
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockreport.usecase.GetDistributorDateUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val stockReportDomainModule = Kodein.Module("stockReportDomainModule") {

    // Get Supplier
    bind<BaseSingleUseCase<List<DistributorDateModel>, DistributorDateRequestModel>>("getDistributorDateUseCase") with provider {
        GetDistributorDateUseCase(instance(), instance(), instance("stockReportDataSource"))
    }

}