package io.ramani.ramaniWarehouse.data.stockassignmentreport

import io.ramani.ramaniWarehouse.domainCore.prefs.Prefs
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.domain.assignmentreport.AssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.StockAssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import io.reactivex.Single

class StockAssignmentReportLocalDataSource(
    private val prefsManager: Prefs
) : StockAssignmentReportDataSource, BaseRemoteDataSource() {

    override fun getStockAssignmentDistributorDate(
        salesPersonUID: String,
        warehouseId: String,
        startDate: String,
        endDate: String
    ): Single<List<StockAssignmentReportDistributorDateModel>> {
        TODO("Not yet implemented")
    }

}