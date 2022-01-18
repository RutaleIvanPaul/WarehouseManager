package io.ramani.ramaniWarehouse.data.stockreport.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params

data class DistributorDateRequestModel(val companyId:String, val warehouseId:String, val date:String, val page:Int, val size:Int):Params