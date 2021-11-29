package io.ramani.ramaniWarehouse.data.warehouses.mappers

import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseDimensionsRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseDimensionsModel

class WarehouseDimensionsRemoteModelMapper :
    UniModelMapper<WarehouseDimensionsRemoteModel, WarehouseDimensionsModel> {
    override fun mapFrom(from: WarehouseDimensionsRemoteModel): WarehouseDimensionsModel =
        WarehouseDimensionsModel(from.height, from.width, from.length)
}