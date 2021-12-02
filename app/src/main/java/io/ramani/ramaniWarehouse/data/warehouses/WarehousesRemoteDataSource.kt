package io.ramani.ramaniWarehouse.data.warehouses

import io.ramani.ramaniWarehouse.data.common.network.ApiConstants
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.entities.PaginationMeta
import io.ramani.ramaniWarehouse.domain.warehouses.WarehousesDataSource
import io.ramani.ramaniWarehouse.domain.warehouses.models.GetWarehousesRequestModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.Single

class WarehousesRemoteDataSource(
    private val warehouseApi: WarehouseApi,
    private val warehouseRemoteModelMapper: ModelMapper<WarehouseRemoteModel, WarehouseModel>
) : WarehousesDataSource, BaseRemoteDataSource() {
    override fun getWarehouses(getWarehousesRequestModel: GetWarehousesRequestModel): Single<PagedList<WarehouseModel>> =
        callSingle(
            warehouseApi.getWarehouses(
                getWarehousesRequestModel.companyId ?: "",
                getWarehousesRequestModel.page ?: 1
            )
        ).flatMap {
            val data = it.data?.warehouses
            val meta = PaginationMeta.Builder()
                .count(it.data?.totalDocs ?: 0)
                .currenPage(it.data?.page ?: 0)
                .perPage(ApiConstants.PAGINATION_PER_PAGE_SIZE)
                .totalPages(it.data?.totalPages ?: 0)
                .build()
            Single.just(
                PagedList.Builder<WarehouseModel>()
                    .data(data?.mapFromWith(warehouseRemoteModelMapper) ?: listOf())
                    .paginationMeta(meta)
                    .build()
            )
        }
}