package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.data.auth.model.*
import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.reactivex.Single
import retrofit2.http.*

interface AssignmentReportApi {
    @GET("/api/v1/warehouse/stock/distributor-date")
    fun getDistributorDate(
        @Query("companyId") companyId: String,
        @Query("warehouseId") warehouseId: String,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<List<DistributorDateRemoteModel>>>
}