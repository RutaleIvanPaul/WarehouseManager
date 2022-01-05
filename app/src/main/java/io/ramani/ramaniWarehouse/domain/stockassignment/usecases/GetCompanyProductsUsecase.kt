package io.ramani.ramaniWarehouse.domain.stockassignment.usecases

import io.ramani.ramaniWarehouse.data.stockassignment.model.GetProductsRequestModel
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.reactivex.Single

class GetCompanyProductsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val assignStockDataSource: AssignStockDataSource
): BaseSingleUseCase<List<ProductEntity>, GetProductsRequestModel>(threadExecutor,postThreadExecutor) {
    override fun buildUseCaseSingle(params: GetProductsRequestModel?): Single<List<ProductEntity>> {
       return assignStockDataSource.getProducts(params!!.companyId)
    }
}