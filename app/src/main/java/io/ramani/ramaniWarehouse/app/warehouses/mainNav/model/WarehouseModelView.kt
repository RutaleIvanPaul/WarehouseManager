package io.ramani.ramaniWarehouse.app.warehouses.mainNav.model

import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseDimensionsModel

data class WarehouseModelView(
     val id: Int? = null,
     val name: String? = null,
     val category: String? = null,
     val dimensions: WarehouseDimensionsModel? = null,
     var isSelected: Boolean? = null
)