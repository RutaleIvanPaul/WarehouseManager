package io.ramani.ramaniWarehouse.data.stockassignment

import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItemsResponse
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.SalesPersonRemoteModel
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface AssignStockAPI {
    @GET("/accounts/team/salespeople/v2")
    fun getSalesPerson(
        @Query("companyId") companyId: String
    ): Single<BaseResponse<List<SalesPersonRemoteModel>>>

    @GET("/products/history/all/v2")
    fun getCompanyProducts(
        @Header("invalidate_cache") invalidate_cache: String,
        @Query("companyId") companyId: String
    ): Single<BaseResponse<List<RemoteProductModel>>>

    @Multipart
    @POST("/sfa/new/intake/v2")
    fun postAssignedStock(
        @Part("assigner") assigner: RequestBody,
        @Part("companyId") companyId: RequestBody,
        @Part("comment") comment: RequestBody,
        @Part("dateStockTaken") dateStockTaken: RequestBody,
        @Part("name") name: RequestBody,
        @Part("salesPersonUID") salesPersonUID: RequestBody,
        @Part("stockAssignmentType") stockAssignmentType: RequestBody,
        @Part("warehouseId") warehouseId: RequestBody,
        @Part("listOfProducts") listOfProducts: RequestBody,
        @Part("storeKeeperSignature")  storeKeeperSignature: RequestBody,
        @Part("salesPersonSignature") salesPersonSignature: RequestBody
    ): Single<BaseResponse<PostAssignedItemsResponse>>
}