package io.ramani.ramaniWarehouse.domain.auth.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder
import com.google.gson.Gson

data class GoodsReceivedItemModel(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val qtyAccepted: Int = 0,
    val qtyDeclined: Int = 0,
    val declinedReason: String = "",
    val temperature: Int = 0
) : Parcelable {

    class Builder : IBuilder<GoodsReceivedItemModel> {
        private var id: String = ""
        private var productId: String = ""
        private var productName: String = ""
        private var qtyAccepted: Int = 0
        private var qtyDeclined: Int = 0
        private var declinedReason: String = ""
        private var temperature: Int = 0

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun productId(productId: String): Builder {
            this.productId = productId
            return this
        }

        fun productName(productName: String): Builder {
            this.productName = productName
            return this
        }

        fun qtyAccepted(qtyAccepted: Int): Builder {
            this.qtyAccepted = qtyAccepted
            return this
        }

        fun qtyDeclined(qtyDeclined: Int): Builder {
            this.qtyDeclined = qtyDeclined
            return this
        }

        fun declinedReason(declinedReason: String): Builder {
            this.declinedReason = declinedReason
            return this
        }

        fun temperature(temperature: Int): Builder {
            this.temperature = temperature
            return this
        }

        override fun build(): GoodsReceivedItemModel =
            GoodsReceivedItemModel(
                id,
                productId,
                productName,
                qtyAccepted,
                qtyDeclined,
                declinedReason,
                temperature
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt()
    ) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeInt(qtyAccepted)
        parcel.writeInt(qtyDeclined)
        parcel.writeString(declinedReason)
        parcel.writeInt(temperature)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoodsReceivedItemModel> {
        override fun createFromParcel(parcel: Parcel): GoodsReceivedItemModel {
            return GoodsReceivedItemModel(parcel)
        }

        override fun newArray(size: Int): Array<GoodsReceivedItemModel?> {
            return arrayOfNulls(size)
        }
    }

}