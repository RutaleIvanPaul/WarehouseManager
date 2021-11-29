package io.ramani.ramaniWarehouse.domain.warehouses.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson

data class WarehouseModel(
    val id: Int? = null,
    val name: String? = null,
    val category: String? = null,
    val dimensions: WarehouseDimensionsModel? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(WarehouseDimensionsModel::class.java.classLoader)
    ) {
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(category)
        parcel.writeParcelable(dimensions, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WarehouseModel> {
        override fun createFromParcel(parcel: Parcel): WarehouseModel {
            return WarehouseModel(parcel)
        }

        override fun newArray(size: Int): Array<WarehouseModel?> {
            return arrayOfNulls(size)
        }
    }
}