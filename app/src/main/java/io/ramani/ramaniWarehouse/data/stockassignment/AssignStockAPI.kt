package io.ramani.ramaniWarehouse.data.stockassignment

import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.returnStock.model.SalespeopleRemoteModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.SalesPersonRemoteModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AssignStockAPI {
    @GET("/accounts/team/salespeople/v2")
    fun getSalesPerson(
        @Query("companyId") companyId: String
    ): Single<BaseResponse<List<SalesPersonRemoteModel>>>
}