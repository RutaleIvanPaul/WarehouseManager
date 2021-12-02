package io.ramani.ramaniWarehouse.data.returnStock

import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.returnStock.model.SalespeopleRemoteModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ReturnStockApi {
    @GET("/accounts/team/salespeople/v2")
    fun getSalespeople(
        @Query("companyId") companyId: String
    ): Single<BaseResponse<List<SalespeopleRemoteModel>>>
}