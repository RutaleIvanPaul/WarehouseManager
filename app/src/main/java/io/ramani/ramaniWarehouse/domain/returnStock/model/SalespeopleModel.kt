package io.ramani.ramaniWarehouse.domain.returnStock.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class SalespeopleModel(
    val companyId: String = "",
    val id: String = "",
    val isActive: Boolean = false,
    val isAdmin: Boolean = false,
    val name: String = "",
    val phoneNumber: String = "",
    val userType: String = ""
):Parcelable {

    class Builder:IBuilder<SalespeopleModel>{
        private var companyId: String = ""
        private var id: String = ""
        private var isActive: Boolean = false
        private var isAdmin: Boolean = false
        private var name: String = ""
        private var phoneNumber: String = ""
        private var userType: String = ""

        fun companyId(companyId: String):Builder{
            this.companyId = companyId
            return this
        }

        fun id(id: String):Builder{
            this.id = id
            return this
        }

        fun isActive(isActive: Boolean):Builder{
            this.isActive = isActive
            return this
        }

        fun isAdmin(isAdmin: Boolean):Builder{
            this.isAdmin = isAdmin
            return this
        }

        fun name(name: String):Builder{
            this.name = name
            return this
        }

        fun phoneNumber(phoneNumber: String):Builder{
            this.phoneNumber = phoneNumber
            return this
        }

        fun userType(userType: String):Builder{
            this.userType = userType
            return this
        }

        override fun build(): SalespeopleModel  =
            SalespeopleModel(
                companyId,id,isActive,isAdmin,name,phoneNumber,userType
            )

    }
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(companyId)
        parcel?.writeByte(if (isActive) 1 else 0)
        parcel?.writeByte(if (isAdmin) 1 else 0)
        parcel?.writeString(name)
        parcel?.writeString(phoneNumber)
        parcel?.writeString(userType)
    }

    companion object CREATOR : Parcelable.Creator<SalespeopleModel> {
        override fun createFromParcel(parcel: Parcel): SalespeopleModel {
            return SalespeopleModel(parcel)
        }

        override fun newArray(size: Int): Array<SalespeopleModel?> {
            return arrayOfNulls(size)
        }

        override fun toString(): String {
            return Gson().toJson(this)
        }
    }
}