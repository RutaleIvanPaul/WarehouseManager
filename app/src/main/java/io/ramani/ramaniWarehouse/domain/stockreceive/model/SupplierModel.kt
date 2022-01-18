package io.ramani.ramaniWarehouse.domain.stockreceive.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder
import com.google.gson.Gson

data class SupplierModel(
    val id: String = "",
    val name: String = "",
    val products: List<SupplierProductModel> = ArrayList()

) : Parcelable {


    class Builder : IBuilder<SupplierModel> {
        private var id: String = ""
        private var name: String = ""
        private var products: List<SupplierProductModel> = ArrayList()

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun products(products: List<SupplierProductModel>): Builder {
            this.products = products
            return this
        }

        override fun build(): SupplierModel =
            SupplierModel(
                id,
                name,
                products
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(SupplierProductModel)  ?: ArrayList()
    ) {
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeTypedList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SupplierModel> {
        override fun createFromParcel(parcel: Parcel): SupplierModel {
            return SupplierModel(parcel)
        }

        override fun newArray(size: Int): Array<SupplierModel?> {
            return arrayOfNulls(size)
        }
    }

}