package io.ramani.ramaniWarehouse.app.auth.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlow
import com.code95.android.app.auth.flow.AuthFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.generic.factory

class LoginFragment : BaseFragment() {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModelProvider: (Fragment) -> LoginViewModel by factory()
    private lateinit var viewModel: LoginViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AuthFlow

    override fun getLayoutResId(): Int = R.layout.fragment_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)
        sign_in_button.setOnSingleClickListener {
            flow.openSigninSheet()
        }

    }
}