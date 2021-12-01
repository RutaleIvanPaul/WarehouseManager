package io.ramani.ramaniWarehouse.data.returnStock

import io.ramani.ramaniWarehouse.data.auth.model.LoginRequestModel
import io.ramani.ramaniWarehouse.data.auth.model.UserRemoteModel
import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface ReturnStockApi {
    @GET("/accounts/team/salespeople/v2?companyId=60886068ebfa73a74fbaf2bd")
    fun login(
        @Query()
    ): Single<BaseResponse<UserRemoteModel>>
}