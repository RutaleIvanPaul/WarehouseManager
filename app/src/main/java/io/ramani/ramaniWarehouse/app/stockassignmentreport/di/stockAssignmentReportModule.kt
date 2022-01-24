package io.ramani.ramaniWarehouse.app.stockassignmentreport.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.mapper.SalesPersonRVMapper
import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.model.SalesPersonRVModel
import io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation.AssignmentReportSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation.StockAssignmentReportViewModel
import io.ramani.ramaniWarehouse.app.stockstockAssignmentReport.di.stockAssignmentReportDataModule
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider


val stockAssignmentReportModule = Kodein.Module("stockAssignmentReportModule") {
    import(stockAssignmentReportDataModule)
    import(stockAssignmentReportDomainModule)


    bind<StockAssignmentReportViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, StockAssignmentReportViewModel.Factory(
                instance(), instance(), instance(), instance(), instance(), instance("getStockAssignmentReportDistributorDateUseCase"),
                instance("getAssignmentReportSalesPersonUseCase"), instance()
            )
        ).get(StockAssignmentReportViewModel::class.java)
    }

    bind<AssignmentReportSalesPersonViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, AssignmentReportSalesPersonViewModel.Factory(
                instance(),
                instance(),
                instance(),
                instance("getSalesPersonUseCase"),
                instance(),
                instance()
            )
        ).get(AssignmentReportSalesPersonViewModel::class.java)
    }


}