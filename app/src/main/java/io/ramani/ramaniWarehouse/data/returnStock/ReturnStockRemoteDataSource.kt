package io.ramani.ramaniWarehouse.data.returnStock

import android.content.Context
import android.graphics.Bitmap
import com.google.gson.Gson
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.data.common.network.ErrorConstants
import io.ramani.ramaniWarehouse.data.common.network.toErrorResponseModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableStockReturnedListItem
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.data.returnStock.model.SalespeopleRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.entities.BaseErrorResponse
import io.ramani.ramaniWarehouse.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniWarehouse.domain.returnStock.ReturnStockDataSource
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class ReturnStockRemoteDataSource(
    private val returnStockApi: ReturnStockApi,
    private val salespeopleRemoteMapper: ModelMapper<SalespeopleRemoteModel, SalespeopleModel>,
    private val prefs: PrefsManager
): ReturnStockDataSource, BaseRemoteDataSource() {
    override fun getSalespeople(companyId: String): Single<List<SalespeopleModel>> =
        callSingle(
            returnStockApi.getSalespeople(companyId).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(salespeopleRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }.onErrorResumeNext {
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
                        else "No active user with those credentials"
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

    override fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockReturnedListItem>>  =
        callSingle(
            returnStockApi.getAvailableStock(prefs.invalidate_cache_available_products.toString(),salesPersonUID).flatMap {
                val data = it.data
                if (data != null){
                    Single.just(data)
                }else{
                    Single.error(ParseResponseException())
                }
            }.onErrorResumeNext {
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
                        else "No active user with those credentials"
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


    override fun postReturnedStock(postReturnItems: PostReturnItems): Single<PostReturnItemsResponse> =
        callSingle(
            returnStockApi.postReturnedStock(
                createRequestBody(postReturnItems)
            ).flatMap {
                val data = it.data
                if (data != null){
                    Single.just(data)
                }else{
                    Single.error(ParseResponseException())
                }
            }.onErrorResumeNext {
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
                        else "No active user with those credentials"
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


    private fun createRequestBody(
        postReturnItems: PostReturnItems
    ): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("assigner", postReturnItems.assigner)
            .addFormDataPart("comment", postReturnItems.comment)
            .addFormDataPart("companyId", postReturnItems.companyId)
            .addFormDataPart("dateStockTaken", postReturnItems.dateStockTaken)
            .addFormDataPart("name", postReturnItems.name)
            .addFormDataPart("salesPersonUID", postReturnItems.salesPersonUID)
            .addFormDataPart("stockAssignmentType", postReturnItems.stockAssignmentType)
            .addFormDataPart("warehouseId", postReturnItems.warehouseId)
            .addFormDataPart("listOfProducts", Gson().toJson(postReturnItems.listOfProducts))

        postReturnItems.signatureInfoStoreKeeperFile?.let {
            builder.addFormDataPart(
                "storeKeeperSignature", postReturnItems.assigner,
                createImageFormData(postReturnItems.signatureInfoStoreKeeperFile)
            )
        }
        postReturnItems.signatureInfoSalesPersonFile?.let {
            builder.addFormDataPart(
                "salesPersonSignature",
                postReturnItems.name,
                createImageFormData(postReturnItems.signatureInfoSalesPersonFile)
            )
        }

        return builder.build()
    }

}