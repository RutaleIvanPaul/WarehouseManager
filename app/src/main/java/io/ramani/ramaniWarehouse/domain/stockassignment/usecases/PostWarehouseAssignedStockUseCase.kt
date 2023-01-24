package io.ramani.ramaniWarehouse.domain.stockassignment.usecases

import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostWarehouseAssignedItems
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.reactivex.Single

class PostWarehouseAssignedStockUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val assignStockDataSource: AssignStockDataSource
): BaseSingleUseCase<String, PostWarehouseAssignedItems>(threadExecutor,postThreadExecutor)  {
    override fun buildUseCaseSingle(params: PostWarehouseAssignedItems?): Single<String>  =
        assignStockDataSource.postAssignedWarehouseStock(params!!,AssignedItemDetails.assigningWarehouseId)

}