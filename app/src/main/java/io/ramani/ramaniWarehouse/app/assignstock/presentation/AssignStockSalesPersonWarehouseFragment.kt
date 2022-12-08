package io.ramani.ramaniWarehouse.app.assignstock.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.flow.AssignStockFlow
import io.ramani.ramaniWarehouse.app.assignstock.flow.AssignStockFlowcontroller
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel.Companion.onStockTakenDateSelectedLiveData
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow.MainNavFlow
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow.MainNavFlowController
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.MainNavViewModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domainCore.date.now
import kotlinx.android.synthetic.main.fragment_sales_person.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.util.*

class AssignStockSalesPersonWarehouseFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> AssignStockSalesPersonViewModel by factory()
    private lateinit var viewModel: AssignStockSalesPersonViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private val mainNavViewModelProvider: (Fragment) -> MainNavViewModel by factory()
    private lateinit var mainNavViewModel: MainNavViewModel

    private lateinit var flow: AssignStockFlow
    private lateinit var warehouseBottomFlow: MainNavFlow

    private val dateFormatter: DateFormatter by instance()
    private var calendar = Calendar.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        mainNavViewModel = mainNavViewModelProvider(this)
        flow = AssignStockFlowcontroller(baseActivity!!, R.id.main_fragment_container)
        warehouseBottomFlow = MainNavFlowController(baseActivity!!)
        subscribeObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales_person, container, false)
    }


    override fun initView(view: View?) {
        super.initView(view)
        return_stock_datepicker_text.text = viewModel.getDate(now())
        pick_date_layout.isEnabled = false
        date_title.text = getString(R.string.assignment_date)

        /**
         * Leaving this in case we need to enable this in the future
         */

//        pick_date_layout.setOnClickListener {
//            DatePickerDialog(
//                requireActivity(),
//                startDateSetListener,
//                // set DatePickerDialog to point to today's date when it loads up
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            ).show()
//        }

        select_assignto_spinner.text = ""
        AssignStockSalesPersonViewModel.selectedSalespersonLiveData.postValue(null)
        AssignStockSalesPersonViewModel.dateStockTakenLiveData.postValue(return_stock_datepicker_text.text.toString())

        select_assignto_spinner.setOnSingleClickListener {
            select_assignee_spinner.visibility = View.GONE
            assignToSalesWarehouseLabel.visibility = View.GONE
            flow.openAssignToBottomSheet()
        }

        select_assignee_spinner.setOnSingleClickListener {
            if(AssignStockSalesPersonViewModel.selectedAssignToOption.equals("Salesperson")) {
                flow.openAssignStockSalesPersonBottomSheet()
            }else{
                warehouseBottomFlow.openWarehousesBottomSheet(false)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        AssignStockSalesPersonViewModel.salesPeopleList.clear()
        viewModel.getAssignToOptions()
        viewModel.getSalespeople()
        select_assignto_spinner.text = ""
        select_assignee_spinner.text = ""
        select_assignee_spinner.visibility = View.GONE
        assignToSalesWarehouseLabel.visibility = View.GONE
    }

    private fun subscribeObservers() {
        var hasDateBeenSelected: Boolean = false

        onStockTakenDateSelectedLiveData.observe(this, {
            if(it == true){
                hasDateBeenSelected = true
            }
        })


        AssignStockSalesPersonViewModel.selectedSalespersonLiveData.observe(this, {

            if (it != null) {
                select_assignee_spinner.text = it
                if(onStockTakenDateSelectedLiveData.value == true) {
                    AssignStockViewModel.allowToGoNext.postValue(Pair(0, true))
                    onStockTakenDateSelectedLiveData.postValue(false)
                }
                else{
                    Toast.makeText(requireContext(), "Please ensure to select the date above", Toast.LENGTH_LONG)
                }
            } else {
                AssignStockViewModel.allowToGoNext.postValue(Pair(0, false))
            }
        })

        MainNavViewModel.onWarehousesSelectedLiveData.observe(this,{
            select_assignee_spinner.text = it.first
            AssignStockViewModel.allowToGoNext.postValue(Pair(0, true))
        })

        AssignStockSalesPersonViewModel.selectedAssignToOptionLiveData.observe(this,{
            if (it.equals("Salesperson")){
                AssignStockSalesPersonViewModel.selectedAssignToOption = "Salesperson"
                assignToSalesWarehouseLabel.visibility = View.VISIBLE
                assignToSalesWarehouseLabel.text = "Salesperson"
                select_assignee_spinner.visibility = View.VISIBLE
                select_assignto_spinner.text = "Salesperson"
                select_assignee_spinner.hint = "Select Salesperson"
                AssignedItemDetails.isWarehouseAssignment = false
            }
            else{
                AssignStockSalesPersonViewModel.selectedAssignToOption = "Warehouse"
                assignToSalesWarehouseLabel.visibility = View.VISIBLE
                assignToSalesWarehouseLabel.text = "Warehouse"
                select_assignee_spinner.visibility = View.VISIBLE
                select_assignto_spinner.text = "Warehouse"
                select_assignee_spinner.hint = "Select Warehouse"
                AssignedItemDetails.isWarehouseAssignment = true
            }

        })
    }

    private fun updateStartPickDate() {
        val timeString = dateFormatter.convertToDateWithDashes1(calendar.time.time)
        AssignStockSalesPersonViewModel.dateStockTakenLiveData.postValue(timeString)
        return_stock_datepicker_text.text = timeString
    }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateStartPickDate()
            viewModel.updateStockTakenDateItem(true)
        }

    override fun getLayoutResId() = R.layout.fragment_sales_person

    companion object {
        @JvmStatic
        fun newInstance() = AssignStockSalesPersonWarehouseFragment()
    }
}