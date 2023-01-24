package io.ramani.ramaniWarehouse.domain.stockassignment.usecases

import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ReportsQueryRequestModel
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ReportsQueryModel
import io.reactivex.Single

class ReportsQueryUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val assignStockDataSource: AssignStockDataSource
):BaseSingleUseCase<ReportsQueryModel, ReportsQueryRequestModel>(threadExecutor,postThreadExecutor) {
    override fun buildUseCaseSingle(params: ReportsQueryRequestModel?): Single<ReportsQueryModel> =
        assignStockDataSource.getReportsQuery(params!!)
}