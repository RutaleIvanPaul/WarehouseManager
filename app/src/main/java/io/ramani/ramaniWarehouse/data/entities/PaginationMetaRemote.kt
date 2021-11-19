package io.ramani.ramaniWarehouse.data.entities

import com.google.gson.annotations.SerializedName

data class PaginationMetaRemote(@SerializedName("total") val total: Int? = 0,
                                @SerializedName("per_page") val perPage: Int? = 0,
                                @SerializedName("current_page") val currentPage: Int? = 0,
                                @SerializedName("last_page") val totalPages: Int? = 0)