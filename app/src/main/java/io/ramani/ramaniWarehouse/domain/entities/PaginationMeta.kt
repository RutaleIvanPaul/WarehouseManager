package io.ramani.ramaniWarehouse.domain.entities

import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class PaginationMeta(val total: Int = 0,
                          val count: Int = 0,
                          val perPage: Int = 0,
                          val currentPage: Int = 0,
                          val totalPages: Int = 0,
                          val links: PaginationLinks = PaginationLinks()) {


    val isFirstPage = currentPage == 1

    val nextPage = currentPage + 1

    val hasNext = currentPage < totalPages

    class Builder : IBuilder<PaginationMeta> {
        private var total = 0
        private var count = 0
        private var perPage = 0
        private var currentPage = 0
        private var totalPages = 0
        private var links = PaginationLinks()

        fun total(total: Int): Builder {
            this.total = total
            return this
        }

        fun count(count: Int): Builder {
            this.count = count
            return this
        }

        fun perPage(perPage: Int): Builder {
            this.perPage = perPage
            return this
        }

        fun currenPage(currentPage: Int): Builder {
            this.currentPage = currentPage
            return this
        }

        fun totalPages(totalPages: Int): Builder {
            this.totalPages = totalPages
            return this
        }

        fun links(links: PaginationLinks): Builder {
            this.links = links
            return this
        }

        override fun build(): PaginationMeta = PaginationMeta(total,
                                                              count,
                                                              perPage,
                                                              currentPage,
                                                              totalPages,
                                                              links)
    }
}