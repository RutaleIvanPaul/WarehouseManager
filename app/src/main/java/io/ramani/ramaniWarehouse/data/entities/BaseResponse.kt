package io.ramani.ramaniWarehouse.data.entities

import com.google.gson.annotations.SerializedName

/**
 * Created by Amr on 9/14/17.
 */
open class BaseResponse<T>(@SerializedName("Status") var status: Long? = 0,
                           @SerializedName("message") var message: String? = "",
                           @SerializedName("response") var data: T? = null,
                           @SerializedName("meta") var meta: PaginationMetaRemote? = null) {
    companion object {
        fun <T> buildSuccessResponse(status: Long? = 0, message: String? = "Success",
                                     data: T) =
                BaseResponse(status, message, data)

        fun <T> buildSuccessEmptyDataResponse(status: Long? = 0, message: String? = "Success") =
                BaseResponse<T>(status, message, null)
    }
}