package io.ramani.ramaniWarehouse.data.entities

import com.google.gson.annotations.SerializedName

/**
 * Created by Amr on 9/14/17.
 */
open class BaseResponse<T>(@SerializedName("Status") var status: String? = "",
                           @SerializedName("Message") var message: String? = "",
                           @SerializedName("data") var data: T? = null,
                           @SerializedName("meta") var meta: PaginationMetaRemote? = null) {
    companion object {
        fun <T> buildSuccessResponse(status: String? = "Success", message: String? = "Success",
                                     data: T) =
                BaseResponse(status, message, data)

        fun <T> buildSuccessEmptyDataResponse(status: String? = "Success", message: String? = "Success") =
                BaseResponse<T>(status, message, null)
    }
}