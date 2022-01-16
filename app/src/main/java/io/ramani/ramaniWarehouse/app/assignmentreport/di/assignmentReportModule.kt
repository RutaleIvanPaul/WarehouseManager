package io.ramani.ramaniWarehouse.app.assignmentreport.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.assignmentreport.presentation.AssignmentReportViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

/**
 * @description     Assignment Report Module
 *
 * @author          Adrian
 */
val assignmentReportModule = Kodein.Module("assignmentReportModule") {
    import(assignmentReportDataModule)
    import(assignmentReportDomainModule)


    bind<AssignmentReportViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, AssignmentReportViewModel.Factory(
                instance(), instance(), instance(), instance(), instance("getDistributorDateUseCase"), instance()
            )
        ).get(AssignmentReportViewModel::class.java)
    }

}