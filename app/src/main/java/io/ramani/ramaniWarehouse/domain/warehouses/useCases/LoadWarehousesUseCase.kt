package io.ramani.ramaniWarehouse.domain.warehouses.useCases

import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.warehouses.WarehousesDataSource
import io.ramani.ramaniWarehouse.domain.warehouses.models.GetWarehousesRequestModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.Single

class LoadWarehousesUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val warehousesDataSource: WarehousesDataSource
) : BaseSingleUseCase<PagedList<WarehouseModel>, GetWarehousesRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: GetWarehousesRequestModel?): Single<PagedList<WarehouseModel>> =
        warehousesDataSource.getWarehouses(params ?: GetWarehousesRequestModel("", 1))


}