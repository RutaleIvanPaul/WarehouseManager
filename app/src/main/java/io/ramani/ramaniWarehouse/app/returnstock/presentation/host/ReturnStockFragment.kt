package io.ramani.ramaniWarehouse.app.returnstock.presentation.host

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlow
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlowcontroller
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ConfirmReturnStockFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.products.SelectReturnItemsFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonViewModel
import kotlinx.android.synthetic.main.fragment_return_stock.*
import org.jetbrains.anko.backgroundDrawable
import org.kodein.di.generic.factory

class ReturnStockFragment : BaseFragment() {

    companion object {
        fun newInstance() = ReturnStockFragment()
    }

    private val viewModelProvider: (Fragment) -> ReturnStockViewModel by factory()
    private lateinit var viewModel: ReturnStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: ReturnStockFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    private var salespersonFragment: SalesPersonFragment? = null
    private var productsFragment: SelectReturnItemsFragment? = null
    private var confirmFragment: ConfirmReturnStockFragment? = null


    override fun getLayoutResId() = R.layout.fragment_return_stock


    override fun initView(view: View?) {
        super.initView(view)
        viewModel = ViewModelProvider(this).get(ReturnStockViewModel::class.java)
        flow = ReturnStockFlowcontroller(baseActivity!!, R.id.main_fragment_container)
        initTabLayout()
        subscribeObservers()

        return_stock_host_next_button.setOnClickListener {
            return_stock_viewpager.currentItem++
        }
    }

    private fun subscribeObservers() {
        SalesPersonViewModel.selectedSalespersonLiveData.observe(this, {
            if (it != null) {
                return_stock_host_next_button.apply {
                    isEnabled = true
                    backgroundDrawable =
                        getDrawable(requireContext(), R.drawable.green_stroke_action_button)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_lime_yellow
                        )
                    )
                    ReturnStockViewModel.allowToGoNext.postValue(Pair(0, true))
                }
            } else {
                return_stock_host_next_button.apply {
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

        ReturnStockViewModel.allowToGoNext.observe(this, {
            if (it.second) {
                when (it.first) {
                    0 -> DrawableCompat.setTint(
                        return_stock_host_indicator_0.drawable,
                        ContextCompat.getColor(requireContext(), R.color.ramani_green)
                    );
                    1 -> DrawableCompat.setTint(
                        return_stock_host_indicator_1.drawable,
                        ContextCompat.getColor(requireContext(), R.color.ramani_green)
                    );
                    2 -> DrawableCompat.setTint(
                        return_stock_host_indicator_2.drawable,
                        ContextCompat.getColor(requireContext(), R.color.ramani_green)
                    );
                }
            }
        })

        ReturnStockViewModel.readyToConfirmLiveData.observe(this, { readyToConfirm ->
            if (readyToConfirm) {
                return_stock_host_next_button.text = getText(R.string.done)
                return_stock_host_next_button.setOnClickListener {
                    ReturnStockViewModel.readyToPostLiveData.postValue(true)
                }

            } else {
                return_stock_host_next_button.text = getText(R.string.next)
                return_stock_host_next_button.setOnClickListener {
                    return_stock_viewpager.currentItem++
                }
            }
        })

        ReturnStockViewModel.itemsReturned.observe(this, { itemsReturned ->
            if (itemsReturned) {
                (activity as BaseActivity).navigationManager?.remove(this)
                flow.openReturnSuccess()
            }
        })
    }

    private fun initTabLayout() {
        salespersonFragment = SalesPersonFragment.newInstance()
        productsFragment = SelectReturnItemsFragment.newInstance()
        confirmFragment = ConfirmReturnStockFragment.newInstance()

        val adapter = TabPagerAdapter(activity)
        adapter.addFragment(salespersonFragment!!, getString(R.string.salesperson))
        adapter.addFragment(productsFragment!!, getString(R.string.products))
        adapter.addFragment(confirmFragment!!, getString(R.string.confirm))

        return_stock_viewpager.adapter = adapter
        return_stock_viewpager.currentItem = 0
        return_stock_viewpager.isUserInputEnabled = false

        TabLayoutMediator(return_stock_tablayout, return_stock_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

        return_stock_tablayout.touchables.forEach { it.isClickable = false }

    }

}