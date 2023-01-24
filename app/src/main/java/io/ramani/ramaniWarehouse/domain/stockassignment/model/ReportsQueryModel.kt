package io.ramani.ramaniWarehouse.domain.stockassignment.model

import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class ReportsQueryModel(
    val headers: List<String> = listOf(),
    val rows: List<List<String>> = listOf()
) {

    class Builder: IBuilder<ReportsQueryModel> {
        private var headers: List<String> = listOf()
        private var rows: List<List<String>> = listOf()

        fun headers(headers: List<String>):Builder{
            this.headers = headers
            return this
        }

        fun rows(rows: List<List<String>>):Builder{
            this.rows = rows
            return this
        }

        override fun build(): ReportsQueryModel  =
            ReportsQueryModel(
                headers, rows
            )

    }

}