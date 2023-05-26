package io.ramani.ramaniWarehouse.data.stockreceive

import io.ramani.ramaniWarehouse.data.stockreceive.model.GetSupplierRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRemoteModel
import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface StockReceiveApi {
    @GET("/api/v1/accounts/suppliers-distributor/relationship")
    fun getSuppliers(
        @Query("companyId") companyId: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<GetSupplierRemoteModel>>

    @GET("/api/v1/account/decline-reasons")
    fun getDeclineReasons(): Single<BaseResponse<List<String>>>

    @POST("/api/v2/invoice/goods-received")
    fun postGoodsReceived(
        @Body body: RequestBody
    ): Single<BaseResponse<GoodsReceivedRemoteModel>>

    @PUT("/api/v2/invoice/goods-received")
    fun putMoreSupportingDocs(
        @Body body: RequestBody
    ): Single<BaseResponse<Any>>

}