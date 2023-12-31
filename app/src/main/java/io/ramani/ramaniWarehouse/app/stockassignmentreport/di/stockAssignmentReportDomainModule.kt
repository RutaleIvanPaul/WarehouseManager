package io.ramani.ramaniWarehouse.app.stockassignmentreport.di

import io.ramani.ramaniWarehouse.data.stockassignment.model.GetSalesPersonRequestModel
import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.StockAssignmentReportDistributorDateRequestModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.ramani.ramaniWarehouse.domain.stockassignment.usecases.GetSalesPersonUseCase
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

    bind<BaseSingleUseCase<List<SalesPersonModel>, GetSalesPersonRequestModel>>("getAssignmentReportSalesPersonUseCase") with provider {
        GetSalesPersonUseCase(instance(), instance(), instance("assignStockRepository"))
    }

}