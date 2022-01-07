package io.ramani.ramaniWarehouse.domain.stockreceive.model.selected

import android.annotation.SuppressLint
import android.graphics.Bitmap
import com.google.gson.Gson
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import okhttp3.MultipartBody
import java.text.SimpleDateFormat

class SelectedSupplierDataModel {
    var supplier: SupplierModel? = null
    var date: Date? = null
    var documents: List<String>? = null

    var products: List<SelectedProductModel>? = null

    var storeKeeperData: SignatureInfo? = null
    var deliveryPersonData: SignatureInfo? = null

    fun clear() {
        supplier = null
        date = null

        documents = null
        products = null
        storeKeeperData = null
        deliveryPersonData = null
    }

    @SuppressLint("SimpleDateFormat")
    fun createRequestBody(warehouseId: String, warehouseManagerId: String, distributorId: String): RequestBody {
        val confirmDate = date ?: Date()

        val time = SimpleDateFormat("HH:mm:ss").format(confirmDate)
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(confirmDate)

        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("invoiceId", "")
            .addFormDataPart("warehouseId", warehouseId)
            .addFormDataPart("distributorId", distributorId)
            .addFormDataPart("supplierId", supplier!!.id)
            .addFormDataPart("warehouseManagerId", warehouseManagerId)
            .addFormDataPart("time", time /* "10:39:49" */)
            .addFormDataPart("date", date /* "2021-10-19T23:00:00.000Z" */)
            .addFormDataPart("items", Gson().toJson(products))

        documents?.let {
            builder.addFormDataPart(
                "supportingDocument", "",
                RequestBody.create(
                    MediaType.parse("application/octet-stream"),
                    File(it[0])
                )
            )
        }

        storeKeeperData?.let {
            builder.addFormDataPart("storeKeeperName", it.name)
            builder.addFormDataPart(
                "storeKeeperSignature", "",
                createImageFormData(it.bitmap!!)
            )
        }

        deliveryPersonData?.let {
            builder.addFormDataPart("deliveryPersonName", it.name)
            builder.addFormDataPart(
                    "deliveryPersonSignature", "",
                        createImageFormData(it.bitmap!!)
                )
        }

        return builder.build();
    }

    private fun createImageFormData(bitmap: Bitmap): RequestBody {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        return RequestBody.create(MediaType.parse("application/octet-stream"), bos.toByteArray())
    }

}