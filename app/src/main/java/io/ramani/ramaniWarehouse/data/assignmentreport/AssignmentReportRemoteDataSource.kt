package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.data.auth.mappers.GoodsReceivedRemoteMapper
import io.ramani.ramaniWarehouse.data.auth.model.DistributorDateRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedRemoteModel
import io.ramani.ramaniWarehouse.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniWarehouse.data.auth.model.SupplierRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ErrorConstants
import io.ramani.ramaniWarehouse.data.common.network.toErrorResponseModel
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.domain.assignmentreport.AssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.auth.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.entities.BaseErrorResponse
import io.ramani.ramaniWarehouse.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.http.Part

class AssignmentReportRemoteDataSource(
    private val assignmentReportApi: AssignmentReportApi,
    private val distributorDateRemoteMapper: ModelMapper<DistributorDateRemoteModel, DistributorDateModel>,
    ) : AssignmentReportDataSource, BaseRemoteDataSource() {
    override fun getDistributorDate(companyId: String, warehouseId: String, date: String, page: Int, size: Int) =
        callSingle(
            assignmentReportApi.getDistributorDate(companyId, warehouseId, date, page, size).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(distributorDateRemoteMapper))
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