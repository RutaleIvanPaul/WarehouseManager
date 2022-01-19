package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import kotlinx.android.synthetic.main.fragment_stock_receive_supplier.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AdapterView
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showConfirmDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL.Companion.DATA_DATE
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL.Companion.DATA_SUPPLIER
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull

class StockReceiveSupplierFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockReceiveSupplierFragment()
        private val IMAGE_DIRECTORY = "/RWTemp"
    }

    private val viewModelProvider: (Fragment) -> StockReceiveNowViewModel by factory()
    private lateinit var viewModel: StockReceiveNowViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    private val dateFormatter: DateFormatter by instance()
    private val images:ArrayList<String> = ArrayList();
    private var gridViewAdapter: GridViewAdapter? = null

    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_supplier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        subscribeObservers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)

        // Initialize UI
        val now = Date()
        STOCK_RECEIVE_MODEL.setData(DATA_DATE, now)
        supplier_receiving_date_label.text = dateFormatter.getCalendarTimeString(now)

        supplier_receiving_select_supplier_spinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            supplier_receiving_select_supplier_spinner.isSelected = true

            viewModel.getSuppliersActionLiveData.value?.get(newIndex)?.let {
                STOCK_RECEIVE_MODEL.setData(DATA_SUPPLIER, it)
            }

            checkIfGoNext()
        }

        stock_receive_take_photo.setOnSingleClickListener {
            showTakePhotoDialog()
        }

        gridViewAdapter = GridViewAdapter(requireActivity(), images)
        stock_receive_supplier_gallery.adapter = gridViewAdapter
        stock_receive_supplier_gallery.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                showConfirmDialog("Are you sure you want to delete this photo?", onConfirmed = {
                    images.removeAt(position)
                    updateGallery()
                })

                true
            }

       viewModel.getSuppliers(1, 200)
    }

    override fun onPause() {
        if (supplier_receiving_select_supplier_spinner != null)
            supplier_receiving_select_supplier_spinner.dismiss()

        super.onPause()
    }

    override fun onDestroy() {
        if (supplier_receiving_select_supplier_spinner != null)
            supplier_receiving_select_supplier_spinner.dismiss()

        super.onDestroy()
    }

    private fun subscribeObservers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel, this)
        subscribeError(viewModel)
        observerError(viewModel, this)

        viewModel.validationResponseLiveData.observe(this, {

        })

        viewModel.getSuppliersActionLiveData.observe(this, {
            val items = ArrayList<String>()
            for (supplier in it)
                items.add(supplier.name)

            supplier_receiving_select_supplier_spinner.setItems(items)

        })
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        stock_receive_supplier_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun updateGallery() {
        gridViewAdapter?.apply {
            setImages(images)
            notifyDataSetInvalidated()
        }

        STOCK_RECEIVE_MODEL.setData(STOCK_RECEIVE_MODEL.DATA_DOCUMENTS, images)
        checkIfGoNext()
    }

    private fun showTakePhotoDialog() {
        val pictureDialog = AlertDialog.Builder(requireActivity())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select image from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> chooseImageFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncherForGallery.launch(intent)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncherForCamera.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private var resultLauncherForGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null)
            {
                val pictureUri = data.data
                pictureUri?.let {
                    val bitmap = getCapturedImage(it)
                    val path = saveImage(bitmap)
                    images.add(path)
                    updateGallery()
                }
            }
        }
    }

    private var resultLauncherForCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            val thumbnail = data!!.extras!!.get("data") as Bitmap
            val path = saveImage(thumbnail)
            images.add(path)
            updateGallery()
        }
    }

    private fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

        val wallpaperDirectory = File (requireActivity().getExternalFilesDir(null).toString() + IMAGE_DIRECTORY)
        if (!wallpaperDirectory.exists())
            wallpaperDirectory.mkdirs()

        try {
            val f = File(wallpaperDirectory, ((Calendar.getInstance().getTimeInMillis()).toString() + ".png"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(requireActivity(), arrayOf(f.getPath()), arrayOf("image/png"), null)
            fo.close()

            return f.getAbsolutePath()
        }
        catch (e1: IOException){
            e1.printStackTrace()
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        val bitmap = when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                selectedPhotoUri
            )
            else -> {
                val source = ImageDecoder.createSource(requireActivity().contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }

        return bitmap
    }

    private fun checkIfGoNext() {
        STOCK_RECEIVE_MODEL.supplierData?.let {
            if (it.supplier.isNotNull() && !it.documents.isNullOrEmpty())
                STOCK_RECEIVE_MODEL.allowToGoNextLiveData.postValue(Pair(0, true))
        }
    }

    /**
     * Grid Adapter
     */
    class GridViewAdapter(private val context: Context, var listImageURLs: List<String>) : BaseAdapter() {

        fun setImages(images: List<String>) {
            listImageURLs = images
        }

        override fun getCount(): Int {
            return listImageURLs.size
        }

        override fun getItem(position: Int): Any {
            return listImageURLs[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val viewHolder: ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_gallery, null)
                viewHolder = ViewHolder()
                viewHolder.imageView = convertView!!.findViewById(R.id.item_gallery)
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            viewHolder.imageView?.apply {
                layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, 150)
                loadImage(listImageURLs[position])
            }

            return convertView
        }

        internal inner class ViewHolder {
            var imageView: ImageView? = null
        }
    }

}