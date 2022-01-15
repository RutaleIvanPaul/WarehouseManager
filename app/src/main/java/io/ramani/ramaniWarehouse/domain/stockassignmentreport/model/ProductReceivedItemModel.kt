package io.ramani.ramaniWarehouse.domain.stockassignmentreport.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class ProductReceivedItemModel(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val units: String = "",
) : Parcelable {

    class Builder : IBuilder<ProductReceivedItemModel> {
        private var id: String = ""
        private var productId: String = ""
        private var productName: String = ""
        private var quantity: Int = 0
        private var units: String = ""

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


        fun units(units: String): Builder {
            this.units = units
            return this
        }

        fun quantity(quantity: Int): Builder {
            this.quantity = quantity
            return this
        }


        override fun build(): ProductReceivedItemModel =
            ProductReceivedItemModel(
                id,
                productId,
                productName,
                quantity,
                units
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",

        parcel.readInt() ?: 0,
        parcel.readString() ?: ""
    ) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeInt(quantity)
        parcel.writeString(units)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductReceivedItemModel> {
        override fun createFromParcel(parcel: Parcel): ProductReceivedItemModel {
            return ProductReceivedItemModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductReceivedItemModel?> {
            return arrayOfNulls(size)
        }
    }

}