package io.ramani.ramaniWarehouse.domain.stockassignment.usecases

import io.ramani.ramaniWarehouse.data.returnStock.model.GetSalespeopleRequestModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.GetSalesPersonRequestModel
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.reactivex.Single

class GetSalesPersonUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val assignStockDataSource: AssignStockDataSource
): BaseSingleUseCase<List<SalesPersonModel>, GetSalesPersonRequestModel>(threadExecutor,postThreadExecutor) {
    override fun buildUseCaseSingle(params: GetSalesPersonRequestModel?): Single<List<SalesPersonModel>> {
       return assignStockDataSource.getSalesPerson(params!!.companyId)
    }
}