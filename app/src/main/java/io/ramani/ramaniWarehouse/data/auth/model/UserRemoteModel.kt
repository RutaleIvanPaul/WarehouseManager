package io.ramani.ramaniWarehouse.data.auth.model

import com.google.gson.annotations.SerializedName

data class UserRemoteModel(
    @SerializedName("fcmToken")
    val fcmToken: String = "",
    @SerializedName("token")
    val token: String = "",
    @SerializedName("accountType")
    val accountType: String = "",
    @SerializedName("companyId")
    val companyId: String = "",
    @SerializedName("companyName")
    val companyName: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("phoneNumber")
    val phoneNumber: String = "",
    @SerializedName("uuid")
    val uuid: String = "",
    @SerializedName("isAdmin")
    val isAdmin: Boolean = false,
    @SerializedName("hasSeenSFAOnboarding")
    val hasSeenSFAOnboarding: Boolean = false,
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("timeZone")
    val timeZone: String = ""
)