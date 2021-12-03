package io.ramani.ramaniWarehouse.app.warehouses.mainNav.mappers

import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel

class WarehouseModelMapper : ModelMapper<WarehouseModel, WarehouseModelView> {
    override fun mapFrom(from: WarehouseModel): WarehouseModelView =
        WarehouseModelView(from.id, from.name, from.category, from.dimensions, false)

    override fun mapTo(to: WarehouseModelView): WarehouseModel =
        WarehouseModel(to.id, to.name, to.category, to.dimensions)
}