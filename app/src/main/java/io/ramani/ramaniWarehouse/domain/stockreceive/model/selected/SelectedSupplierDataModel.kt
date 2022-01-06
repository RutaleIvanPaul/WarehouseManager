package io.ramani.ramaniWarehouse.domain.stockreceive.model.selected

import android.graphics.Bitmap
import com.google.gson.Gson
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

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

    fun createRequestBody(warehouseId: String, warehouseManagerId: String, distributorId: String): Map<String, RequestBody> {
        val map:HashMap<String,RequestBody> = HashMap<String, RequestBody>()

        map["warehouseId"] = createTextFormData(warehouseId)
        map["distributorId"] = createTextFormData(distributorId)
        map["warehouseManagerId"] = createTextFormData(warehouseManagerId)
        map["supplierId"] = createTextFormData(supplier!!.id)
        map["time"] = createTextFormData("")
        map["date"] = createTextFormData("")

        // Add product
        map["items[]"] = createTextFormData(Gson().toJson(products))

        // Append document images
        documents?.let {
            map["supportingDocument"] = createImageFormData(it[0])

        }

        // Append store keeper signature
        storeKeeperData?.let {
            map["storeKeeperName"] = createTextFormData(it.name)
            map["storeKeeperSignature"] = createImageFormData(it.bitmap!!)
        }

        // Append delivery person signature
        deliveryPersonData?.let {
            map["deliveryPersonName"] = createTextFormData(it.name)
            map["deliveryPersonSignature"] = createImageFormData(it.bitmap!!)
        }

        return map;
    }

    private fun createTextFormData(value:String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    private fun createImageFormData(filePath: String): RequestBody {
        val file = File(filePath)
        return RequestBody.create(MediaType.parse("multipart/form-data"), file)
    }

    private fun createImageFormData(bitmap: Bitmap): RequestBody {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        return RequestBody.create(MediaType.parse("multipart/form-data"), bos.toByteArray())
    }

}