package io.ramani.ramaniWarehouse.app.stockassignmentreport.di

import io.ramani.ramaniWarehouse.data.assignmentreport.model.DistributorDateRequestModel
import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.StockAssignmentReportDistributorDateRequestModel
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.assignmentreport.usecase.GetDistributorDateUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.usecase.GetStockAssignmentReportDistributorDateUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val stockAssignmentReportDomainModule = Kodein.Module("stockAssignmentReportDomainModule") {

    // Get Supplier
    bind<BaseSingleUseCase<List<StockAssignmentReportDistributorDateModel>, StockAssignmentReportDistributorDateRequestModel>>("getStockAssignmentReportDistributorDateUseCase") with provider {
        GetStockAssignmentReportDistributorDateUseCase(instance(), instance(), instance("stockAssignmentReportDataSource"))
    }

}