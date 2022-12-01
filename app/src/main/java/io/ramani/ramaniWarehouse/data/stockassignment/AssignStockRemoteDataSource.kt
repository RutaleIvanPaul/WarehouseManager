package io.ramani.ramaniWarehouse.data.stockassignment

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.google.gson.Gson
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.data.common.network.ErrorConstants
import io.ramani.ramaniWarehouse.data.common.network.toErrorResponseModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.data.stockassignment.model.*
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.entities.BaseErrorResponse
import io.ramani.ramaniWarehouse.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.ramani.ramaniWarehouse.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AssignStockRemoteDataSource(
    private val assignStockAPI: AssignStockAPI,
    private val salesPersonRemoteMapper: ModelMapper<SalesPersonRemoteModel, SalesPersonModel>,
    private val productRemoteMapper: ModelMapper<RemoteProductModel, ProductEntity>,
    private val prefs: PrefsManager
) : AssignStockDataSource, BaseRemoteDataSource() {
    private var calendar = Calendar.getInstance()
    private val today: String = calendar.time.toString()
//    val context: Context = TODO()
//    val kodein by closestKodein(context)

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
        val body = createRequestBody(
            postAssignedItems,
            AssignedItemDetails.signatureInfoStoreKeeperFile,
            AssignedItemDetails.signatureInfoSalesPersonFile
        )
        return callSingle(
            assignStockAPI.postAssignedStock(body).flatMap {
                Single.just(it.data)
            }
        )
    }

    override fun postAssignedWarehouseStock(
        body: PostWarehouseAssignedItems,
        warehouseId: String
    ): Single<String> {
        return callSingle(
            assignStockAPI.postAssignedWarehouseStock(body,warehouseId).flatMap {
                Single.just(it.message)
            }
        )
    }

    private fun createRequestBody(
        postAssignedItems: AssignProductsRequestModel, storeKeeperSignature: File?,
        deliveryPersonSignature: File?
    ): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("assigner", postAssignedItems.postAssignmentItem.assigner)
            .addFormDataPart("companyId", postAssignedItems.postAssignmentItem.companyId)
            .addFormDataPart("comment", postAssignedItems.postAssignmentItem.comment)
            .addFormDataPart("dateStockTaken", postAssignedItems.postAssignmentItem.dateStockTaken)
            .addFormDataPart("name", postAssignedItems.postAssignmentItem.name)
            .addFormDataPart("salesPersonUID", postAssignedItems.postAssignmentItem.salesPersonUID)
            .addFormDataPart(
                "stockAssignmentType","assignment"
            )
            .addFormDataPart("warehouseId", postAssignedItems.postAssignmentItem.warehouseId)
            .addFormDataPart(
                "listOfProducts",
                Gson().toJson(postAssignedItems.postAssignmentItem.listOfProducts)
            )
        storeKeeperSignature?.let {
            builder.addFormDataPart(
                "storeKeeperSignature", postAssignedItems.postAssignmentItem.name ?: "sale001",
                RequestBody.create(MediaType.parse("image/jpg"), storeKeeperSignature)
//                saveBitmapQ(postAssignedItems.postAssignmentItem.context, storeKeeperSignature, "storeKeeperSignature")
            )
        }
        deliveryPersonSignature?.let {
            builder.addFormDataPart(
                "salesPersonSignature",
                "salesPersonSignature_file" ?: "del001",
                RequestBody.create(MediaType.parse("image/jpg"), deliveryPersonSignature)
//                saveBitmapQ(postAssignedItems.postAssignmentItem.context, deliveryPersonSignature, "salesPeronSignature")
            )
        }

        return builder.build()
    }


    fun createOwnImageFormData(bitmap: Bitmap?): RequestBody {
        if (AssignedItemDetails.signatureInfoStoreKeeper == null) {

        } else {

        }
        return RequestBody.create(
            MediaType.parse("image/png"),
            AssignStockViewModel.assignedItemDetails.signatureInfoStoreKeeperFile
        )
    }

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
//
//        return builder.build()
//    }

//    fun convertBitMap(bitmap: Bitmap?): Unit{
//        return bitmap?.compress(Bitmap.CompressFormat.PNG, 90, ByteArrayOutputStream());
//    }

