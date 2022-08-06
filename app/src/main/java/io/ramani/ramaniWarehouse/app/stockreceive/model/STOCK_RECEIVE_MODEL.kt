package io.ramani.ramaniWarehouse.app.stockreceive.model

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import io.ramani.ramaniWarehouse.domain.base.SingleLiveEvent
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedSupplierDataModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SignatureInfo
import java.util.*
import kotlin.collections.ArrayList

/**
 * This is shared model class for stock receive feature
 */
class STOCK_RECEIVE_MODEL {
    companion object {
        const val DATA_SUPPLIER = 1000
        const val DATA_DOCUMENTS = 1001
        const val DATA_DATE = 1002
        const val DATA_PRODUCTS = 1003
        const val DATA_STORE_KEEPER_DATA = 1004
        const val DATA_DELIVERY_PERSON_DATA = 1005

        var supplierData = SelectedSupplierDataModel()                                  // Selected Supplier Data
        var allowToGoNextLiveData = MutableLiveData<Pair<Int, Boolean>>()               // Allow event to go next on each page
        var updateProductRequestLiveData = MutableLiveData<SelectedProductModel>()      // Update product request
        var updateProductCompletedLiveData = MutableLiveData<SelectedProductModel>()    // Update product request

        var signedLiveData = MutableLiveData<Pair<String, Bitmap>>()                     // Event triggered when sign is completed

        /**
         * Set any data
         */
        fun setData(what: Int, value: Any) {
            when (what) {
                DATA_SUPPLIER -> {
                    supplierData.supplier = value as SupplierModel
                }
                DATA_DATE -> supplierData.date = value as Date
                DATA_DOCUMENTS -> supplierData.documents = value as ArrayList<String>
                DATA_PRODUCTS -> supplierData.products = value as ArrayList<SelectedProductModel>

                DATA_STORE_KEEPER_DATA -> supplierData.storeKeeperData = value as SignatureInfo
                DATA_DELIVERY_PERSON_DATA -> supplierData.deliveryPersonData = value as SignatureInfo
            }
        }

        fun clearData() {
            // Clear global data
            supplierData.clear()
            allowToGoNextLiveData = MutableLiveData<Pair<Int, Boolean>>()
            updateProductRequestLiveData = MutableLiveData<SelectedProductModel>()
            updateProductCompletedLiveData = MutableLiveData<SelectedProductModel>()
            signedLiveData = MutableLiveData<Pair<String, Bitmap>>()

            System.gc()
        }
    }
}