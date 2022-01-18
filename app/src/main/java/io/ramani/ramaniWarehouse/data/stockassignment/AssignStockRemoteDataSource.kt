package io.ramani.ramaniWarehouse.data.stockassignment

import android.graphics.Bitmap
import com.google.gson.Gson
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.data.common.network.ErrorConstants
import io.ramani.ramaniWarehouse.data.common.network.toErrorResponseModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.data.returnStock.model.SalespeopleRemoteModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.*
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.entities.BaseErrorResponse
import io.ramani.ramaniWarehouse.domain.entities.exceptions.*
import io.ramani.ramaniWarehouse.domain.returnStock.ReturnStockDataSource
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.ramani.ramaniWarehouse.domain.stockassignment.usecases.PostAssignedStockUseCase
import io.ramani.ramaniWarehouse.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import io.ramani.ramaniWarehouse.domainCore.observers.DefaultSingleObserver
import io.ramani.ramaniWarehouse.domainCore.presentation.ErrorHandlerView
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AssignStockRemoteDataSource(
    private val assignStockAPI: AssignStockAPI,
    private val salesPersonRemoteMapper: ModelMapper<SalesPersonRemoteModel, SalesPersonModel>,
    private val productRemoteMapper: ModelMapper<RemoteProductModel, ProductEntity>,
    private val prefs: PrefsManager,
    private val postAssignedProductUseCase: PostAssignedStockUseCase,
): AssignStockDataSource, BaseRemoteDataSource() {
    override fun getSalesPerson(companyId: String): Single<List<SalesPersonModel>> =
        callSingle(
            assignStockAPI.getSalesPerson(companyId).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(salesPersonRemoteMapper))
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

    override fun getProducts(companyId: String): Single<List<ProductEntity>> =
        callSingle(
            assignStockAPI.getCompanyProducts(prefs.invalidate_cache_company_products.toString(),companyId).flatMap {
                val data = it.data
                if (data != null){
                    Single.just(data.mapFromWith(productRemoteMapper))
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

    override fun postAssignedStock(postAssignedItems: PostAssignedItems): Single<PostAssignedItemsResponse> =
//        val request = AssignProductsRequestModel(
//            createRequestBody(
//                postAssignedItems
//            )
//        )
//
//        val single = postAssignedProductUseCase.getSingle(request)
        subscribeSingle(
            postAssignedProductUseCase.getSingle(AssignProductsRequestModel(
                createRequestBody(
                    postAssignedItems
                )
            ))
                .flatMap {
            if (it != null){
                Single.just(it)
            }else{
                Single.error(ParseResponseException())
            } } , onError = {

        })

        //val single =
//        callSingle(
//            postAssignedProductUseCase.getSingle(request).flatMap {
//            val data = it.data
//            if (data != null) {
//                Single.just(data)
//            } else {
//                Single.error(ParseResponseException())
//            }
//        }.onErrorResumeNext {
//            if (it is HttpException) {
//                val code = it.code()
//                val errorResponse = it.toErrorResponseModel<BaseErrorResponse<Any>>()
//                when (code) {
//                    ErrorConstants.INPUT_VALIDATION_400,
//                    ErrorConstants.NOT_FOUND_404 ->
//                        Single.error(InvalidLoginException(errorResponse?.message))
//                    ErrorConstants.NOT_AUTHORIZED_403 ->
//                        Single.error(AccountNotActiveException(errorResponse?.message))
//                    else -> Single.error(it)
//                }
//            } else if (it is NotAuthenticatedException) {
//                val message =
//                    if (!it.message.isNullOrBlank()) it.message
//                    else if (it.cause.isNotNull() && !it.cause?.message.isNullOrBlank()) it.cause?.message
//                    else "No active user with those credentials"
//                Single.error(
//                    NotAuthorizedException(
//                        message ?: ""
//                    )
//                )
//
//            } else {
//                Single.error(it)
//            }
//        })

   // }

     fun <T> subscribeSingle(
        single: Single<T>,
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ): Disposable =
        single.retryWhen { errors ->
            errors.flatMap {
                when (it) {
//                    is TokenExpiredException ->
//                        onTokenExpiredError().andThen(Flowable.fromCallable { true })
                    is TokenAlreadyRefreshedException ->
                        Flowable.fromCallable { true }
                    else -> Flowable.error(it)
                }
            }
        }.subscribeWithErrorHandler(onSuccess, onError)

    private fun <T> Single<T>.subscribeWithErrorHandler(
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ): Disposable =
        subscribeWith(DefaultSingleObserver(onSuccess, onError))


    fun postGoodsReceived(storeKeeperSignature: Bitmap?, deliveryPersonSignature: Bitmap?) {
        if (storeKeeperSignature == null) {
            notifyErrorObserver(
                stringProvider.getString(R.string.missing_store_keeper_signature),
                PresentationError.ERROR_TEXT
            )
        } else if (deliveryPersonSignature == null) {
            notifyErrorObserver(
                stringProvider.getString(R.string.missing_delivery_person_signature),
                PresentationError.ERROR_TEXT
            )
        } else {

            isLoadingVisible = true
            val request = GoodsReceivedRequestModel(
                createRequestBody(
                    storeKeeperSignature,
                    deliveryPersonSignature
                )
            )
            val single = postGoodsReceivedUseCase.getSingle(request)
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                postGoodsReceivedActionLiveData.postValue(it)
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(
                    it.message
                        ?: stringProvider.getString(R.string.an_error_has_occured_please_try_again),
                    PresentationError.ERROR_TEXT
                )
            })
        }
    }


    private fun createRequestBody(
        postAssignedItems: PostAssignedItems
    ): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("assigner", postAssignedItems.assigner)
            .addFormDataPart("companyId", postAssignedItems.companyId)
            .addFormDataPart("comment", postAssignedItems.comment)
            .addFormDataPart("dateStockTaken", postAssignedItems.dateStockTaken)
            .addFormDataPart("name", postAssignedItems.name)
            .addFormDataPart("salesPersonUID", postAssignedItems.salesPersonUID)
            .addFormDataPart("stockAssignmentType", postAssignedItems.stockAssignmentType)
            .addFormDataPart("warehouseId", postAssignedItems.warehouseId)
            .addFormDataPart("listOfProducts", Gson().toJson(postAssignedItems.listOfProducts))
        postAssignedItems.signatureInfoStoreKeeper.let {
            builder.addFormDataPart(
                "storeKeeperSignature", postAssignedItems.name ?: "",
                it?.let { it1 -> createImageFormData(it1) }
            )
        }

        postAssignedItems.signatureInfoSalesPerson.let {
            builder.addFormDataPart(
                "deliveryPersonSignature", postAssignedItems.name ?: "",
                it?.let { it1 -> createImageFormData(it1) }
            )
        }
        return builder.build()
    }

}