package io.ramani.ramaniWarehouse.app.assignstock.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.BaseBottomSheetDialogFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_sales_person_bottom_sheet.*
import org.kodein.di.generic.factory

class AssignStockSalesPersonBottomSheetFragment : BaseBottomSheetDialogFragment() {
    private lateinit var salespersonBottomSheetRVAdapter: AssignStockSalesPersonBottomSheetRVAdapter
    private val viewModelProvider: (Fragment) -> AssignStockSalesPersonViewModel by factory()
    private lateinit var viewModel: AssignStockSalesPersonViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        salespersonBottomSheetRVAdapter =
            AssignStockSalesPersonBottomSheetRVAdapter(AssignStockSalesPersonViewModel.salesPeopleList) {
                viewModel.onSalesPersonSelected(it)
                dismiss()
            }
        viewModel.getSalespeople()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales_person_bottom_sheet, container, false)
    }


    companion object {
        fun newInstance() = AssignStockSalesPersonBottomSheetFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        salespersonnameRV.layoutManager = LinearLayoutManager(this.requireContext())
        salespersonnameRV.adapter = salespersonBottomSheetRVAdapter
        subscribeObservers()
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }


    private fun subscribeObservers() {
        AssignStockSalesPersonViewModel.onSalesPeopleLoadedLiveData.observe(
            this,
            {
                salespersonBottomSheetRVAdapter.notifyDataSetChanged()
            }
        )

    }

}