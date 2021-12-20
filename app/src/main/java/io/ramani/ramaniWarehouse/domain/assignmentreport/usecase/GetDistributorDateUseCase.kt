package io.ramani.ramaniWarehouse.domain.auth.useCase

import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRequestModel
import io.ramani.ramaniWarehouse.domain.auth.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.reactivex.Single

class GetSupplierUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val stockReceiveDataSource: StockReceiveDataSource
) : BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: GetSupplierRequestModel?): Single<List<SupplierModel>> =
        stockReceiveDataSource.getSuppliers(params?.companyId ?: "", params?.page ?: 0, params?.size ?: 0)

}