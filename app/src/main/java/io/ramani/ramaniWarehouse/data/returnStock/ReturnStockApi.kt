package io.ramani.ramaniWarehouse.data.returnStock

import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableStockReturnedListItem
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.data.returnStock.model.SalespeopleRemoteModel
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ReturnStockApi {
    @GET("/accounts/team/salespeople/v2")
    fun getSalespeople(
        @Query("companyId") companyId: String
    ): Single<BaseResponse<List<SalespeopleRemoteModel>>>

    @GET("/sfa/stock/available")
    fun getAvailableStock(
        @Query("salesPersonUID") salesPersonUID: String
    ): Single<BaseResponse<List<AvailableStockReturnedListItem>>>

    @Multipart
    @POST("/sfa/new/intake/v2")
    fun postReturnedStock(
        @Part("assigner") assigner: RequestBody,
        @Part("comment") comment: RequestBody,
        @Part("companyId") companyId: RequestBody,
        @Part("dateStockTaken") dateStockTaken: RequestBody,
        @Part("name") name: RequestBody,
        @Part("salesPersonUID") salesPersonUID: RequestBody,
        @Part("stockAssignmentType") stockAssignmentType: RequestBody,
        @Part("warehouseId") warehouseId: RequestBody,
        @Part("listOfProducts") listOfProducts: RequestBody,
        @Part("storeKeeperSignature") storeKeeperSignature: RequestBody,
        @Part("salesPersonSignature") salesPersonSignature: RequestBody
    ): Single<BaseResponse<PostReturnItemsResponse>>
}