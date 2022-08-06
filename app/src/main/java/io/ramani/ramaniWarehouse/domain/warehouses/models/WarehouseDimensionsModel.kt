package io.ramani.ramaniWarehouse.domain.warehouses.models

import android.os.Parcel
import android.os.Parcelable

data class WarehouseDimensionsModel(
    val height: Int? = null,
    val width: Int? = null,
    val length: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(height)
        parcel.writeValue(width)
        parcel.writeValue(length)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WarehouseDimensionsModel> {
        override fun createFromParcel(parcel: Parcel): WarehouseDimensionsModel {
            return WarehouseDimensionsModel(parcel)
        }

        override fun newArray(size: Int): Array<WarehouseDimensionsModel?> {
            return arrayOfNulls(size)
        }
    }

}