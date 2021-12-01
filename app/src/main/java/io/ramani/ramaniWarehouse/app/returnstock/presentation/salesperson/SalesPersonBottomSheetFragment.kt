package io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.presentation.LoginViewModel
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.BaseBottomSheetDialogFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_sales_person_bottom_sheet.*
import org.kodein.di.generic.factory

class SalesPersonBottomSheetFragment : BaseBottomSheetDialogFragment() {
    private val viewModelProvider: (Fragment) -> SalesPersonViewModel by factory()
    private lateinit var viewModel: SalesPersonViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales_person_bottom_sheet, container, false)
    }

    companion object {
        fun newInstance() = SalesPersonBottomSheetFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        salespersonnameRV.layoutManager = LinearLayoutManager(this.requireContext())
        salespersonnameRV.adapter = SalespersonBottomSheetRVAdapter(
            arrayOf("January", "February", "March")
        )
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

}