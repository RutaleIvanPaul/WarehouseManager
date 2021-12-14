package io.ramani.ramaniWarehouse.app.warehouses.invoices.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domain.warehouses.models.ProductModel
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class InvoiceModelView(

    val invoiceId: String? = null,
    val createdAt: String? = null,
    val distributorName: String? = null,
    val supplierName: String? = null,
    val invoiceAmount: Double? = null,
    val products: List<ProductModel>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createTypedArrayList(ProductModel)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(invoiceId)
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
        private var createdAt: String? = null
        private var distributorName: String? = null
        private var supplierName: String? = null
        private var invoiceAmount: Double? = null
        private var products: List<ProductModel>? = null

        fun invoiceId(invoiceId: String?): Builder {
            this.invoiceId = invoiceId
            return this
        }

        fun createdAt(createdAt: String?): Builder {
            this.createdAt = createdAt
            return this
        }

        fun distributorName(distributorName: String?): Builder {
            this.distributorName = distributorName
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

        fun products(products: List<ProductModel>?): Builder {
            this.products = products
            return this
        }

        override fun build(): InvoiceModelView = InvoiceModelView(
            invoiceId,
            createdAt,
            distributorName,
            supplierName,
            invoiceAmount,
            products
        )

    }

}