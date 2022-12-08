package io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlow
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.BaseBottomSheetDialogFragment
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import kotlinx.android.synthetic.main.fragment_warehouse_list.*
import org.kodein.di.generic.factory

class WarehouseBottomSheetFragment(showCurrent: Boolean): BaseBottomSheetDialogFragment() {
    private val viewModelProvider: (Fragment) -> MainNavViewModel by factory()
    private lateinit var viewModel: MainNavViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AuthFlow
    private var isLoading = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_warehouse_list, container, false)

    private val warehouseAdapter = WarehouseAdapter(if(showCurrent)MainNavViewModel.warehousesList else MainNavViewModel.warehousesList.filter { it.isSelected != true } as MutableList<WarehouseModelView>) {
        viewModel.onWarehouseSelected(it.id ?: "", showCurrent)
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        flow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeOnWarehousesLoaded()
        viewModel.start()
    }


    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun subscribeOnWarehousesLoaded() {
        MainNavViewModel.onWarehousesLoadedLiveData.observe(this) {
            warehouseAdapter.notifyDataSetChanged()
            isLoading = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        warehouse_rv.apply {
            adapter = warehouseAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = layoutManager?.childCount ?: 0
                    val totalItemCount = layoutManager?.itemCount ?: 0
                    val firstVisibleItemPosition =
                        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()


                    if (!isLoading && MainNavViewModel.hasMoreToLoad) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            MainNavViewModel.page++
                            viewModel.loadWarehouses()
                            isLoading = true
                        }
                    }
                }

            })
        }
    }
}