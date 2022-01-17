package io.ramani.ramaniWarehouse.app.stockreport.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.stockreport.presentation.StockReportViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

/**
 * @description     Stock Report Module
 *
 * @author          Adrian
 */
val stockReportModule = Kodein.Module("stockReportModule") {
    import(stockReportDataModule)
    import(stockReportDomainModule)


    bind<StockReportViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, StockReportViewModel.Factory(
                instance(), instance(), instance(), instance(), instance("getDistributorDateUseCase"), instance()
            )
        ).get(StockReportViewModel::class.java)
    }

}