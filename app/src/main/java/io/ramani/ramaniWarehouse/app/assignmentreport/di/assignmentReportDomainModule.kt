package io.ramani.ramaniWarehouse.app.assignmentreport.di

import io.ramani.ramaniWarehouse.data.auth.model.DistributorDateRequestModel
import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRequestModel
import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.domain.auth.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.auth.useCase.GetDeclineReasonsUseCase
import io.ramani.ramaniWarehouse.domain.auth.useCase.GetDistributorDateUseCase
import io.ramani.ramaniWarehouse.domain.auth.useCase.GetSupplierUseCase
import io.ramani.ramaniWarehouse.domain.auth.useCase.PostGoodsReceivedUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.Params
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