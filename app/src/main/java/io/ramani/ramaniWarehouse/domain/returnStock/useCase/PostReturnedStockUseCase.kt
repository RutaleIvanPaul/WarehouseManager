package io.ramani.ramaniWarehouse.domain.returnStock.useCase

import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.returnStock.ReturnStockDataSource
import io.reactivex.Single

class PostReturnedStockUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val returnStockDataSource: ReturnStockDataSource
):BaseSingleUseCase<PostReturnItemsResponse,PostReturnItems>(threadExecutor,postThreadExecutor) {
    override fun buildUseCaseSingle(params: PostReturnItems?): Single<PostReturnItemsResponse> =
        returnStockDataSource.postReturnedStock(params!!)

}