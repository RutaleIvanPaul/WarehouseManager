package io.ramani.ramaniWarehouse.app.warehouses.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.mappers.WarehouseModelMapper
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.MainNavViewModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider


val warehousesModule = Module("warehousesModule") {

    import(warehousesDataModule)
    import(warehousesDomainModule)

    bind<MainNavViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, MainNavViewModel.Factory(
                instance(), instance(), instance(), instance("loadWarehousesUseCase"), instance(),
                instance()
            )
        ).get(MainNavViewModel::class.java)
    }

    bind<ModelMapper<WarehouseModel, WarehouseModelView>>() with provider {
        WarehouseModelMapper()
    }


}