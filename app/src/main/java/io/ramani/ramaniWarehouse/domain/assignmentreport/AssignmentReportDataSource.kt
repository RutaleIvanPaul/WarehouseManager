package io.ramani.ramaniWarehouse.domain.auth

import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Part

interface AssignmentReportDataSource {
    fun getDistributorDate(companyId: String, warehouseId: String, date: String, page: Int, size: Int): Single<List<SupplierModel>>
}