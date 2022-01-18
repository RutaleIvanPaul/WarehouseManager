package io.ramani.ramaniWarehouse.domain.stockreport.usecase

import io.ramani.ramaniWarehouse.data.stockreport.model.DistributorDateRequestModel
import io.ramani.ramaniWarehouse.domain.stockreport.StockReportDataSource
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.reactivex.Single

class GetDistributorDateUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val stockReportDataSource: StockReportDataSource
) : BaseSingleUseCase<List<DistributorDateModel>, DistributorDateRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: DistributorDateRequestModel?): Single<List<DistributorDateModel>> =
        stockReportDataSource.getDistributorDate(
            params?.companyId ?: "",
            params?.warehouseId ?: "",
            params?.date ?: "",
            params?.page ?: 0,
            params?.size ?: 0)

}