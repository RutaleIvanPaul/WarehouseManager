package io.ramani.ramaniWarehouse.app.assignmentreport.di

import io.ramani.ramaniWarehouse.data.assignmentreport.AssignmentReportApi
import io.ramani.ramaniWarehouse.data.assignmentreport.AssignmentReportLocalDataSource
import io.ramani.ramaniWarehouse.data.assignmentreport.AssignmentReportRemoteDataSource
import io.ramani.ramaniWarehouse.data.assignmentreport.AssignmentReportRepository
import io.ramani.ramaniWarehouse.data.assignmentreport.mappers.DistributorDateRemoteMapper
import io.ramani.ramaniWarehouse.data.assignmentreport.model.DistributorDateRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.domain.assignmentreport.AssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val assignmentReportDataModule = Kodein.Module("assignmentReportDataModule") {

    bind<AssignmentReportApi>() with provider {
        ServiceHelper.createService<AssignmentReportApi>(instance())
    }

    bind<AssignmentReportDataSource>("assignmentReportDataSource") with singleton {
        AssignmentReportRepository(
            instance("remoteAssignmentReportDataSource"),
            instance("localAssignmentReportDataSource")
        )
    }

    bind<AssignmentReportDataSource>("remoteAssignmentReportDataSource") with singleton {
        AssignmentReportRemoteDataSource(
            instance(),
            instance()
        )
    }

    bind<AssignmentReportDataSource>("localAssignmentReportDataSource") with singleton {
        AssignmentReportLocalDataSource(
            instance()
        )
    }

    bind<ModelMapper<DistributorDateRemoteModel, DistributorDateModel>>() with provider {
        DistributorDateRemoteMapper(instance("GoodsReceivedItemRemoteMapper"))
    }
}