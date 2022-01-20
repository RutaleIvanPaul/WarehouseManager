package io.ramani.ramaniWarehouse.data.stockassignment.model

import android.content.Context
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import okhttp3.RequestBody

data class AssignProductsRequestModel(
    val postAssignmentItem: PostAssignedItems
) : Params {

}
