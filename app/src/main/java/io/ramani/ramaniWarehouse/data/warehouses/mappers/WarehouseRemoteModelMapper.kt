package io.ramani.ramaniWarehouse.data.warehouses.mappers

import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseDimensionsRemoteModel
import io.ramani.ramaniWarehouse.data.warehouses.models.WarehouseRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.mappers.mapToWith
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseDimensionsModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel

class WarehouseRemoteModelMapper(private val warehouseDimensionsRemoteModelMapper: ModelMapper<WarehouseDimensionsRemoteModel, WarehouseDimensionsModel>) :
    ModelMapper<WarehouseRemoteModel, WarehouseModel> {
    override fun mapFrom(from: WarehouseRemoteModel): WarehouseModel =
        WarehouseModel(
            from.id,
            from.name,
            from.category,
            from.dimensions?.mapFromWith(warehouseDimensionsRemoteModelMapper)
        )

    override fun mapTo(to: WarehouseModel): WarehouseRemoteModel =
        WarehouseRemoteModel(
            to.id,
            to.name,
            to.category,
            to.dimensions?.mapToWith(warehouseDimensionsRemoteModelMapper)
        )
}