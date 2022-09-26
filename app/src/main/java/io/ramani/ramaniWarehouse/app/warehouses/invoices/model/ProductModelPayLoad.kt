package io.ramani.ramaniWarehouse.app.warehouses.invoices.model

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class ProductModelPayLoad(
    var productId: String? = null,
    var productName: String? = null,
    var units: String? = null,
    var qtyAccepted: Int? = null,
    var qtyDeclined: Int? = null,
    var declinedReason: String? = null,
    var temperature: Int? = null,
    var supplierProductId: String? = "",
    var qtyPending: Int? = null,
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    fun copy(productModelView: ProductModelView?) {
        this.productId = productModelView?.productId
        this.productName = productModelView?.productName
        this.qtyDeclined = productModelView?.qtyDeclined?.toInt()
        this.units = productModelView?.units
        this.declinedReason = productModelView?.declinedReason
        this.qtyAccepted = productModelView?.qtyAccepted?.toInt()
        this.supplierProductId = ""
        this.qtyPending = productModelView?.qtyPending?.toInt()
        productModelView?.temperature?.let {
            if (it.isEmpty())
                this.temperature = 0
            else
                this.temperature = it.toInt()
        }
    }

   fun copy(productModelView: SelectedProductModel?) {
        this.productId = productModelView?.productId
        this.productName = productModelView?.productName
        this.qtyDeclined = productModelView?.qtyDeclined
        this.units = productModelView?.units
        this.declinedReason = productModelView?.declinedReason
        this.qtyAccepted = productModelView?.qtyAccepted
        this.temperature = productModelView?.temperature
        this.supplierProductId = ""
        this.qtyPending = productModelView?.qtyPending
    }

    class Builder : IBuilder<ProductModelPayLoad> {
        private var productId: String? = null
        private var productName: String? = null
        private var price: Double? = null
        private var quantity: Double? = null
        private var unit: String? = null
        private var quantityAccepted: Int? = null
        private var quantityDeclined: Int? = null
        private var isReceived: Boolean? = null
        private var declineReason: String? = null
        private var temp: Int? = null
        private var quantityPending: Int? = null

        fun productId(productId: String?): Builder {
            this.productId = productId
            return this
        }

        fun productName(productName: String?): Builder {
            this.productName = productName
            return this
        }

        fun price(price: Double?): Builder {
            this.price = price
            return this
        }

        fun quantity(quantity: Double?): Builder {
            this.quantity = quantity
            return this
        }

        fun unit(unit: String?): Builder {
            this.unit = unit
            return this
        }

        fun quantityAccepted(quantityAccepted: Int?): Builder {
            this.quantityAccepted = quantityAccepted
            return this
        }

        fun quantityDeclined(quantityDeclined: Int?): Builder {
            this.quantityDeclined = quantityDeclined
            return this
        }

        fun isReceived(isReceived: Boolean?): Builder {
            this.isReceived = isReceived
            return this
        }

        fun declineReason(declineReason: String?): Builder {
            this.declineReason = declineReason
            return this
        }

        fun temp(temp: Int?): Builder {
            this.temp = temp
            return this
        }

        fun quantityPending(quantityPending: Int?): Builder {
            this.quantityPending = quantityPending
            return this
        }

        override fun build(): ProductModelPayLoad =
            ProductModelPayLoad(
                productId,
                productName,
                unit,
                quantityAccepted,
                quantityDeclined,
                declineReason,
                temp,
                "",
                quantityPending
            )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(units)
        parcel.writeValue(qtyAccepted)
        parcel.writeValue(qtyDeclined)
        parcel.writeString(declinedReason)
        parcel.writeValue(temperature)
        parcel.writeValue(qtyPending)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModelPayLoad> {
        override fun createFromParcel(parcel: Parcel): ProductModelPayLoad {
            return ProductModelPayLoad(parcel)
        }

        override fun newArray(size: Int): Array<ProductModelPayLoad?> {
            return arrayOfNulls(size)
        }
    }

}