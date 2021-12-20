package io.ramani.ramaniWarehouse.domain.auth.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder
import com.google.gson.Gson

data class DistributorDateModel(
    val id: String = "",
    val supplierName: String = "",
    val date: String = "",
    val time: String = "",

    val items: List<GoodsReceivedItemModel> = ArrayList(),

    val storeKeeperSignature: String = "",
    val deliveryPersonSignature: String = "",

    val deliveryPersonName: String = "",
    val warehouseManagerName: String = "",
) : Parcelable {

    class Builder : IBuilder<DistributorDateModel> {
        private var id: String = ""
        private var supplierName: String = ""
        private var date: String = ""
        private var time: String = ""

        private var items: List<GoodsReceivedItemModel> = ArrayList()

        private var storeKeeperSignature: String = ""
        private var deliveryPersonSignature: String = ""

        private var deliveryPersonName: String = ""
        private var warehouseManagerName: String = ""

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun supplierName(supplierName: String): Builder {
            this.supplierName = supplierName
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

        fun items(items: List<GoodsReceivedItemModel>): Builder {
            this.items = items
            return this
        }

        fun storeKeeperSignature(storeKeeperSignature: String): Builder {
            this.storeKeeperSignature = storeKeeperSignature
            return this
        }

        fun deliveryPersonSignature(deliveryPersonSignature: String): Builder {
            this.deliveryPersonSignature = deliveryPersonSignature
            return this
        }

        fun deliveryPersonName(deliveryPersonName: String): Builder {
            this.deliveryPersonName = deliveryPersonName
            return this
        }

        fun warehouseManagerName(warehouseManagerName: String): Builder {
            this.warehouseManagerName = warehouseManagerName
            return this
        }

        override fun build(): DistributorDateModel =
            DistributorDateModel(
                id,
                supplierName,
                date, time,
                items,
                storeKeeperSignature, deliveryPersonSignature,
                deliveryPersonName,
                warehouseManagerName
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(GoodsReceivedItemModel) ?: ArrayList(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(supplierName)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeTypedList(items)
        parcel.writeString(storeKeeperSignature)
        parcel.writeString(deliveryPersonSignature)
        parcel.writeString(deliveryPersonName)
        parcel.writeString(warehouseManagerName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DistributorDateModel> {
        override fun createFromParcel(parcel: Parcel): DistributorDateModel {
            return DistributorDateModel(parcel)
        }

        override fun newArray(size: Int): Array<DistributorDateModel?> {
            return arrayOfNulls(size)
        }
    }

}