package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRequestModel
import io.ramani.ramaniWarehouse.data.auth.model.SupplierRemoteModel
import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.reactivex.Single
import retrofit2.http.*

interface StockReceiveApi {
    @GET("/api/v1/accounts/suppliers-distributor/relationship")
    fun getSuppliers(
        @Query("companyId") companyId: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<GetSupplierRemoteModel>>

}