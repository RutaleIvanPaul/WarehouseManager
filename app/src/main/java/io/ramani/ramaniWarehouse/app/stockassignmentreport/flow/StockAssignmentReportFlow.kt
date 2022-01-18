package io.ramani.ramaniWarehouse.app.stockassignmentreport.flow

import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel

interface StockAssignmentReportFlow {
    fun openDetail(isAssignedStock: Boolean, stock: StockAssignmentReportDistributorDateModel)
    fun openPrint()
}