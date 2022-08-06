package io.ramani.ramaniWarehouse.domain.auth.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder
import com.google.gson.Gson

data class UserModel(
    val fcmToken: String = "",
    val token: String = "",
    val accountType: String = "",
    val companyId: String = "",
    val companyName: String = "",
    val companyType: String = "",
    val userName: String = "",
    val phoneNumber: String = "",
    val uuid: String = "",
    val isAdmin: Boolean = false,
    val hasSeenSFAOnboarding: Boolean = false,
    val currency: String = "",
    val timeZone: String = ""

) : Parcelable {


    class Builder : IBuilder<UserModel> {
        private var fcmToken: String = ""
        private var token: String = ""
        private var accountType: String = ""
        private var companyId: String = ""
        private var companyName: String = ""
        private var companyType: String = ""
        private var userName: String = ""
        private var phoneNumber: String = ""
        private var uuid: String = ""
        private var isAdmin: Boolean = false
        private var hasSeenSFAOnboarding: Boolean = false
        private var currency: String = ""
        private var timeZone: String = ""

        fun fcmToken(fcmToken: String): Builder {
            this.fcmToken = fcmToken
            return this
        }

        fun phoneNumber(phoneNumber: String): Builder {
            this.phoneNumber = phoneNumber
            return this
        }

        fun token(token: String): Builder {
            this.token = token
            return this
        }

        fun name(name: String): Builder {
            this.userName = name
            return this
        }

        fun accountType(accountType: String): Builder {
            this.accountType = accountType
            return this
        }

        fun companyId(companyId: String): Builder {
            this.companyId = companyId
            return this
        }

        fun companyName(companyName: String): Builder {
            this.companyName = companyName
            return this
        }

        fun companyType(companyType: String): Builder {
            this.companyType = companyType
            return this
        }

        fun uuid(uuid: String): Builder {
            this.uuid = uuid
            return this
        }

        fun isAdmin(isAdmin: Boolean): Builder {
            this.isAdmin = isAdmin
            return this
        }

        fun hasSeenSFAOnboarding(hasSeenSFAOnboarding: Boolean): Builder {
            this.hasSeenSFAOnboarding = hasSeenSFAOnboarding
            return this
        }

        fun currency(currency: String): Builder {
            this.currency = currency
            return this
        }

        fun timeZone(timeZone: String): Builder {
            this.timeZone = timeZone
            return this
        }


        override fun build(): UserModel =
            UserModel(
                fcmToken,
                token,
                accountType,
                companyId,
                companyName,
                companyType,
                userName,
                phoneNumber,
                uuid,
                isAdmin,
                hasSeenSFAOnboarding,
                currency,
                timeZone
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
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fcmToken)
        parcel.writeString(token)
        parcel.writeString(accountType)
        parcel.writeString(companyId)
        parcel.writeString(companyName)
        parcel.writeString(companyType)
        parcel.writeString(userName)
        parcel.writeString(phoneNumber)
        parcel.writeString(uuid)
        parcel.writeByte(if (isAdmin) 1 else 0)
        parcel.writeByte(if (hasSeenSFAOnboarding) 1 else 0)
        parcel.writeString(currency)
        parcel.writeString(timeZone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}