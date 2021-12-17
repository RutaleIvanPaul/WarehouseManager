package io.ramani.ramaniWarehouse.domain.returnStock.useCase

import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableStockReturnedListItem
import io.ramani.ramaniWarehouse.data.returnStock.model.GetAvailableStockRequestModel
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.returnStock.ReturnStockDataSource
import io.reactivex.Single

class GetAvailableStockUsecase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val returnStockDataSource: ReturnStockDataSource
):BaseSingleUseCase<List<AvailableStockReturnedListItem>,GetAvailableStockRequestModel>(threadExecutor,postThreadExecutor) {
    override fun buildUseCaseSingle(params: GetAvailableStockRequestModel?): Single<List<AvailableStockReturnedListItem>> =
        returnStockDataSource.getAvailableStock(params!!.salesPersonUID)

}