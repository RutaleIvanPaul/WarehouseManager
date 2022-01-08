package io.ramani.ramaniWarehouse.data.returnStock.model



data class PostReturnItemsResponse(
    val assigner: String = "",
    val comment: String = "",
    val companyId: String = "",
    val dateStockTaken: String = "",
    val id: String = "",
    val listOfProducts: List<OfProductsX> = listOf(),
    val name: String = "",
    val salesPersonUID: String = "",
    val stockAssignmentType: String = "",
    val timestamp: String = "",
    val v: Int = 0,
    val warehouseId: String = ""
)