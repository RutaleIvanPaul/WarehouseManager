package io.ramani.ramaniWarehouse.data.auth.mappers

import io.ramani.ramaniWarehouse.data.auth.model.SupplierProductRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.SupplierRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import kotlin.collections.ArrayList

class SupplierRemoteMapper(
    private val supplierProductRemoteMapper: ModelMapper<SupplierProductRemoteModel, SupplierProductModel>,
) : ModelMapper<SupplierRemoteModel, SupplierModel> {
    override fun mapFrom(from: SupplierRemoteModel): SupplierModel {
        val products:ArrayList<SupplierProductModel> = ArrayList()
        for (productRemote in from.products) {
            products.add(supplierProductRemoteMapper!!.mapFrom(productRemote))
        }

        return SupplierModel.Builder()
                .id(from.id)
                .name(from.name)
                .products(products)
                .build()
    }

    override fun mapTo(to: SupplierModel): SupplierRemoteModel {
        val productsRemote:ArrayList<SupplierProductRemoteModel> = ArrayList()
        for (product in to.products) {
            productsRemote.add(supplierProductRemoteMapper!!.mapTo(product))
        }

        return SupplierRemoteModel(
                to.id,
                to.name,
                productsRemote
            )
    }

}