package io.ramani.ramaniWarehouse.app.warehouses.invoices.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class InvoiceModelView(

    val invoiceId: String? = null,
    val purchaseOrderId: String? = null,
    val createdAt: String? = null,
    val distributorId: String? = null,
    val distributorName: String? = null,
    val supplierId: String? = null,
    val supplierName: String? = null,
    val invoiceAmount: Double? = null,
    val products: List<ProductModelView>? = null,
    var storeKeeperName: String? = null,
    var deliveryPersonName: String? = null,
    var storeKeeperSign: Bitmap? = null,
    var deliveryPersonSign: Bitmap? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createTypedArrayList(ProductModelView)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(invoiceId)
        parcel.writeString(purchaseOrderId)
        parcel.writeString(createdAt)
        parcel.writeString(distributorName)
        parcel.writeString(supplierName)
        parcel.writeValue(invoiceAmount)
        parcel.writeTypedList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InvoiceModelView> {
        override fun createFromParcel(parcel: Parcel): InvoiceModelView {
            return InvoiceModelView(parcel)
        }

        override fun newArray(size: Int): Array<InvoiceModelView?> {
            return arrayOfNulls(size)
        }
    }

    class Builder : IBuilder<InvoiceModelView> {
        private var invoiceId: String? = null
        private var purchaseOrderId: String? = null
        private var createdAt: String? = null
        private var distributorId: String? = null
        private var distributorName: String? = null
        private var supplierId: String? = null
        private var supplierName: String? = null
        private var invoiceAmount: Double? = null
        private var products: List<ProductModelView>? = null

        fun invoiceId(invoiceId: String?): Builder {
            this.invoiceId = invoiceId
            return this
        }

        fun purchaseOrderId(purchaseOrderId: String?): Builder {
            this.purchaseOrderId = purchaseOrderId
            return this
        }

        fun createdAt(createdAt: String?): Builder {
            this.createdAt = createdAt
            return this
        }

        fun distributorId(distributorId: String?): Builder {
            this.distributorId = distributorId
            return this
        }

        fun distributorName(distributorName: String?): Builder {
            this.distributorName = distributorName
            return this
        }

        fun supplierId(supplierId: String?): Builder {
            this.supplierId = supplierId
            return this
        }

        fun supplierName(supplierName: String?): Builder {
            this.supplierName = supplierName
            return this
        }

        fun invoiceAmount(invoiceAmount: Double?): Builder {
            this.invoiceAmount = invoiceAmount
            return this
        }

        fun products(products: List<ProductModelView>?): Builder {
            this.products = products
            return this
        }

        override fun build(): InvoiceModelView = InvoiceModelView(
            invoiceId,
            purchaseOrderId,
            createdAt,
            distributorId,
            distributorName,
            supplierId,
            supplierName,
            invoiceAmount,
            products
        )

    }

}