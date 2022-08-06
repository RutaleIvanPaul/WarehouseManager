package io.ramani.ramaniWarehouse.data.common.pagination

import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.entities.PaginationMetaRemote
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.entities.PaginationMeta

inline fun <reified T : Any, reified V> BaseResponse<List<T>>?.buildPagedList(
    modelMapper: UniModelMapper<T, V>,
    paginationModelMapper: ModelMapper<PaginationMetaRemote,
            PaginationMeta>
)
        : PagedList<V> =
        let { response ->
            PagedList.Builder<V>().run {
                data(response?.data?.mapFromWith(modelMapper) ?: listOf())
                paginationMeta(
                    response?.meta?.mapFromWith(paginationModelMapper)
                        ?: PaginationMeta()
                )
                build()
            }
        }