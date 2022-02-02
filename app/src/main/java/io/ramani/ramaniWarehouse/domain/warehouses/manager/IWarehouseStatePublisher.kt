package io.ramani.ramaniWarehouse.domain.warehouses.manager

import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel

interface IWarehouseStatePublisher {
    fun notifyWarehouseListLoaded(warehouseList: MutableList<WarehouseModelView>)
    fun notifyCurrentWarehouseChanged(currentWarehouse: WarehouseModel?)
}