package io.ramani.ramaniWarehouse.domain.warehouses.models

import io.ramani.ramaniWarehouse.domain.base.v2.Params

data class GetWarehousesRequestModel(val companyId:String?,val page:Int?):Params