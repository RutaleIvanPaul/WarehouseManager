package io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.mappers

import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.ConfirmProducts
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper

class AssignedItemsMapper : ModelMapper<ProductsUIModel, ConfirmProducts> {
    override fun mapFrom(from: ProductsUIModel): ConfirmProducts =
        ConfirmProducts(
            from._id,
            from.name,
            from.assignedNumber,
            from.selectedUnits,
            supplierProductId = from.supplierProductId
        )

    override fun mapTo(to: ConfirmProducts): ProductsUIModel {
        TODO("Not yet implemented")
    }


}