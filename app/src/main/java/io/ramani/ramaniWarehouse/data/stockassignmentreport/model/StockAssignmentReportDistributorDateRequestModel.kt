package io.ramani.ramaniWarehouse.data.stockassignmentreport.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params

data class StockAssignmentReportDistributorDateRequestModel(val salesPersonUID:String, val warehouseId:String, val startDate:String, val endDate:String):Params