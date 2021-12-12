package io.ramani.ramaniWarehouse.domain.warehouses.models

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class ProductModel(
    val productId: String? = null,
    val productName: String? = null,
    val price: Double? = null,
    val quantity: Double? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeValue(price)
        parcel.writeValue(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }

    class Builder : IBuilder<ProductModel> {
        private var productId: String? = null
        private var productName: String? = null
        private var price: Double? = null
        private var quantity: Double? = null

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

        override fun build(): ProductModel =
            ProductModel(productId, productName, price, quantity)
    }

}