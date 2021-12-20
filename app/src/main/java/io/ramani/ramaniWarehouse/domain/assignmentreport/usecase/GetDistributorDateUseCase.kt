package io.ramani.ramaniWarehouse.domain.auth.useCase

import io.ramani.ramaniWarehouse.data.auth.model.DistributorDateRequestModel
import io.ramani.ramaniWarehouse.domain.assignmentreport.AssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.reactivex.Single

class GetDistributorDateUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val assignmentReportDataSource: AssignmentReportDataSource
) : BaseSingleUseCase<List<DistributorDateModel>, DistributorDateRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: DistributorDateRequestModel?): Single<List<DistributorDateModel>> =
        assignmentReportDataSource.getDistributorDate(
            params?.companyId ?: "",
            params?.warehouseId ?: "",
            params?.date ?: "",
            params?.page ?: 0,
            params?.size ?: 0)

}