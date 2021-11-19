package io.ramani.ramaniWarehouse.domain.entities

import com.google.gson.annotations.SerializedName

/**
 * Created by Amr on 11/22/17.
 */
data class ValidationErrorRemote(@SerializedName("field") val field: String = "",
                                 @SerializedName("errorCode") val errorCode: String = "",
                                 @SerializedName("errorMsg") val errorMsg: String = "")