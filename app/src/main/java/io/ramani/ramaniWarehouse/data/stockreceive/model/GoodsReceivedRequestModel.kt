package io.ramani.ramaniWarehouse.data.stockreceive.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params
import okhttp3.RequestBody

data class GoodsReceivedRequestModel(
    val body: RequestBody,
):Params {

}