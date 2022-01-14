package io.ramani.ramaniWarehouse.data.stockassignmentreport

import io.ramani.ramaniWarehouse.domain.assignmentreport.AssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.StockAssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import io.reactivex.Single

class StockAssignmentReportRepository(
    private val remoteAssignmentReportDataSource: StockAssignmentReportDataSource,
    private val localAssignmentReportDataSource: StockAssignmentReportDataSource
) : StockAssignmentReportDataSource {

    override fun getStockAssignmentDistributorDate(salesPersonUID: String, warehouseId: String, startDate: String, endDate: String): Single<List<StockAssignmentReportDistributorDateModel>> =
        remoteAssignmentReportDataSource.getStockAssignmentDistributorDate(salesPersonUID, warehouseId, startDate, endDate)

}