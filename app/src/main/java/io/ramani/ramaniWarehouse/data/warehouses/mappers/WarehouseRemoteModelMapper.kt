package io.ramani.ramaniWarehouse.data.warehouses.mappers

import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseDimensionsRemoteModel
import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseDimensionsModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel

class WarehouseRemoteModelMapper(private val warehouseDimensionsRemoteModelMapper: UniModelMapper<WarehouseDimensionsRemoteModel, WarehouseDimensionsModel>) :
    UniModelMapper<WarehouseRemoteModel, WarehouseModel> {
    override fun mapFrom(from: WarehouseRemoteModel): WarehouseModel =
        WarehouseModel(
            from.id,
            from.name,
            from.category,
            from.dimensions?.mapFromWith(warehouseDimensionsRemoteModelMapper)
        )
}