//    private fun saveBitmapQ(context: Context?, fileName: String,
//                            bitmap: Bitmap
//    ) : RequestBody {
//        //val relativeLocation = Environment.DIRECTORY_PICTURES
//        var relativeLocation = Environment.DIRECTORY_PICTURES +"/${BuildConfig.APP_NAME}/"
//        //Insted of Environment.DIRECTORY_PICTURES this can be any valid derectory like Environment.DIRECTORY_DCIM, Environment.DIRECTORY_DOWNLOADS etc..
//        // And insted of "KrishanImages" this can be any directory name you want to create for saving images
//        val contentValues = ContentValues()
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName}" + ".jpg") //this is the file name you want to save
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg") // Content-Type
//        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
//
//        val resolver = context?.contentResolver
////    val resolver = appModule.createAnkoContext(app)
//
//        var stream: OutputStream? = null
//        var uri: Uri? = null
//
//        try {
//            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            uri = resolver?.insert(contentUri, contentValues)
//
//            if (uri == null) {
//                throw IOException("Failed to create new MediaStore record.")
//            }
//
//            stream = resolver?.openOutputStream(uri)
//
//            if (stream == null) {
//                throw IOException("Failed to get output stream.")
//            }
//
//            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream) == false) {
//                throw IOException("Failed to save bitmap.")
//            }
//            else{
//                return RequestBody.create(MediaType.parse("image/jpg"), bitmap.bitmaptoFile()!!)
//            }
//        } catch (e: IOException) {
//            if (uri != null) {
//                // Don't leave an orphan entry in the MediaStore
//                resolver?.delete(uri, null, null)
//            }
//
//            throw e
//        } finally {
//            if (stream != null) {
//                stream!!.close()
//            }
//        }
//    }

    private fun saveBitmapQ(
        context: Context?,
        bitmap: Bitmap, fileName: String
    ): RequestBody {
        //val relativeLocation = Environment.DIRECTORY_PICTURES
        var relativeLocation = Environment.DIRECTORY_PICTURES + "/${BuildConfig.APP_NAME}/"
        //Insted of Environment.DIRECTORY_PICTURES this can be any valid derectory like Environment.DIRECTORY_DCIM, Environment.DIRECTORY_DOWNLOADS etc..
        // And insted of "KrishanImages" this can be any directory name you want to create for saving images
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            "$fileName.jpg"
        ) //this is the file name you want to save
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg") // Content-Type
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)

        val resolver = context?.contentResolver
        val file: File
//    val resolver = appModule.createAnkoContext(app)

        var stream: OutputStream? = null
        var uri: Uri? = null

        try {
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            uri = resolver?.insert(contentUri, contentValues)

            if (uri == null) {
                throw IOException("Failed to create new MediaStore record.")
            }

            stream = resolver?.openOutputStream(uri)

            file = File(uri.path)

            return RequestBody.create(MediaType.parse("image/jpg"), file)

        } catch (e: IOException) {
            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver?.delete(uri, null, null)
            }

            throw e
        } finally {
            if (stream != null) {
                stream!!.close()
            }
        }


//        fun saveBitmap(context: Context?, fileName: String,
//                       bitmap: Bitmap
//        ) : RequestBody {
//            var relativeLocation = Environment.DIRECTORY_PICTURES +"/${BuildConfig.APP_NAME}/"
//
//            val contentValues = ContentValues()
//            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName}" + ".jpg") //this is the file name you want to save
//            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg") // Content-Type
//            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
//
//            val resolver = context?.contentResolver
//
//            var stream: OutputStream? = null
//            var uri: Uri? = null
//
//            try {
//                val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                uri = resolver?.insert(contentUri, contentValues)
//
//                if (uri == null) {
//                    throw IOException("Failed to create new MediaStore record.")
//                }
//
//                stream = resolver?.openOutputStream(uri!!)
//
//                if (stream == null) {
//                    throw IOException("Failed to get output stream.")
//                }
//
//                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream) == false) {
//                    throw IOException("Failed to save bitmap.")
//                }
//                else{
//                    return RequestBody.create(MediaType.parse("image/jpg"), bitmap.bitmaptoFile()!!)
//                }
//            } catch (e: IOException) {
//                if (uri != null) {
//                    // Don't leave an orphan entry in the MediaStore
//                    resolver?.delete(uri!!, null, null)
//                }
//
//                throw e
//            } finally {
//                if (stream != null) {
//                    stream!!.close()
//                }
//            }
//        }

    }


}
