package io.ramani.ramaniWarehouse.app.returnstock.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.presentation.LoginViewModel
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.return_stock_fragment.*
import org.kodein.di.generic.factory

class ReturnStockFragment : BaseFragment() {

    companion object {
        fun newInstance() = ReturnStockFragment()
    }

    private val viewModelProvider: (Fragment) -> ReturnStockViewModel by factory()
    private lateinit var viewModel: ReturnStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        returnStock_tabLayout.addTab(returnStock_tabLayout.newTab().setText(R.string.salesperson))
        returnStock_tabLayout.addTab(returnStock_tabLayout.newTab().setText(R.string.products))
        returnStock_tabLayout.addTab(returnStock_tabLayout.newTab().setText(R.string.confirm))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.return_stock_fragment, container, false)
    }

    override fun getLayoutResId(): Int {
        TODO("Not yet implemented")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReturnStockViewModel::class.java)
        // TODO: Use the ViewModel
    }

}