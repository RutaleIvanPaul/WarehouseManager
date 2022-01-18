package io.ramani.ramaniWarehouse.domain.returnStock.useCase

import io.ramani.ramaniWarehouse.data.returnStock.model.GetSalespeopleRequestModel
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.returnStock.ReturnStockDataSource
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.reactivex.Single

class GetSalesPeopleUsecase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val returnStockDataSource: ReturnStockDataSource
): BaseSingleUseCase<List<SalespeopleModel>,GetSalespeopleRequestModel>(threadExecutor,postThreadExecutor) {
    override fun buildUseCaseSingle(params: GetSalespeopleRequestModel?): Single<List<SalespeopleModel>> {
       return returnStockDataSource.getSalespeople(params!!.companyId)
    }
}