package io.ramani.ramaniWarehouse.data.stockassignment

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.model.ASSIGNMENT_RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.common.io.toFile
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File

class AssignStockRemoteDataSource(
    private val assignStockAPI: AssignStockAPI,
    private val salesPersonRemoteMapper: ModelMapper<SalesPersonRemoteModel, SalesPersonModel>,
    private val productRemoteMapper: ModelMapper<RemoteProductModel, ProductEntity>,
    private val prefs: PrefsManager
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
            assignStockAPI.getCompanyProducts(
                prefs.invalidate_cache_company_products.toString(),
                companyId
            ).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(productRemoteMapper))
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

    override fun postAssignedStock(postAssignedItems: AssignProductsRequestModel): Single<PostAssignedItemsResponse> {
        if (AssignedItemDetails.signatureInfoStoreKeeper == null) {
            Log.e("11111111122", "null")

        } else {
            Log.e("11111111122", "NOT null")

        }
        val body = createRequestBody(
            postAssignedItems,
            AssignedItemDetails.signatureInfoStoreKeeper,
            AssignedItemDetails.signatureInfoSalesPerson
        )
        return callSingle(
            assignStockAPI.postAssignedStock(body).flatMap {
                Log.e("111111111", it.data.toString())
                Single.just(it.data)
            }
        )
    }

    private fun createRequestBody(
        postAssignedItems: AssignProductsRequestModel, storeKeeperSignature: Bitmap?,
        deliveryPersonSignature: Bitmap?
    ): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("assigner", postAssignedItems.postAssignmentItem.assigner)
            .addFormDataPart("companyId", postAssignedItems.postAssignmentItem.companyId)
            .addFormDataPart("comment", postAssignedItems.postAssignmentItem.comment)
            .addFormDataPart("dateStockTaken", postAssignedItems.postAssignmentItem.dateStockTaken)
            .addFormDataPart("name", postAssignedItems.postAssignmentItem.name)
            .addFormDataPart("salesPersonUID", postAssignedItems.postAssignmentItem.salesPersonUID)
            .addFormDataPart(
                "stockAssignmentType",
                postAssignedItems.postAssignmentItem.stockAssignmentType
            )
            .addFormDataPart("warehouseId", postAssignedItems.postAssignmentItem.warehouseId)
            .addFormDataPart(
                "listOfProducts",
                Gson().toJson(postAssignedItems.postAssignmentItem.listOfProducts)
            )
            .addFormDataPart(
                "storeKeeperSignature",
                postAssignedItems.postAssignmentItem.signatureInfoSalesPerson.toString()
            )
            .addFormDataPart(
                "storeKeeperSignature",
                postAssignedItems.postAssignmentItem.signatureInfoStoreKeeper.toString()
            )

//        storeKeeperSignature?.let {
//            Log.e("111111 bitmap", it.toString())
//            builder.addFormDataPart(
//                "storeKeeperSignature", "jhhjkhjkh",
//                createOwnImageFormData(
//                    ASSIGNMENT_RECEIVE_MODELS.salesSign.value
//                )
//            )
//        }
//        /**
//         *   storeKeeperSignature?.let {
//        builder.addFormDataPart(
//        "storeKeeperSignature", RECEIVE_MODELS.invoiceModelView?.storeKeeperName ?: "",
//        createImageFormData(storeKeeperSignature)
//        )
//        }
//         */
//
//        deliveryPersonSignature?.let {
//            builder.addFormDataPart(
//                "salesPersonSignature", AssignedItemDetails.salespersonName,
//                //                    convertBitmapToFile(ASSIGNMENT_RECEIVE_MODELS.salesSign.value)
//
////                createOwnImageFormData(
////                    //ASSIGNMENT_RECEIVE_MODELS.salesSign.value
//////                    convertBitmapToFile(ASSIGNMENT_RECEIVE_MODELS.salesSign.value)
////
////                )
//            )
//        }
//        return builder.build()
//    }

//    private fun createOwnImageFormData(bitmap: Bitmap?): RequestBody {
////        val bos = ByteArrayOutputStream()
////        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
//        if(AssignedItemDetails.signatureInfoStoreKeeper == null){
//            Log.e("11111111122", "null")
//
//        }
//        else{
//            Log.e("11111111122", "NOT null")
//
//        }
//        return RequestBody.create(MediaType.parse("image/*"), AssignStockViewModel.assignedItemDetails.signatureInfoStoreKeeper?.toFile())
//    }

//    private fun createOwnImageFormData(bitmap: Bitmap?): RequestBody {
//        val bos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
//        return RequestBody.create(MediaType.parse("image/jpg"), bitmap?.toFile())
//    }

//    fun convertBitmapToFile(bitmap: Bitmap): Bitmap {
//        val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + "fileNameToSave")
//        file.createNewFile()
//        // Convert bitmap to byte array
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos) // It can be also saved it as JPEG
//        val bitmapdata = baos.toByteArray()
//    }

        return builder.build()
    }
}
