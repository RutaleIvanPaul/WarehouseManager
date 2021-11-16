package io.ramani.ramaniWarehouse.app.auth.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.core.domain.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.data.auth.model.LoginRequestModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase

class LoginViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val loginUseCase: BaseSingleUseCase<UserModel, LoginRequestModel>

) : BaseViewModel(application, stringProvider, sessionManager) {
    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
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
            val single = loginUseCase.getSingle(LoginRequestModel(phone, password))
            subscribeSingle(single, onSuccess = {
                Log.d("ALLAH", "login SUCCESS: $it")
            }, onError = {
                Log.d("ALLAH", "login ERROR: " + it.message)
            })
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val loginUseCase: BaseSingleUseCase<UserModel, LoginRequestModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    loginUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}