package io.ramani.ramaniWarehouse.domain.warehouses.models

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class InvoiceModel(

    val invoiceId: String? = null,
    val createdAt: Long? = null,
    val distributorName: String? = null,
    val supplierName: String? = null,
    val invoiceAmount: Double? = null,
    val products: List<ProductModel>? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createTypedArrayList(ProductModel)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(invoiceId)
        parcel.writeLong(createdAt ?: 0L)
        parcel.writeString(distributorName)
        parcel.writeString(supplierName)
        parcel.writeValue(invoiceAmount)
        parcel.writeTypedList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InvoiceModel> {
        override fun createFromParcel(parcel: Parcel): InvoiceModel {
            return InvoiceModel(parcel)
        }

        override fun newArray(size: Int): Array<InvoiceModel?> {
            return arrayOfNulls(size)
        }
    }

    class Builder : IBuilder<InvoiceModel> {
        private var invoiceId: String? = null
        private var createdAt: Long? = null
        private var distributorName: String? = null
        private var supplierName: String? = null
        private var invoiceAmount: Double? = null
        private var products: List<ProductModel>? = null

        fun invoiceId(invoiceId: String?): Builder {
            this.invoiceId = invoiceId
            return this
        }

        fun createdAt(createdAt: Long?): Builder {
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

        override fun build(): InvoiceModel = InvoiceModel(
            invoiceId,
            createdAt,
            distributorName,
            supplierName,
            invoiceAmount,
            products
        )

    }

}