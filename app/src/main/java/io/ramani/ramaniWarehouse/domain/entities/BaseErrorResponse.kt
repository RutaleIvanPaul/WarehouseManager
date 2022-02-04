package io.ramani.ramaniWarehouse.domain.entities

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created by Amr on 10/5/17.
 */
open class BaseErrorResponse<T>(@SerializedName("status") var status: Int? = 0,
                                @SerializedName("message") var message: String? = "",
                                @SerializedName("Errors") var data: T? = null) {
    override fun toString(): String = Gson().toJson(this)

    companion object
}