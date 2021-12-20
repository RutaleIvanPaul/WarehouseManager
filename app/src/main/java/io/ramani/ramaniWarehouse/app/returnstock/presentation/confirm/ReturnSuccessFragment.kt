package io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import org.kodein.di.generic.factory

class ReturnSuccessFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> ReturnSuccessViewModel by factory()
    private lateinit var viewModel: ReturnSuccessViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }


    override fun getLayoutResId(): Int = R.layout.fragment_return_success

    companion object {
        fun newInstance() =
            ReturnSuccessFragment()
    }
}