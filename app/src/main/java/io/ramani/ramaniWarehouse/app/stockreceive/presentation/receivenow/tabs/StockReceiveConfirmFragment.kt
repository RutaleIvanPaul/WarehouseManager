package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.gone
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL.Companion.DATA_DELIVERY_PERSON_DATA
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL.Companion.DATA_STORE_KEEPER_DATA
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSignaturePadSheetFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSignaturePadSheetFragment.Companion.PARAM_DELIVERY_PERSON_SIGN
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSignaturePadSheetFragment.Companion.PARAM_STORE_KEEPER_SIGN
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SignatureInfo
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import kotlinx.android.synthetic.main.fragment_stock_receive_confirm.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.textColor
import org.kodein.di.generic.factory
import java.io.File
import java.util.*

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
    private var listAdapter: StockReceiveConfirmProductRVAdapter? = null

    private var capturedImage: File? = null
    private var capturePhotoActivityResult: ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()

        subscribeObservers()
        setupOnActivityResult()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)
        setupDeliveryPersonSignature()
    }

    private fun setupDeliveryPersonSignature() {

        radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.signature_radio_button -> {
                    stock_receive_confirm_sign_delivery_person.text = getString(R.string.sign)
                    resetButtonStyle()
                    resetData()
                }

                R.id.doc_radio_button -> {
                    stock_receive_confirm_sign_delivery_person.text = getString(R.string.capture)
                    resetButtonStyle()
                    resetData()
                }
            }
        }
    }

    private fun resetData() {
        delivery_person_captured_image_preview.gone()
        val deliveryPersonName = stock_receive_confirm_delivery_person_name.text.toString()
        STOCK_RECEIVE_MODEL.setData(
            DATA_DELIVERY_PERSON_DATA,
            SignatureInfo(deliveryPersonName, null)
        )
        capturedImage = null
    }

    private fun resetButtonStyle() {
        stock_receive_confirm_sign_delivery_person.textColor =
            ContextCompat.getColor(requireContext(), R.color.secondary_blue)
        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stock_receive_confirm_sign_delivery_person.backgroundDrawable =
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.blue_stroke_action_button
                )

        } else {
            stock_receive_confirm_sign_delivery_person.background =
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.blue_stroke_action_button
                )
        }
    }


    override fun onResume() {
        super.onResume()

        updateView()
    }

    private fun subscribeObservers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel, this)
        subscribeError(viewModel)
        observerError(viewModel, this)

        STOCK_RECEIVE_MODEL.signedLiveData.observe(this) {

            if (it.first == PARAM_STORE_KEEPER_SIGN) {
                stock_receive_confirm_store_keeper_name.isEnabled = false

                stock_receive_confirm_sign_store_keeper.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                stock_receive_confirm_sign_store_keeper.setBackgroundResource(R.drawable.green_stroke_action_button)
                stock_receive_confirm_sign_store_keeper.setText(R.string.signed)
                stock_receive_confirm_sign_store_keeper.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )

                val storeKeeperName = stock_receive_confirm_store_keeper_name.text.toString()
                STOCK_RECEIVE_MODEL.setData(
                    DATA_STORE_KEEPER_DATA,
                    SignatureInfo(storeKeeperName, it.second)
                )
                store_keeper_captured_image_preview.visible()
                store_keeper_captured_image_preview.setImageBitmap(it.second)
            } else if (it.first == PARAM_DELIVERY_PERSON_SIGN) {

                stock_receive_confirm_delivery_person_name.isEnabled = false

                stock_receive_confirm_sign_delivery_person.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                stock_receive_confirm_sign_delivery_person.setBackgroundResource(R.drawable.green_stroke_action_button)
                stock_receive_confirm_sign_delivery_person.setText(R.string.signed)
                stock_receive_confirm_sign_delivery_person.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )

                val deliveryPersonName = stock_receive_confirm_delivery_person_name.text.toString()
                STOCK_RECEIVE_MODEL.setData(
                    DATA_DELIVERY_PERSON_DATA,
                    SignatureInfo(deliveryPersonName, it.second)
                )

                delivery_person_captured_image_preview.visible()
                delivery_person_captured_image_preview.setImageBitmap(it.second)

            }

            checkIfGoNext()
        }
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        stock_receive_confirm_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun checkIfGoNext() {
        STOCK_RECEIVE_MODEL.supplierData?.let {
            if (it.supplier.isNotNull() && it.storeKeeperData.isNotNull() && it.deliveryPersonData.isNotNull())
                STOCK_RECEIVE_MODEL.allowToGoNextLiveData.postValue(Pair(2, true))
        }
    }

    fun updateView() {
        // Initialize UI
        if (STOCK_RECEIVE_MODEL.supplierData.products != null) {
            STOCK_RECEIVE_MODEL.supplierData.products.let {
                products = it as ArrayList<SelectedProductModel>

                // Initialize List View
                stock_receive_confirm_product_list.apply {
                    layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

                    listAdapter = StockReceiveConfirmProductRVAdapter(products!!){
                        STOCK_RECEIVE_MODEL.updateProductRequestLiveData.postValue(it)
                    }

                    adapter = listAdapter
                    addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
                }
            }
        }

        // Stock keeper sign
        stock_receive_confirm_store_keeper_name.isEnabled = false
        stock_receive_confirm_store_keeper_name.setText(viewModel.userName)

        stock_receive_confirm_sign_store_keeper.setOnSingleClickListener {
            val signedName = stock_receive_confirm_store_keeper_name.text.toString()

            if (signedName.isNotEmpty()) {
                flow.openSignaturePad(PARAM_STORE_KEEPER_SIGN)
            } else {
                errorDialog("Please enter the stock keeper name")
            }
        }

        // Delivery Person sign
        stock_receive_confirm_sign_delivery_person.setOnSingleClickListener {
            val signedName = stock_receive_confirm_delivery_person_name.text.toString()

            if (signedName.isNotEmpty()) {
                if (signature_radio_button.isChecked) {
                    flow.openSignaturePad(StockReceiveSignaturePadSheetFragment.PARAM_DELIVERY_PERSON_SIGN)
                } else {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                        )
                        == PackageManager.PERMISSION_DENIED
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA
                        )

                    } else {
                        openCameraActivity()
                    }
                }
            } else {
                errorDialog("Please enter the delivery person name")
            }
        }
    }

    private fun openCameraActivity() {

        val directory =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath}/${BuildConfig.APP_NAME}/")
        if (!directory.exists())
            directory.mkdir()

        val fileNameToSave = "${Calendar.getInstance().timeInMillis}.jpg"

        capturedImage = File(directory, fileNameToSave)

        capturedImage?.createNewFile()

//        val outputFileUri = Uri.fromFile(file)
        // Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", createImageFile());
        val outputFileUri = FileProvider.getUriForFile(
            requireContext(),
            "${context?.applicationContext?.packageName}.provider",
            capturedImage ?: File("")
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        capturePhotoActivityResult?.launch(cameraIntent)
//        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE)
    }

    private fun setupOnActivityResult() {
        capturePhotoActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                stock_receive_confirm_sign_delivery_person.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                stock_receive_confirm_sign_delivery_person.setBackgroundResource(R.drawable.green_stroke_action_button)
                stock_receive_confirm_sign_delivery_person.setText(R.string.captured)
                stock_receive_confirm_sign_delivery_person.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )
                val image = BitmapFactory.decodeFile(capturedImage?.absolutePath)
                val deliveryPersonName = stock_receive_confirm_delivery_person_name.text.toString()
                STOCK_RECEIVE_MODEL.setData(
                    DATA_DELIVERY_PERSON_DATA,
                    SignatureInfo(deliveryPersonName, image)
                )
                delivery_person_captured_image_preview.visible()
                delivery_person_captured_image_preview.setImageBitmap(image)
            }
        }
    }

}