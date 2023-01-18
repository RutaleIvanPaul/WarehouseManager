package io.ramani.ramaniWarehouse.domain.stockassignment.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params

data class ReportsQueryRequestModel(
    var queries: List<ReportsQuery> = listOf()
):Params