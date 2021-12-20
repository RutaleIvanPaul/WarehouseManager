package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.domainCore.prefs.Prefs
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.domain.assignmentreport.AssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.DistributorDateModel
import io.reactivex.Single

class AssignmentReportLocalDataSource(
    private val prefsManager: Prefs
) : AssignmentReportDataSource, BaseRemoteDataSource() {

    override fun getDistributorDate(
        companyId: String,
        warehouseId: String,
        date: String,
        page: Int,
        size: Int
    ): Single<List<DistributorDateModel>> {
        TODO("Not yet implemented")
    }

}