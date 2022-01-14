package io.ramani.ramaniWarehouse.app.stockassignmentreport.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation.StockAssignmentReportViewModel
import io.ramani.ramaniWarehouse.app.stockstockAssignmentReport.di.stockAssignmentReportDataModule
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance


val stockAssignmentReportModule = Kodein.Module("stockAssignmentReportModule") {
    import(stockAssignmentReportDataModule)
    import(stockAssignmentReportDomainModule)


    bind<StockAssignmentReportViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, StockAssignmentReportViewModel.Factory(
                instance(), instance(), instance(), instance(), instance("getStockAssignmentReportDistributorDateUseCase")
            )
        ).get(StockAssignmentReportViewModel::class.java)
    }

}