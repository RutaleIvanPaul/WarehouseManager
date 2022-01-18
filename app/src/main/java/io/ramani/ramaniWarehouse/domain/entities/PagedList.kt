package io.ramani.ramaniWarehouse.domain.entities

import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class PagedList<T>(val data: List<T> = listOf(),
                        val paginationMeta: PaginationMeta = PaginationMeta(),
                        var firstPageResponse: String = "") {

    class Builder<T> : IBuilder<PagedList<T>> {
        private var data = listOf<T>()
        private var paginationMeta = PaginationMeta()
        private var firstPageResponse: String = ""

        fun data(data: List<T>): Builder<T> {
            this.data = data
            return this
        }

        fun paginationMeta(paginationMeta: PaginationMeta): Builder<T> {
            this.paginationMeta = paginationMeta
            return this
        }

        fun firstPageResponse(firstPageResponse: String): Builder<T> {
            this.firstPageResponse = firstPageResponse
            return this
        }

        override fun build(): PagedList<T> = PagedList(data, paginationMeta, firstPageResponse)
    }
}