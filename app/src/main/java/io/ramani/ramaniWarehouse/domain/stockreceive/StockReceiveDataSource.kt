package io.ramani.ramaniWarehouse.domain.auth

import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.reactivex.Single

interface StockReceiveDataSource {
    fun getSuppliers(companyId: String, page: Int, size: Int): Single<List<SupplierModel>>

}