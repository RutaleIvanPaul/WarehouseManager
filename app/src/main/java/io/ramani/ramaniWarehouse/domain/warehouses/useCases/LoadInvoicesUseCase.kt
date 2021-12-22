package io.ramani.ramaniWarehouse.domain.warehouses.useCases

import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.warehouses.WarehousesDataSource
import io.ramani.ramaniWarehouse.domain.warehouses.models.GetWarehousesRequestModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.reactivex.Single

class LoadInvoicesUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val warehousesDataSource: WarehousesDataSource
) : BaseSingleUseCase<PagedList<InvoiceModel>, GetWarehousesRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: GetWarehousesRequestModel?): Single<PagedList<InvoiceModel>> =
        warehousesDataSource.getInvoices(params ?: GetWarehousesRequestModel("", 1))


}