package io.ramani.ramaniWarehouse.domain.stockreceive.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder
import com.google.gson.Gson

data class GoodsReceivedModel(
    val id: String = "",
    val invoiceId: String = "",
    val distributorId: String = "",
    val supplierId: String = "",
    val warehouseId: String = "",
    val warehouseManagerId: String = "",
    val date: String = "",
    val time: String = "",
    val deliveryPersonName: String = "",

    val supportingDocument: List<String> = ArrayList(),
    val storeKeeperSignature: List<String> = ArrayList(),
    val deliveryPersonSignature: List<String> = ArrayList(),

    val items: List<GoodsReceivedItemModel> = ArrayList()
) : Parcelable {

    class Builder : IBuilder<GoodsReceivedModel> {
        private var id: String = ""
        private var invoiceId: String = ""
        private var distributorId: String = ""
        private var supplierId: String = ""
        private var warehouseId: String = ""
        private var warehouseManagerId: String = ""
        private var date: String = ""
        private var time: String = ""
        private var deliveryPersonName: String = ""
        private var supportingDocument: List<String> = ArrayList()
        private var storeKeeperSignature: List<String> = ArrayList()
        private var deliveryPersonSignature: List<String> = ArrayList()
        private var items: List<GoodsReceivedItemModel> = ArrayList()

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun invoiceId(invoiceId: String): Builder {
            this.invoiceId = invoiceId
            return this
        }

        fun distributorId(distributorId: String): Builder {
            this.distributorId = distributorId
            return this
        }

        fun supplierId(supplierId: String): Builder {
            this.supplierId = supplierId
            return this
        }

        fun warehouseId(warehouseId: String): Builder {
            this.warehouseId = warehouseId
            return this
        }

        fun warehouseManagerId(warehouseManagerId: String): Builder {
            this.warehouseManagerId = warehouseManagerId
            return this
        }

        fun date(date: String): Builder {
            this.date = date
            return this
        }

        fun time(time: String): Builder {
            this.time = time
            return this
        }

        fun deliveryPersonName(deliveryPersonName: String): Builder {
            this.deliveryPersonName = deliveryPersonName
            return this
        }

        fun supportingDocument(supportingDocument: List<String>): Builder {
            this.supportingDocument = supportingDocument
            return this
        }

        fun storeKeeperSignature(storeKeeperSignature: List<String>): Builder {
            this.storeKeeperSignature = storeKeeperSignature
            return this
        }

        fun deliveryPersonSignature(deliveryPersonSignature: List<String>): Builder {
            this.deliveryPersonSignature = deliveryPersonSignature
            return this
        }

        fun items(items: List<GoodsReceivedItemModel>): Builder {
            this.items = items
            return this
        }

        override fun build(): GoodsReceivedModel =
            GoodsReceivedModel(
                id,
                invoiceId,
                distributorId, supplierId, warehouseId, warehouseManagerId,
                date, time,
                deliveryPersonName,
                supportingDocument, storeKeeperSignature, deliveryPersonSignature,
                items
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: ArrayList(),
        parcel.createStringArrayList() ?: ArrayList(),
        parcel.createStringArrayList() ?: ArrayList(),
        parcel.createTypedArrayList(GoodsReceivedItemModel) ?: ArrayList()
    ) {
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(invoiceId)
        parcel.writeString(distributorId)
        parcel.writeString(supplierId)
        parcel.writeString(warehouseId)
        parcel.writeString(warehouseManagerId)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(deliveryPersonName)
        parcel.writeStringList(supportingDocument)
        parcel.writeStringList(storeKeeperSignature)
        parcel.writeStringList(deliveryPersonSignature)
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoodsReceivedModel> {
        override fun createFromParcel(parcel: Parcel): GoodsReceivedModel {
            return GoodsReceivedModel(parcel)
        }

        override fun newArray(size: Int): Array<GoodsReceivedModel?> {
            return arrayOfNulls(size)
        }
    }

}