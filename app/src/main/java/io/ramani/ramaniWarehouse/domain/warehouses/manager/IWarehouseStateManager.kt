package io.ramani.ramaniWarehouse.domain.warehouses.manager

import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

object IWarehouseStateManager : IWarehouseStateObservable, IWarehouseStatePublisher {
    private val warehouseListLoadedObservable: PublishSubject<MutableList<WarehouseModelView>> =
        PublishSubject.create()
    private val currentWarehouseChangedObservable: PublishSubject<WarehouseModel?> =
        PublishSubject.create()

    override fun subscribeWarehousesLoaded(onWarehousesLoaded: (MutableList<WarehouseModelView>) -> Unit): Disposable =
        warehouseListLoadedObservable.subscribe {
            onWarehousesLoaded(it)
        }


    override fun subscribeCurrentWarehouseChanged(onWarehousesChanged: (WarehouseModel?) -> Unit): Disposable =
        currentWarehouseChangedObservable.subscribe {
            onWarehousesChanged(it)
        }


    override fun notifyWarehouseListLoaded(warehouseList: MutableList<WarehouseModelView>) {
        warehouseListLoadedObservable.onNext(warehouseList)
    }

    override fun notifyCurrentWarehouseChanged(currentWarehouse: WarehouseModel?) {
        currentWarehouseChangedObservable.onNext(currentWarehouse ?: WarehouseModel())
    }


}