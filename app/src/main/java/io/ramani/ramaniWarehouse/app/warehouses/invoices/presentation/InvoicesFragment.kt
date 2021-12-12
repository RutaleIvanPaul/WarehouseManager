package io.ramani.ramaniWarehouse.app.warehouses.invoices.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import org.kodein.di.generic.factory

class InvoicesFragment : BaseFragment() {
    companion object {
        fun newInstance() = InvoicesFragment()
    }

    private val viewModelProvider: (Fragment) -> InvoicesViewModel by factory()
    private lateinit var viewModel: InvoicesViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun getLayoutResId(): Int = R.layout.fragment_invoice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()
    }

    override fun initView(view: View?) {
        super.initView(view)

    }
}