package io.ramani.ramaniWarehouse.data.auth.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params

data class GetSupplierRequestModel(val companyId:String,val page:Int,val size:Int):Params