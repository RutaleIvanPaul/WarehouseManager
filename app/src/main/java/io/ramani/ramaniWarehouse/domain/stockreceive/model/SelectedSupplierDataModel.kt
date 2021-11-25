package io.ramani.ramaniWarehouse.domain.auth.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder
import com.google.gson.Gson
import java.util.*

class SelectedSupplierDataModel {
    var supplier: SupplierModel? = null
    var date: Date? = null
    var documents: List<String>? = null
    var products: List<SupplierProductModel>? = null
}