package io.ramani.ramaniWarehouse.data.auth.models

import io.ramani.ramaniWarehouse.domain.base.v2.Params

data class LoginRequestModel(val phoneNumber:String,val password:String):Params