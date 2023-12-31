package io.ramani.ramaniWarehouse.app.common.di

import io.ramani.ramaniWarehouse.app.assignstock.di.assignStockModule
import io.ramani.ramaniWarehouse.app.auth.di.authModule
import io.ramani.ramaniWarehouse.app.common.di.datetime.dateTimeModule
import io.ramani.ramaniWarehouse.app.common.di.pagination.paginationModule
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.di.confirmReceiveModule
import io.ramani.ramaniWarehouse.app.main.di.mainModule
import io.ramani.ramaniWarehouse.app.returnstock.di.returnStockModule
import io.ramani.ramaniWarehouse.app.stockassignmentreport.di.stockAssignmentReportModule
import io.ramani.ramaniWarehouse.app.warehouses.di.warehousesModule
import io.ramani.ramaniWarehouse.app.stockreceive.di.stockReceiveModule
import io.ramani.ramaniWarehouse.app.stockreport.di.stockReportModule
import org.kodein.di.Kodein

/**
 * Created by Amr on 9/22/17.
 */
val appModule = Kodein.Module("appModule") {
    import(domainModule)
    import(networkModule)
    import(paginationModule)
    import(fileHelperModule)
    import(dateTimeModule)
    import(stringProviderModule)
    import(mainModule)
    import(authModule)
    import(warehousesModule)

    import(stockReceiveModule)
    import(returnStockModule)
    import(assignStockModule)
    import(stockReportModule)
    import(stockAssignmentReportModule)
    import(confirmReceiveModule)

    import(printerHelperModule)
}
