package io.ramani.ramaniWarehouse.data.warehouses

import io.ramani.ramaniWarehouse.data.common.network.ApiConstants
import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.warehouses.models.GetWarehouseRemoteResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WarehouseApi {
    @GET("/warehouses/companyId")
    fun getWarehouses(
        @Query("companyId") companyId: String,
        @Query("page") page: Int,
        @Query("size") perPage: Int = ApiConstants.PAGINATION_PER_PAGE_SIZE
    ): Single<BaseResponse<GetWarehouseRemoteResponseModel>>

}