package io.ramani.ramaniWarehouse.data.stockreceive.mappers

import io.ramani.ramaniWarehouse.data.stockreceive.model.SupplierProductRemoteModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper

class SupplierProductRemoteMapper : ModelMapper<SupplierProductRemoteModel, SupplierProductModel> {
    override fun mapFrom(from: SupplierProductRemoteModel): SupplierProductModel =
        SupplierProductModel.Builder()
            .id(from.id)
            .name(from.name)
            .units(from.units)
            .secondaryUnitName(from.secondaryUnitName)
            .hasSecondaryUnitConversion(from.hasSecondaryUnitConversion)
            .build()

    override fun mapTo(to: SupplierProductModel): SupplierProductRemoteModel =
        SupplierProductRemoteModel(
            to.id,
            to.name,
            to.units,
            to.secondaryUnitName,
            to.hasSecondaryUnitConversion
        )
}