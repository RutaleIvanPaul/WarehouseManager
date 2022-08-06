package io.ramani.ramaniWarehouse.data.stockreport

import io.ramani.ramaniWarehouse.domain.stockreport.StockReportDataSource
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import io.reactivex.Single

class StockReportRepository(
    private val remoteStockReportDataSource: StockReportDataSource,
    private val localStockReportDataSource: StockReportDataSource
) : StockReportDataSource {

    override fun getDistributorDate(companyId: String, warehouseId: String, date: String, page: Int, size: Int): Single<List<DistributorDateModel>> =
        remoteStockReportDataSource.getDistributorDate(companyId, warehouseId, date, page, size)

}