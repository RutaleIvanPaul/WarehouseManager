package io.ramani.ramaniWarehouse.data.warehouses.mappers

import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseDimensionsRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseDimensionsModel

class WarehouseDimensionsRemoteModelMapper :
    ModelMapper<WarehouseDimensionsRemoteModel, WarehouseDimensionsModel> {
    override fun mapFrom(from: WarehouseDimensionsRemoteModel): WarehouseDimensionsModel =
        WarehouseDimensionsModel(from.height, from.width, from.length)

    override fun mapTo(to: WarehouseDimensionsModel): WarehouseDimensionsRemoteModel =
        WarehouseDimensionsRemoteModel(to.height, to.width, to.length)
}