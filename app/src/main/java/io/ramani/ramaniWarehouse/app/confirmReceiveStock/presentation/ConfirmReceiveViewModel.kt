package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.io.toFile
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.greaterThan
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelPayLoad
import io.ramani.ramaniWarehouse.data.common.network.HeadersProvider
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.ramani.ramaniWarehouse.domainCore.date.now
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.domainCore.printer.PrinterHelper
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ConfirmReceiveViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val headersProvider: HeadersProvider,
    private val declineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
    private val postGoodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>,
    private val putMoreSupportingDocsUseCase: BaseSingleUseCase<String, GoodsReceivedRequestModel>,
    private val dateFormatter: DateFormatter,
    private val printerHelper: PrinterHelper
) : BaseViewModel(application, stringProvider, sessionManager) {
    var token = ""
    var storeKeeperName = ""
    var loggedInUser = UserModel()
    var currentWarehoues = WarehouseModel()
    val postGoodsReceivedActionLiveData = MutableLiveData<GoodsReceivedModel>()
    val onSupportingDocAdded = MutableLiveData<Boolean>()
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            token = it.token
            storeKeeperName = it.userName
            loggedInUser = it
        }

        sessionManager.getCurrentWarehouse().subscribeBy {
            currentWarehoues = it
        }

        getDeclineReasons()
