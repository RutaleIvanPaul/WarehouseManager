package io.ramani.ramaniWarehouse.data.stockassignmentreport.model

import com.google.gson.annotations.SerializedName


data class StockAssignmentReportDistributorDateRemoteModel(
    @SerializedName("_id")
    val id: String = "",

    @SerializedName("__v")
    val __v: String = "",

    @SerializedName("assigner")
    val assigner: String = "",

    @SerializedName("comment")
    val comment: String = "",

    @SerializedName("companyId")
    val companyId: String = "",

    @SerializedName("dateStockTaken")
    val dateStockTaken: String = "",

    @SerializedName("listOfProducts")
    val listOfProducts: List<ProductReceivedItemRemoteModel> = ArrayList(),

    @SerializedName("name")
    val name: String = "",

    @SerializedName("salesPersonUID")
    val salesPersonUID: String = "",

    @SerializedName("stockAssignmentType")
    val stockAssignmentType: String = "",


    @SerializedName("timestamp")
    val timestamp: String = "",

    @SerializedName("storeKeeperSignature")
    val storeKeeperSignature: List<String> = ArrayList(),

    @SerializedName("salesPersonSignature")
    val salesPersonSignature: List<String> = ArrayList(),

    )