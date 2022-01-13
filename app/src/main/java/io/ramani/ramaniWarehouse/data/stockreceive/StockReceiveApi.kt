package io.ramani.ramaniWarehouse.data.stockreceive

import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.stockreceive.model.GetSupplierRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRemoteModel
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

    @Multipart
    @POST("/api/v1/invoice/goods-received")
    fun postGoodsReceived(
        @Part("invoiceId") invoiceId: String,
        @Part("warehouseManagerId") warehouseManagerId: String,
        @Part("warehouseId") warehouseId: String,
        @Part("distributorId") distributorId: String,
        @Part("date") date: String,
        @Part("time") time: String,
        @Part("deliveryPersonName") deliveryPersonName: String,
        @Part("supplierId") supplierId: String?,
        @Part("items") items: String?,
        @Part("storeKeeperSignature") storeKeeperSignature: RequestBody?,
        @Part("deliveryPersonSignature") deliveryPersonSignature: RequestBody?
    ): Single<BaseResponse<GoodsReceivedRemoteModel>>

}