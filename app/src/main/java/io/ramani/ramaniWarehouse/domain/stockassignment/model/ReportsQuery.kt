package io.ramani.ramaniWarehouse.domain.stockassignment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.ramani.ramaniWarehouse.domain.base.v2.Params

data class ReportsQuery(
    var startDate: String = "",
    var endDate: String = "",
    @SerializedName("object")
    @Expose
    var _object: String = "In Stock",
    val displayType: String = "Table",
    val dateRange: String = "Custom",
    val timeSegment: String = "DAY",
    val type: String = "aggregate",

    val property: ReportsQueryProperty = ReportsQueryProperty(),
    var filter: List<ReportsQueryFilter> = listOf(),
    val groupBy: ReportsQueryGroupBy = ReportsQueryGroupBy(),
    var companyId: String = "",
    var userId: String = "",
):Params

data class ReportsQueryProperty(
    val property: String = "",
    val funct: String = ""
):Params

data class ReportsQueryFilter(
    val id: String = "",
    val property: String = "Warehouse",
    val function: String = "equals",
    val field: String = "warehouseId",
    var value: String = "Table",    // warehouse id
    var label: String = "",         // this is name of the warehouse
):Params

data class ReportsQueryGroupBy(
    val property: List<String> = listOf()
):Params