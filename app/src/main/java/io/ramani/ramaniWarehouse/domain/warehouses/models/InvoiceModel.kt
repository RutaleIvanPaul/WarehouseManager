package io.ramani.ramaniWarehouse.domain.warehouses.models

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class InvoiceModel(

    val invoiceId: String? = null,
    val purchaseOrderId: String? = null,
    val createdAt: Long? = null,
    val distributorId: String? = null,
    val distributorName: String? = null,
    val supplierId: String? = null,
    val supplierName: String? = null,
    val invoiceAmount: Double? = null,
    val products: List<ProductModel>? = null,
    val serverCreatedAtDateTime: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createTypedArrayList(ProductModel),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(invoiceId)
        parcel.writeString(purchaseOrderId)
        parcel.writeLong(createdAt ?: 0L)
        parcel.writeString(distributorName)
        parcel.writeString(supplierName)
        parcel.writeValue(invoiceAmount)
        parcel.writeTypedList(products)
        parcel.writeString(serverCreatedAtDateTime)
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
        private var purchaseOrderId: String? = null
        private var createdAt: Long? = null
        private var distributorId: String? = null
        private var distributorName: String? = null
        private var supplierId: String? = null
        private var supplierName: String? = null
        private var invoiceAmount: Double? = null
        private var products: List<ProductModel>? = null
        private var serverCreatedAtDateTime:String?=null

        fun invoiceId(invoiceId: String?): Builder {
            this.invoiceId = invoiceId
            return this
        }

        fun purchaseOrderId(purchaseOrderId: String?): Builder {
            this.purchaseOrderId = purchaseOrderId
            return this
        }

        fun createdAt(createdAt: Long?): Builder {
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

        fun products(products: List<ProductModel>?): Builder {
            this.products = products
            return this
        }

        fun serverCreatedAtDateTime(serverCreatedAtDateTime:String?):Builder{
            this.serverCreatedAtDateTime = serverCreatedAtDateTime
            return this
        }

        override fun build(): InvoiceModel = InvoiceModel(
            invoiceId,
            purchaseOrderId,
            createdAt,
            distributorId,
            distributorName,
            supplierId,
            supplierName,
            invoiceAmount,
            products,
            serverCreatedAtDateTime
        )

    }

}