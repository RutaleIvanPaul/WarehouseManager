package io.ramani.ramaniWarehouse.app.assignmentreport.di

import io.ramani.ramaniWarehouse.data.auth.*
import io.ramani.ramaniWarehouse.data.auth.mappers.DistributorDateRemoteMapper
import io.ramani.ramaniWarehouse.data.auth.mappers.GoodsReceivedItemRemoteMapper
import io.ramani.ramaniWarehouse.data.auth.mappers.GoodsReceivedRemoteMapper
import io.ramani.ramaniWarehouse.data.auth.mappers.UserRemoteMapper
import io.ramani.ramaniWarehouse.data.auth.model.DistributorDateRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedItemRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedRemoteModel
import io.ramani.ramaniWarehouse.data.auth.models.UserRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.domain.assignmentreport.AssignmentReportDataSource
import io.ramani.ramaniWarehouse.domain.auth.AuthDataSource
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.manager.SessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
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

    bind<ISessionManager>() with singleton {
        SessionManager(instance("assignmentReportDataSource"))
    }
}