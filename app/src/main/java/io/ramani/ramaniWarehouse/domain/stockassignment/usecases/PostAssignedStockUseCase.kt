package io.ramani.ramaniWarehouse.domain.stockassignment.usecases

import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.data.stockassignment.model.AssignProductsRequestModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItems
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItemsResponse
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.reactivex.Single

class PostAssignedStockUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val assignStockDataSource: AssignStockDataSource
):BaseSingleUseCase<PostAssignedItemsResponse, AssignProductsRequestModel>(threadExecutor,postThreadExecutor) {
    override fun buildUseCaseSingle(params: AssignProductsRequestModel?): Single<PostAssignedItemsResponse> =
        assignStockDataSource.postAssignedStock(params!!.body)

}