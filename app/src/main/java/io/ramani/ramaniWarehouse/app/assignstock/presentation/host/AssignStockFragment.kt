package io.ramani.ramaniWarehouse.app.assignstock.presentation.host

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.flow.AssignStockFlowcontroller
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.ConfirmAssignedStockFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.model.ASSIGNMENT_RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.CompanyProductsFragment
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlowcontroller
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import kotlinx.android.synthetic.main.fragment_assign_stock.*
import kotlinx.android.synthetic.main.fragment_assign_stock.assign_stock_host_next_button
import kotlinx.android.synthetic.main.fragment_return_stock.*

import org.jetbrains.anko.backgroundDrawable
import org.kodein.di.generic.factory

class AssignStockFragment : BaseFragment() {

    companion object {
        fun newInstance() = AssignStockFragment()
    }

    private val viewModelProvider: (Fragment) -> AssignStockViewModel by factory()
    private lateinit var viewModel: AssignStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AssignStockFlowcontroller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        ASSIGNMENT_RECEIVE_MODELS.resetAssignmentDetails()
        AssignStockSalesPersonViewModel.onStockTakenDateSelectedLiveData.postValue(false)
    }

    private var salespersonFragment: AssignStockSalesPersonFragment? = null
    private var productsFragment: CompanyProductsFragment? = null
    private var confirmAssignmentFragment: ConfirmAssignedStockFragment? = null


    override fun getLayoutResId() = R.layout.fragment_assign_stock


    override fun initView(view: View?) {
        super.initView(view)
        viewModel = ViewModelProvider(this).get(AssignStockViewModel::class.java)
        flow = AssignStockFlowcontroller(baseActivity!!, R.id.main_fragment_container)

        initTabLayout()
        subscribeObservers()
        viewModel.start()
        assign_stock_host_next_button.setOnClickListener {
            when (assign_stock_viewpager.currentItem) {
                0 -> {
                    assign_stock_host_next_button.text = getText(R.string.next)
                    assign_stock_viewpager.currentItem++
                    AssignStockViewModel.allowToGoNext.postValue(Pair(1, false))
                }
                1 -> {
                    assign_stock_host_next_button.text = getText(R.string.done)
                    assign_stock_viewpager.currentItem++
                    AssignStockViewModel.allowToGoNext.postValue(Pair(1, false))
                }
                else -> {
                    assign_stock_host_next_button.text = getText(R.string.done)
                    viewModel.assignStock(requireContext())
                    assign_stock_host_next_button.isEnabled = false
                }
            }
        }
    }

    private fun subscribeObservers() {
        AssignStockViewModel.allowToGoNext.observe(this, {
            if (it.second) {
                when (it.first) {
                    0 -> {
                        DrawableCompat.setTint(
                            assign_stock_host_indicator_0.drawable,
                            ContextCompat.getColor(requireContext(), R.color.ramani_green)
                        )

                        allowToGoNext()

                    }
                    1 -> {
                        DrawableCompat.setTint(
                            assign_stock_host_indicator_1.drawable,
                            ContextCompat.getColor(requireContext(), R.color.ramani_green)
                        )

                        allowToGoNext()
                    }
                    2 -> {
                        DrawableCompat.setTint(
                            assign_stock_host_indicator_2.drawable,
                            ContextCompat.getColor(requireContext(), R.color.ramani_green)
                        )

                        allowToGoNext()
                    }
                }
            } else {
                assign_stock_host_next_button.apply {
                    isEnabled = false
                    backgroundDrawable =
                        getDrawable(requireContext(), R.drawable.grey_stroke_next_action_button)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey_inactive_button_text
                        )
                    )
                }
            }
        })


        AssignStockSalesPersonViewModel.selectedSalespersonLiveData.observe(this, {
            if (it != null) {
                assign_stock_host_next_button.apply {
                    isEnabled = true
                    allowToGoNext()
                    backgroundDrawable =
                        getDrawable(requireContext(), R.drawable.green_stroke_action_button)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_lime_yellow
                        )
                    )
                }
            } else {
                assign_stock_host_next_button.apply {
                    isEnabled = false
                    backgroundDrawable =
                        getDrawable(requireContext(), R.drawable.grey_stroke_next_action_button)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey_inactive_button_text
                        )
                    )
                }
            }
        })

        viewModel.onItemsAssignedLiveData.observe(this, {
            if (it) {
                flow.openAssignSuccess()
                (activity as BaseActivity).navigationManager?.remove(this)
            } else {
                Toast.makeText(requireContext(),
                    requireContext().getString(R.string.an_error_has_occured_with_assignment),
                    Toast.LENGTH_LONG
                ).show()
            }

        })


        AssignStockViewModel.pushBackToStart.observe(this, {
            if (it) {
                assign_stock_viewpager.currentItem = 0
            }
        })
    }

    private fun allowToGoNext() {
        assign_stock_host_next_button.apply {
            isEnabled = true
            backgroundDrawable =
                getDrawable(requireContext(), R.drawable.green_stroke_action_button)
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_lime_yellow
                )
            )
        }
    }


    private fun initTabLayout() {
        salespersonFragment = AssignStockSalesPersonFragment.newInstance()
        productsFragment = CompanyProductsFragment.newInstance()
        confirmAssignmentFragment = ConfirmAssignedStockFragment()

        val adapter = TabPagerAdapter(activity)
        adapter.addFragment(salespersonFragment!!, getString(R.string.salesperson))
        adapter.addFragment(productsFragment!!, getString(R.string.products))
        adapter.addFragment(confirmAssignmentFragment!!, getString(R.string.confirm))

        assign_stock_viewpager.adapter = adapter
        assign_stock_viewpager.currentItem = 0
        assign_stock_viewpager.isUserInputEnabled = false

        TabLayoutMediator(assign_stock_tablayout, assign_stock_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

        assign_stock_tablayout.touchables.forEach { it.isClickable = false }


    }

}