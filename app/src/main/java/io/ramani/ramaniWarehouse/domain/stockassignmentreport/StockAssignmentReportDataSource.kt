package io.ramani.ramaniWarehouse.domain.stockassignmentreport

import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import io.reactivex.Single

interface StockAssignmentReportDataSource {
    fun getStockAssignmentDistributorDate(salesPersonUID: String, warehouseId: String, startDate: String, endDate: String): Single<List<StockAssignmentReportDistributorDateModel>>
}