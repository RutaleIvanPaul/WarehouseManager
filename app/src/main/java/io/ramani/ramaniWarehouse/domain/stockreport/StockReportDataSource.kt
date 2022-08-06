package io.ramani.ramaniWarehouse.domain.stockreport

import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import io.reactivex.Single

interface StockReportDataSource {
    fun getDistributorDate(companyId: String, warehouseId: String, date: String, page: Int, size: Int): Single<List<DistributorDateModel>>
}