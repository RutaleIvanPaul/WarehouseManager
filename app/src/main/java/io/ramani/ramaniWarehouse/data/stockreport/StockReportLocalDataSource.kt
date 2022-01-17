package io.ramani.ramaniWarehouse.data.stockreport

import io.ramani.ramaniWarehouse.domainCore.prefs.Prefs
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.domain.stockreport.StockReportDataSource
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import io.reactivex.Single

class StockReportLocalDataSource(
    private val prefsManager: Prefs
) : StockReportDataSource, BaseRemoteDataSource() {

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