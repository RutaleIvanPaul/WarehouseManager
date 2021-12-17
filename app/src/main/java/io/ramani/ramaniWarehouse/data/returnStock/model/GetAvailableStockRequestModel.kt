package io.ramani.ramaniWarehouse.data.returnStock.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params

data class GetAvailableStockRequestModel(
    val salesPersonUID: String
): Params
