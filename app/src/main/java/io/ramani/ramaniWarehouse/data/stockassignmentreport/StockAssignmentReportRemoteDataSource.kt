package io.ramani.ramaniWarehouse.data.stockStockAssignmentReport

import android.util.Log
import io.ramani.ramaniWarehouse.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniWarehouse.data.common.network.ErrorConstants
import io.ramani.ramaniWarehouse.data.common.network.toErrorResponseModel
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.data.stockassignmentreport.StockAssignmentReportApi
import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.StockAssignmentReportDistributorDateRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.entities.BaseErrorResponse
import io.ramani.ramaniWarehouse.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.StockAssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import io.reactivex.Single
import retrofit2.HttpException

class StockStockAssignmentReportRemoteDataSource(
    private val StockAssignmentReportApi: StockAssignmentReportApi,
    private val distributorDateRemoteMapper: ModelMapper<StockAssignmentReportDistributorDateRemoteModel, StockAssignmentReportDistributorDateModel>,
    ) : StockAssignmentReportDataSource, BaseRemoteDataSource() {
    override fun getStockAssignmentDistributorDate(salesPersonUID: String, warehouseId: String, startDate: String, endDate: String) =
        callSingle(
            StockAssignmentReportApi.getStockAssignmentReportDistributorDate(salesPersonUID, warehouseId, startDate, endDate).flatMap {
                val data = it.data
                Log.e("555555", data.toString())
                if (data != null) {
//                    Single.just(data.listOfProducts)
                    Single.just(data.listOfProducts.mapFromWith(distributorDateRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }
                .onErrorResumeNext {
                    if (it is HttpException) {
                        val code = it.code()
                        val errorResponse = it.toErrorResponseModel<BaseErrorResponse<Any>>()
                        when (code) {
                            ErrorConstants.INPUT_VALIDATION_400,
                            ErrorConstants.NOT_FOUND_404 ->
                                Single.error(InvalidLoginException(errorResponse?.message))
                            ErrorConstants.NOT_AUTHORIZED_403 ->
                                Single.error(AccountNotActiveException(errorResponse?.message))
                            else -> Single.error(it)
                        }
                    } else if (it is NotAuthenticatedException) {
                        val message =
                            if (!it.message.isNullOrBlank()) it.message
                            else if (it.cause.isNotNull() && !it.cause?.message.isNullOrBlank()) it.cause?.message
                            else "Not Authorized exception"
                        Single.error(
                            NotAuthorizedException(
                                message ?: ""
                            )
                        )

                    } else {
                        Single.error(it)
                    }
                }
        )
}