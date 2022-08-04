package io.ramani.ramaniWarehouse.app.warehouses.invoices.model

import android.os.Parcel
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class ProductModelView(
    var productId: String? = null,
    var productName: String? = null,
    var price: Double? = null,
    var quantity: Double? = null,
    var units: String? = null,
    var qtyAccepted: Double? = null,
    var qtyDeclined: Double? = null,
    var isReceived: Boolean? = null,
    var declinedReason: String? = null,
    var temperature: String? = null,
    var status: String? = null,
    var qtyPending: Double? = null,
    var qtyPendingBackup: Double? = null,
    var viewType: Int = TYPE.PRODUCT
) : Parcelable, MultiItemEntity {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
    ) {
    }

    fun copy(productModelView: ProductModelView?) {
        this.productId = productModelView?.productId
        this.productName = productModelView?.productName
        this.qtyDeclined = productModelView?.qtyDeclined
        this.units = productModelView?.units
        this.viewType = productModelView?.viewType ?: TYPE.PRODUCT
        this.declinedReason = productModelView?.declinedReason
        this.isReceived = productModelView?.isReceived
        this.price = productModelView?.price
        this.qtyAccepted = productModelView?.qtyAccepted
        this.temperature = productModelView?.temperature
        this.status = productModelView?.status
        this.qtyPending = productModelView?.qtyPending
        this.qtyPendingBackup = productModelView?.qtyPendingBackup
    }

    class Builder : IBuilder<ProductModelView> {
        private var productId: String? = null
        private var productName: String? = null
        private var price: Double? = null
        private var quantity: Double? = null
        private var unit: String? = null
        private var quantityAccepted: Double? = null
        private var quantityDeclined: Double? = null
        private var isReceived: Boolean? = null
        private var declineReason: String? = null
        private var temp: String? = null
        private var status: String? = null
        private var quantityPending: Double? = null
        private var quantityPendingBackup: Double? = null
        private var viewType: Int = TYPE.PRODUCT

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

        fun quantityAccepted(quantityAccepted: Double?): Builder {
            this.quantityAccepted = quantityAccepted
            return this
        }

        fun quantityDeclined(quantityDeclined: Double?): Builder {
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

        fun temp(temp: String?): Builder {
            this.temp = temp
            return this
        }

        fun status(status: String?): Builder {
            this.status = status
            return this
        }

        fun quantityPending(quantityPending: Double?): Builder {
            this.quantityPending = quantityPending
            return this
        }

        fun quantityPendingBackup(quantityPendingBackup: Double?): Builder {
            this.quantityPendingBackup = quantityPendingBackup
            return this
        }

        fun viewType(viewType: Int): Builder {
            this.viewType = viewType
            return this
        }

        override fun build(): ProductModelView =
            ProductModelView(
                productId,
                productName,
                price,
                quantity,
                unit,
                quantityAccepted,
                quantityDeclined,
                isReceived,
                declineReason,
                temp,
                status,
                quantityPending,
                quantityPendingBackup,
                viewType
            )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeValue(price)
        parcel.writeValue(quantity)
        parcel.writeString(units)
        parcel.writeValue(qtyAccepted)
        parcel.writeValue(qtyDeclined)
        parcel.writeValue(isReceived)
        parcel.writeString(declinedReason)
        parcel.writeString(temperature)
        parcel.writeString(status)
        parcel.writeValue(qtyPending)
        parcel.writeValue(qtyPendingBackup)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModelView> {
        override fun createFromParcel(parcel: Parcel): ProductModelView {
            return ProductModelView(parcel)
        }

        override fun newArray(size: Int): Array<ProductModelView?> {
            return arrayOfNulls(size)
        }
    }

    object TYPE {
        const val LABEL = 0
        const val PRODUCT = 1
    }

    override val itemType: Int
        get() = viewType

}