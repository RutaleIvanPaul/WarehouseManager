package io.ramani.ramaniWarehouse.data.stockassignmentreport

import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.StockAssignmentReportDistributorDateRemoteModel
import io.reactivex.Single
import retrofit2.http.*

interface StockAssignmentReportApi {
    @GET("/sfa/intakes/salesperson/date-range")
    fun getStockAssignmentReportDistributorDate(
        @Query("salesPersonUID") salesPersonUID: String,
        @Query("warehouseId") warehouseId: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<BaseResponse<List<StockAssignmentReportDistributorDateRemoteModel>>>
}