//        printerHelper.open()
    }

    fun getUrl(purchaseId: String?): Pair<String, Map<String, String>> {
        val url =
            BuildConfig.BASE_URL.plus("purchase/order/get/invoice/for/distributor/pdf?purchaseOrderId=$purchaseId")
        var map = mapOf<String, String>()
        headersProvider.getHeaders().entries.forEach { (key, value) ->
            map += key to value
        }
        return Pair(url, map)
    }

    private fun getDeclineReasons() {
        if (RECEIVE_MODELS.declineReasons.isEmpty()) {
            val single = declineReasonsUseCase.getSingle()
            subscribeSingle(single, onSuccess = {
                RECEIVE_MODELS.declineReasons.clear()
                RECEIVE_MODELS.declineReasons.addAll(it)
            }, onError = {})
        }
    }

    fun validateQty(quantity: Double?, acceptedQty: Double, declinedQty: Double): Boolean =
        quantity!! == (acceptedQty + declinedQty)

    /**
     * Post Goods Received
     */
    fun postGoodsReceived(
        context: Context,
        storeKeeperSignature: Bitmap?,
        deliveryPersonSignature: Bitmap?
    ) {
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
                    deliveryPersonSignature,
                    context
                )
            )
            val single = postGoodsReceivedUseCase.getSingle(request)
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                postGoodsReceivedActionLiveData.postValue(it)
                putMoreSupportingDocs(context, it.id)
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

    fun putMoreSupportingDocs(context: Context, id: String) {
        if (RECEIVE_MODELS.invoiceModelView?.supportingDocs?.size?.greaterThan(0) == true) {
            val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("warehouseStockId", id)
            val bitmap = RECEIVE_MODELS.invoiceModelView?.supportingDocs?.first
            builder.addFormDataPart(
                "supportingDocument", "supportingDocument-${now()}",
                bitmap?.let { createImageFormData(it, context) }
            )
            val request = GoodsReceivedRequestModel(
                builder.build()
            )
            val single = putMoreSupportingDocsUseCase.getSingle(request)

            subscribeSingle(single, onSuccess = {
                RECEIVE_MODELS.invoiceModelView?.supportingDocs?.remove(bitmap)
                putMoreSupportingDocs(context, id)
            }, onError = {
                notifyErrorObserver(
                    it.message
                        ?: stringProvider.getString(R.string.an_error_has_occured_please_try_again),
                    PresentationError.ERROR_TEXT
                )
            })
        }
    }


    fun printBitmap(bitmap: Bitmap) {
        printerHelper.printBitmap(bitmap)
    }

    fun printText(receiptText: String) {
        printerHelper.printText(receiptText)
    }

    fun getNowCalendarDate(): String =
        dateFormatter.convertToCalendarFormatDate(now())

    private fun createRequestBody(
        storeKeeperSignature: Bitmap?,
        deliveryPersonSignature: Bitmap?,
        context: Context
    ): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("invoiceId", RECEIVE_MODELS.invoiceModelView?.invoiceId ?: "")
            .addFormDataPart("warehouseId", currentWarehoues.id ?: "")
            .addFormDataPart("distributorId", RECEIVE_MODELS.invoiceModelView?.distributorId ?: "")
            .addFormDataPart("supplierId", RECEIVE_MODELS.invoiceModelView?.supplierId)
            .addFormDataPart("warehouseManagerId", loggedInUser.uuid)
            .addFormDataPart(
                "time",
                dateFormatter.getServerTimeFromServerDate(RECEIVE_MODELS.invoiceModelView?.serverCreatedAtDateTime) /* "10:39:49" */
            )
            .addFormDataPart(
                "date",
                RECEIVE_MODELS.invoiceModelView?.serverCreatedAtDateTime
                    ?: "" /* "2021-10-19T23:00:00.000Z" */
            )
            .addFormDataPart(
                "items", Gson().toJson(getProductsPayload())
            )
        builder.addFormDataPart("storeKeeperName", RECEIVE_MODELS.invoiceModelView?.storeKeeperName)
        storeKeeperSignature?.let {
            builder.addFormDataPart(
                "storeKeeperSignature", RECEIVE_MODELS.invoiceModelView?.storeKeeperName ?: "",
                createImageFormData(storeKeeperSignature, context)
            )
        }
        builder.addFormDataPart(
            "deliveryPersonName",
            RECEIVE_MODELS.invoiceModelView?.deliveryPersonName ?: ""
        )
        deliveryPersonSignature?.let {
            builder.addFormDataPart(
                "deliveryPersonSignature",
                RECEIVE_MODELS.invoiceModelView?.deliveryPersonName ?: "",
                createImageFormData(deliveryPersonSignature, context)
            )
        }

        return builder.build()
    }

    private fun getProductsPayload(): List<ProductModelPayLoad> {
        val productsPayload = mutableListOf<ProductModelPayLoad>()
        RECEIVE_MODELS.invoiceModelView?.products?.forEach {
            if (it.isReceived == true) {
                val productPayload = ProductModelPayLoad.Builder().build()
                productPayload.copy(it)
                productsPayload.add(productPayload)
            } else {
                // [RI-1099] If this item is not selected on this delivery, it should be contains on posting
                val productPayload = ProductModelPayLoad.Builder().build()
                productPayload.copy(it)

                if (it.status.equals("pending", true)) {
                    productPayload.qtyPending = it.quantity?.toInt()
                    productPayload.qtyAccepted = 0
                    productPayload.qtyDeclined = 0
                } else {
                    productPayload.qtyPending = it.qtyPendingBackup?.toInt()
                    productPayload.qtyAccepted = 0
                    productPayload.qtyDeclined = 0
                }

                productsPayload.add(productPayload)
            }
        }
        return productsPayload
    }

    private fun createImageFormData(bitmap: Bitmap, context: Context): RequestBody {
//        val bos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        return RequestBody.create(MediaType.parse("image/jpg"), bitmap.toFile(context))
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val headersProvider: HeadersProvider,
        private val declineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
        private val postGoodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>,
        private val putMoreSupportingDocsUseCase: BaseSingleUseCase<String, GoodsReceivedRequestModel>,
        private val dateFormatter: DateFormatter,
        private val printerHelper: PrinterHelper
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmReceiveViewModel::class.java)) {
                return ConfirmReceiveViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    headersProvider,
                    declineReasonsUseCase,
                    postGoodsReceivedUseCase,
                    putMoreSupportingDocsUseCase,
                    dateFormatter,
                    printerHelper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}