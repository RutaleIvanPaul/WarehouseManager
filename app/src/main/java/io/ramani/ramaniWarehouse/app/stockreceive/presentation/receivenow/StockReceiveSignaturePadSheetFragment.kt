package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import com.williamww.silkysignature.views.SignaturePad.OnSignedListener
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.BaseBottomSheetDialogFragment
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL
import kotlinx.android.synthetic.main.fragment_stock_receive_signature_sheet.*
import org.kodein.di.generic.factory

class StockReceiveSignaturePadSheetFragment : BaseBottomSheetDialogFragment() {
    companion object {
        const val PARAM_STORE_KEEPER_SIGN = "Store_Keeper_Sign"
        const val PARAM_DELIVERY_PERSON_SIGN = "Delivery_Person_Sign"

        fun newInstance(what: String): StockReceiveSignaturePadSheetFragment {
            val fragment = StockReceiveSignaturePadSheetFragment()

            val args = Bundle()
            args.putString("what", what)

            fragment.arguments = args
            return fragment
        }
    }

    private val viewModelProvider: (Fragment) -> StockReceiveNowViewModel by factory()
    private lateinit var viewModel: StockReceiveNowViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    private var isSigned = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_stock_receive_signature_sheet, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeResponse()
        viewModel.start()
    }

    private fun subscribeResponse() {

    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stock_receive_signature_pad.setOnSignedListener(object : OnSignedListener {
            override fun onStartSigning() {}

            override fun onSigned() {
                isSigned = true
            }

            override fun onClear() {
                isSigned = false
            }
        })


        stock_receive_signature_pad_clear.setOnSingleClickListener {
            stock_receive_signature_pad.clear()
        }

        stock_receive_signature_pad_confirm.setOnSingleClickListener {
            if (!isSigned) {
                errorDialog("Please sign and click confirm.")
            } else {
                val bitmap = stock_receive_signature_pad.getCompressedSignatureBitmap(5)

                when (arguments?.getString("what")) {
                    PARAM_STORE_KEEPER_SIGN -> STOCK_RECEIVE_MODEL.signedLiveData.postValue(Pair(PARAM_STORE_KEEPER_SIGN, bitmap))
                    PARAM_DELIVERY_PERSON_SIGN -> STOCK_RECEIVE_MODEL.signedLiveData.postValue(Pair(PARAM_DELIVERY_PERSON_SIGN, bitmap))
                }

                dismiss()
            }
        }

    }
}