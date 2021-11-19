package io.ramani.ramaniWarehouse.domain.entities

import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class PaginationLinks(val next: String = "",
                           val previous: String = "") {
    class Builder : IBuilder<PaginationLinks> {
        private var next = ""
        private var previous = ""

        fun next(next: String): Builder {
            this.next = next
            return this
        }

        fun previous(previous: String): Builder {
            this.previous = previous
            return this
        }

        override fun build(): PaginationLinks =
                PaginationLinks(next, previous)

    }
}