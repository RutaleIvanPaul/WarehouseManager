package io.ramani.ramaniWarehouse.data.returnStock

import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableStockReturnedListItem
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.data.returnStock.model.SalespeopleRemoteModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReturnStockApi {
    @GET("/accounts/team/salespeople/v2")
    fun getSalespeople(
        @Query("companyId") companyId: String
    ): Single<BaseResponse<List<SalespeopleRemoteModel>>>

    @GET("/sfa/stock/available")
    fun getAvailableStock(
        @Query("salesPersonUID") salesPersonUID: String
    ): Single<BaseResponse<List<AvailableStockReturnedListItem>>>

    @POST("/sfa/new/intake/v2")
    fun postReturnedStock(
        @Body postReturnItems: PostReturnItems
    ): Single<BaseResponse<PostReturnItemsResponse>>
}