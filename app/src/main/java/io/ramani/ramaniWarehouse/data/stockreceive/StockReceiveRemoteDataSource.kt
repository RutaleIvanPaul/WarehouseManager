package io.ramani.ramaniWarehouse.data.stockreceive


import android.util.Log
import io.ramani.ramaniWarehouse.data.common.network.ErrorConstants
import io.ramani.ramaniWarehouse.data.common.network.toErrorResponseModel
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRemoteModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.SupplierRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.entities.BaseErrorResponse
import io.ramani.ramaniWarehouse.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniWarehouse.domain.stockreceive.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
import io.ramani.ramaniWarehouse.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.HttpException

class StockReceiveRemoteDataSource(
    private val stockReceiveApi: StockReceiveApi,
    private val supplierRemoteMapper: ModelMapper<SupplierRemoteModel, SupplierModel>,
    private val goodsReceivedRemoteMapper: ModelMapper<GoodsReceivedRemoteModel, GoodsReceivedModel>,
) : StockReceiveDataSource, BaseRemoteDataSource() {
    override fun getSuppliers(companyId: String, page: Int, size: Int) =
        callSingle(
            stockReceiveApi.getSuppliers(companyId, page, size).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.suppliers.mapFromWith(supplierRemoteMapper))
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

    override fun getDeclineReasons() =
        callSingle(
            stockReceiveApi.getDeclineReasons().flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data)
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

    override fun postGoodsReceived(
        body: RequestBody
    ): Single<GoodsReceivedModel> =
        callSingle(
            stockReceiveApi.postGoodsReceived(body).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(goodsReceivedRemoteMapper))
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
                            ErrorConstants.UNPROCESSABLE_ENTITY_422 ->
                                Single.error(Exception(errorResponse?.message))
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

    override fun putMoreSupportingDocs(body: RequestBody): Single<String> =
        callSingle(
            stockReceiveApi.putMoreSupportingDocs(body).flatMap {
                val status = it.status
                if (status != null) {
                    Single.just(status.toString())
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
                            ErrorConstants.UNPROCESSABLE_ENTITY_422 ->
                                Single.error(Exception(errorResponse?.message))
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