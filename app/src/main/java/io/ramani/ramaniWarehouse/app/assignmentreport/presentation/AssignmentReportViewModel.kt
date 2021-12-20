package io.ramani.ramaniWarehouse.app.assignmentreport.presentation

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.auth.model.DistributorDateRequestModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.reactivex.rxkotlin.subscribeBy

class AssignmentReportViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val prefs: PrefsManager,
    private val getDistributorDateUseCase: BaseSingleUseCase<List<DistributorDateModel>, DistributorDateRequestModel>,

    ) : BaseViewModel(application, stringProvider, sessionManager) {
    var userId = ""
    var companyId = ""
    var warehouseId = ""

    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    val getDistributorDateActionLiveData = MutableLiveData<List<DistributorDateModel>>()

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        /*
        val user = getLoggedInUser()
        subscribeSingle(user, onSuccess = {
            getSuppliers(it.companyId, 1, 100)
        })
        */
        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            companyId = it.companyId
        }

        sessionManager.getCurrentWarehouse().subscribeBy {
            warehouseId = it.id ?: ""
        }

    }

    private fun getDistributorDate(date: String, page: Int, size: Int) {
        isLoadingVisible = true

        val single = getDistributorDateUseCase.getSingle(DistributorDateRequestModel(companyId, userId, date, page, size))
        subscribeSingle(single, onSuccess = {
            isLoadingVisible = false

            getDistributorDateActionLiveData.postValue(it)
        }, onError = {
            isLoadingVisible = false
            notifyErrorObserver(it.message
                ?: getString(R.string.an_error_has_occured_please_try_again), PresentationError.ERROR_TEXT)
        })
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val prefs: PrefsManager,
        private val getDistributorDateUseCase: BaseSingleUseCase<List<DistributorDateModel>, DistributorDateRequestModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AssignmentReportViewModel::class.java)) {
                return AssignmentReportViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    prefs,
                    getDistributorDateUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}