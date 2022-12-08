package io.ramani.ramaniWarehouse.data.stockassignment.model

import io.ramani.ramaniWarehouse.domain.base.v2.Params


data class PostWarehouseAssignedItems(
    val assignedTo: String = "",
    val to: String = "",
    val warehouseManagerId: String = "",
    val products: List<WarehouseAssignedProduct> = listOf()
): Params