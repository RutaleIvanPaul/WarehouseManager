package io.ramani.ramaniWarehouse.data.entities

import com.google.gson.annotations.SerializedName

data class BasePagedRemoteResponseModel<T>(
    @SerializedName("docs")
    var data: T? = null,
    @SerializedName("totalDocs") var totalDocs: Int? = null,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("totalPages") var totalPages: Int? = null,
    @SerializedName("pagingCounter") var pagingCounter: Int? = null,
    @SerializedName("hasPrevPage") var hasPrevPage: Boolean? = null,
    @SerializedName("hasNextPage") var hasNextPage: Boolean? = null,
    @SerializedName("prevPage") var prevPage: String? = null,
    @SerializedName("nextPage") var nextPage: Int? = null
)