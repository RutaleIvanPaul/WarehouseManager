package io.ramani.ramaniWarehouse.app.assignmentreport.di

import io.ramani.ramaniWarehouse.data.assignmentreport.model.DistributorDateRequestModel
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.assignmentreport.usecase.GetDistributorDateUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val assignmentReportDomainModule = Kodein.Module("assignmentReportDomainModule") {

    // Get Supplier
    bind<BaseSingleUseCase<List<DistributorDateModel>, DistributorDateRequestModel>>("getDistributorDateUseCase") with provider {
        GetDistributorDateUseCase(instance(), instance(), instance("assignmentReportDataSource"))
    }

}