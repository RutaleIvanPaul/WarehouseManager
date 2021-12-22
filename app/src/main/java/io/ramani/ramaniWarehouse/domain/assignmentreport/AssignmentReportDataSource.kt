package io.ramani.ramaniWarehouse.domain.assignmentreport

import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.reactivex.Single

interface AssignmentReportDataSource {
    fun getDistributorDate(companyId: String, warehouseId: String, date: String, page: Int, size: Int): Single<List<DistributorDateModel>>
}