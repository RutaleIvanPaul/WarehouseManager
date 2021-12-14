package io.ramani.ramaniWarehouse.data.auth.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params
import okhttp3.RequestBody

data class GoodsReceivedRequestModel(val bodyMaps: Map<String, RequestBody>):Params