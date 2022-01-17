package io.ramani.ramaniWarehouse.app.stockreport.flow

import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel

interface StockReportFlow {
    fun openDetail(isAssignedStock: Boolean, stock: DistributorDateModel)
    fun openPrint()
}