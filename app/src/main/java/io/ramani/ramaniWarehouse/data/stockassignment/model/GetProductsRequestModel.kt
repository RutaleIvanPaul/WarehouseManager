package io.ramani.ramaniWarehouse.data.stockassignment.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params

data class GetProductsRequestModel(
    val companyId: String
): Params
