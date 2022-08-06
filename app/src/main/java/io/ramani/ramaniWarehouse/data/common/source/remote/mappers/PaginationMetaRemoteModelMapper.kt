package io.ramani.ramaniWarehouse.data.common.source.remote.mappers

import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.data.entities.PaginationMetaRemote
import io.ramani.ramaniWarehouse.domain.entities.PaginationMeta

class PaginationMetaRemoteModelMapper : ModelMapper<PaginationMetaRemote, PaginationMeta> {
    override fun mapFrom(from: PaginationMetaRemote): PaginationMeta =
            PaginationMeta.Builder()
                    .total(from.total ?: 0)
                    .currenPage(from.currentPage ?: 0)
                    .perPage(from.perPage ?: 0)
                    .totalPages(from.totalPages ?: 0)
                    .build()

        override fun mapTo(to: PaginationMeta): PaginationMetaRemote =
                PaginationMetaRemote(to.total,to.perPage,to.currentPage,to.totalPages)
}