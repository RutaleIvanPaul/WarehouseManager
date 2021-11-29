package io.ramani.ramaniWarehouse.app.auth.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.auth.models.LoginRequestModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider

class LoginViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val loginUseCase: BaseSingleUseCase<UserModel, LoginRequestModel>,
    private val prefs: PrefsManager

) : BaseViewModel(application, stringProvider, sessionManager) {
    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    val loginActionLiveData = MutableLiveData<UserModel>()
    override fun start(args: Map<String, Any?>) {

    }

    fun login(phone: String?, password: String?) {
        if (phone.isNullOrBlank() || password.isNullOrBlank()) {
            validationResponseLiveData.postValue(
                Pair(
                    !phone.isNullOrBlank(),
                    !password.isNullOrBlank()
                )
            )
        } else {
            validationResponseLiveData.postValue(Pair(first = true, second = true))
            isLoadingVisible = true
            var phoneEnhanced = phone
            if (phoneEnhanced.toCharArray()[0] == '0')
                phoneEnhanced = phoneEnhanced.replaceFirst("0", "")// remove first character
            phoneEnhanced = "255${phoneEnhanced}"
            val single = loginUseCase.getSingle(LoginRequestModel(phoneEnhanced, password))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                prefs.currentUser = it.toString()
                prefs.accessToken = it.token
                loginActionLiveData.postValue(it)
                Log.d("ALLAH", "login SUCCESS: $it")
            }, onError = {
                isLoadingVisible = false
                notifyError(
                    it.message
                        ?: getString(R.string.an_error_has_occured_please_try_again),
                    PresentationError.ERROR_TEXT
                )
//                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
                Log.d("ALLAH", "login ERROR: " + it.message)
            })
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val loginUseCase: BaseSingleUseCase<UserModel, LoginRequestModel>,
        private val prefs: PrefsManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    loginUseCase,
                    prefs
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}