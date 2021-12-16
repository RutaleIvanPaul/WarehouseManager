package io.ramani.ramaniWarehouse.data.warehouses

import io.ramani.ramaniWarehouse.data.common.network.ApiConstants.API_VERSION_1
import io.ramani.ramaniWarehouse.data.common.network.ApiConstants.PAGINATION_PER_PAGE_SIZE
import io.ramani.ramaniWarehouse.data.entities.BasePagedRemoteResponseModel
import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.warehouses.models.InvoiceRemoteModel
import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseRemoteModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WarehouseApi {
    @GET("/warehouses/companyId")
    fun getWarehouses(
        @Query("companyId") companyId: String,
        @Query("page") page: Int,
        @Query("size") perPage: Int = PAGINATION_PER_PAGE_SIZE
    ): Single<BaseResponse<BasePagedRemoteResponseModel<List<WarehouseRemoteModel>>>>

    @GET("${API_VERSION_1}/invoices/pending/distributorId")
    fun getInvoices(
        @Query("companyId") companyId: String,
        @Query("page") page: Int,
        @Query("size") perPage: Int = PAGINATION_PER_PAGE_SIZE
    ): Single<BaseResponse<BasePagedRemoteResponseModel<List<InvoiceRemoteModel>>>>

}