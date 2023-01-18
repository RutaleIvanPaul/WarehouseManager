package io.ramani.ramaniWarehouse.data.stockassignment.mappers

import io.ramani.ramaniWarehouse.data.stockassignment.model.ReportsQueryRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ReportsQueryModel

class ReportsQueryRemoteMapper():ModelMapper<ReportsQueryRemoteModel, ReportsQueryModel> {
    override fun mapFrom(from: ReportsQueryRemoteModel): ReportsQueryModel {
        return ReportsQueryModel.Builder()
            .headers(from.headers)
            .rows(from.rows)
            .build()
    }

    override fun mapTo(to: ReportsQueryModel): ReportsQueryRemoteModel {
        return ReportsQueryRemoteModel(
            to.headers,
            to.rows
        )
    }

}