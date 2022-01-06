package io.ramani.ramaniWarehouse.domain.stockreceive.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder
import com.google.gson.Gson

data class SupplierProductModel(
     val id: String = "",
     val name: String = "",
     val units: String = "",
     val secondaryUnitName: String = "",
     val hasSecondaryUnitConversion: Boolean = false,
) : Parcelable {


    class Builder : IBuilder<SupplierProductModel> {
        private var id: String = ""
        private var name: String = ""
        private var units: String = ""
        private var secondaryUnitName: String = ""
        private var hasSecondaryUnitConversion: Boolean = false

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun units(units: String): Builder {
            this.units = units
            return this
        }

        fun secondaryUnitName(secondaryUnitName: String): Builder {
            this.secondaryUnitName = secondaryUnitName
            return this
        }

        fun hasSecondaryUnitConversion(hasSecondaryUnitConversion: Boolean): Builder {
            this.hasSecondaryUnitConversion = hasSecondaryUnitConversion
            return this
        }

        override fun build(): SupplierProductModel =
            SupplierProductModel(
                id,
                name,
                units,
                secondaryUnitName,
                hasSecondaryUnitConversion
            )
    }


    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(units)
        parcel.writeString(secondaryUnitName)
        parcel.writeByte(if (hasSecondaryUnitConversion) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SupplierProductModel> {
        override fun createFromParcel(parcel: Parcel): SupplierProductModel {
            return SupplierProductModel(parcel)
        }

        override fun newArray(size: Int): Array<SupplierProductModel?> {
            return arrayOfNulls(size)
        }
    }

}