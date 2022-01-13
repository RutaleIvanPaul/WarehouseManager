package io.ramani.ramaniWarehouse.data.stockassignment.model



data class PostAssignedItemsResponse(
    val assigner: String = "",
    val comment: String = "",
    val companyId: String = "",
    val dateStockTaken: String = "",
    val id: String = "",
    val listOfProducts: List<ConfirmProducts> = listOf(),
    val name: String = "",
    val salesPersonUID: String = "",
    val stockAssignmentType: String = "",
    val timestamp: String = "",
    val v: Int = 0,
    val warehouseId: String = ""
)