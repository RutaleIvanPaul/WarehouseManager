package io.ramani.ramaniWarehouse.app.stockreceive.presentation.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.code95.android.app.auth.flow.StockReceiveFlowController
import com.google.android.material.tabs.TabLayoutMediator
import androidx.lifecycle.Observer
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.warehouses.invoices.presentation.InvoicesFragment
import kotlinx.android.synthetic.main.fragment_signin_sheet.*
import kotlinx.android.synthetic.main.fragment_stock_receive_main.*
import org.kodein.di.generic.factory

class StockReceiveMainFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockReceiveMainFragment()
    }

    private val viewModelProvider: (Fragment) -> StockReceiveMainViewModel by factory()
    private lateinit var viewModel: StockReceiveMainViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeValidationResponse()
        viewModel.start()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)
        initTabLayout()
    }

    private fun initTabLayout() {
        val adapter = AdapterTabPager(activity)
        adapter.addFragment(InvoicesFragment.newInstance(), getString(R.string.ramani_orders))
        adapter.addFragment(StockReceiveMainOthersFragment.newInstance(), getString(R.string.others))

        stock_receive_main_viewpager.adapter = adapter
        stock_receive_main_viewpager.currentItem = 0
        TabLayoutMediator(stock_receive_main_tablayout, stock_receive_main_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }

    private fun subscribeValidationResponse() {
        viewModel.validationResponseLiveData.observe(this, Observer {
            if (it.first && it.second) {
                phone_et.error = null
                password_pwd_et.error = null
                // TODO: nav to main screen
            } else {
                phone_et.error = if (it.first) null else getString(R.string.required_field)
                password_pwd_et.error = if (it.second) null else getString(R.string.required_field)
            }
        })
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    internal inner class AdapterTabPager(activity: FragmentActivity?) : FragmentStateAdapter(activity!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()

        public fun getTabTitle(position : Int): String{
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getItemCount(): Int {
            return mFragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return mFragmentList[position]
        }
    }


}