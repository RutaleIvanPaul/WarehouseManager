package io.ramani.ramaniWarehouse.domain.warehouses.manager

import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.disposables.Disposable

interface IWarehouseStateObservable {
    fun subscribeWarehousesLoaded(onWarehousesLoaded: (MutableList<WarehouseModelView>)-> Unit): Disposable
    fun subscribeCurrentWarehouseChanged(onWarehousesChanged: (WarehouseModel?)-> Unit): Disposable
}