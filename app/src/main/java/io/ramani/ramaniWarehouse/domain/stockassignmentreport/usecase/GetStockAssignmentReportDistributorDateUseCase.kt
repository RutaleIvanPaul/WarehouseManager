package io.ramani.ramaniWarehouse.domain.stockassignmentreport.usecase

import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.StockAssignmentReportDistributorDateRequestModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.StockAssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import io.reactivex.Single

class GetStockAssignmentReportDistributorDateUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val stockAssignmentReportDataSource: StockAssignmentReportDataSource
) : BaseSingleUseCase<List<StockAssignmentReportDistributorDateModel>, StockAssignmentReportDistributorDateRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: StockAssignmentReportDistributorDateRequestModel?): Single<List<StockAssignmentReportDistributorDateModel>> =
        stockAssignmentReportDataSource.getStockAssignmentDistributorDate(
            params?.salesPersonUID ?: "",
            params?.warehouseId ?: "",
            params?.startDate ?: "",
            params?.endDate ?: ""
        )


}