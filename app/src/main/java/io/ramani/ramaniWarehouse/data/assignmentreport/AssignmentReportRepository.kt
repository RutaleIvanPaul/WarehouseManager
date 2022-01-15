package io.ramani.ramaniWarehouse.data.assignmentreport

import io.ramani.ramaniWarehouse.domain.assignmentreport.AssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.reactivex.Single

class AssignmentReportRepository(
    private val remoteAssignmentReportDataSource: AssignmentReportDataSource,
    private val localAssignmentReportDataSource: AssignmentReportDataSource
) : AssignmentReportDataSource {

    override fun getDistributorDate(companyId: String, warehouseId: String, date: String, page: Int, size: Int): Single<List<DistributorDateModel>> =
        remoteAssignmentReportDataSource.getDistributorDate(companyId, warehouseId, date, page, size)

}