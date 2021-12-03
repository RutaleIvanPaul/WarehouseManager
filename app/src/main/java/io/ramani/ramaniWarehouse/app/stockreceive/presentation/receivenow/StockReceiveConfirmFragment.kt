package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.code95.android.app.auth.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import org.kodein.di.generic.factory
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import kotlinx.android.synthetic.main.fragment_stock_receive_confirm.*
import kotlinx.android.synthetic.main.item_product_confirm_row.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog


class StockReceiveConfirmFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockReceiveConfirmFragment()
    }

    private val viewModelProvider: (Fragment) -> StockReceiveNowViewModel by factory()
    private lateinit var viewModel: StockReceiveNowViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_confirm

    private var products: ArrayList<SelectedProductModel>? = null
    private var listAdapter: ProductsRecycledViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)

    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)

        subscribeObservers()
        updateView()
    }

    private fun subscribeObservers() {

    }

    private fun checkIfGoNext() {
        StockReceiveNowViewModel.supplierData?.let {
            if (it.supplier.isNotNull() && !it.documents.isNullOrEmpty())
                StockReceiveNowViewModel.allowToGoNextLiveData.postValue(Pair(2, true))
        }
    }

    fun updateView() {
        // Initialize UI
        if (StockReceiveNowViewModel.supplierData.products != null) {
            StockReceiveNowViewModel.supplierData.products.let {
                products = it as ArrayList<SelectedProductModel>
            }
        }

        // Initialize List View
        stock_receive_confirm_product_list.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            listAdapter = ProductsRecycledViewAdapter(products!!, requireActivity(), R.layout.item_product_confirm_row)
            adapter = listAdapter

            addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        }

        // Stock keeper sign
        stock_receive_confirm_sign_stock_keeper.setOnClickListener {
            val signedName = stock_receive_confirm_stock_keeper_name.text.toString()

            if (signedName.isNotEmpty()) {

            } else {
                errorDialog("Please enter the stock keeper name")
            }
        }
    }

    /**
     * Recycled View Adapter For List
     */
    internal class ProductsRecycledViewAdapter(private val arrayList: ArrayList<SelectedProductModel>,
                             private val context: Context,
                             private val layout: Int) : RecyclerView.Adapter<ProductsRecycledViewAdapter.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsRecycledViewAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ProductsRecycledViewAdapter.ViewHolder, position: Int) {
            holder.bindItems(arrayList[position])
        }

        override fun getItemCount(): Int {
            return arrayList.size
        }

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var isInEditMode = false

            fun bindItems(product: SelectedProductModel) {
                itemView.item_product_confirm_row_product_name.text = product.product?.name ?: ""
                itemView.item_product_confirm_row_agreed_amount.setText(product.accepted.toString())
                itemView.item_product_confirm_row_declined_amount.setText(product.declined.toString())
                itemView.item_product_confirm_row_edit_action.setOnClickListener {
                    isInEditMode = !isInEditMode

                    StockReceiveNowViewModel.updateProductRequestLiveData.postValue(product)
                    //updateItemView()
                }

                //updateItemView()
            }

            private fun updateItemView() {
                itemView.item_product_confirm_row_agreed_amount.isEnabled = isInEditMode
                itemView.item_product_confirm_row_agreed_amount.setBackgroundResource(if (isInEditMode) R.drawable.item_outline_round else R.color.transparent)

                itemView.item_product_confirm_row_declined_amount.isEnabled = isInEditMode
                itemView.item_product_confirm_row_declined_amount.setBackgroundResource(if (isInEditMode) R.drawable.item_outline_round else R.color.transparent)

                itemView.item_product_confirm_row_edit_action.text = context.getString(if (isInEditMode) R.string.done else R.string.edit )
            }
        }
    }

}