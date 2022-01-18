package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.data.auth.models.LoginRequestModel
import io.ramani.ramaniWarehouse.data.auth.models.UserRemoteModel
import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/get/login")
    fun login(
        @Body loginRequest: LoginRequestModel
    ): Single<BaseResponse<UserRemoteModel>>

}