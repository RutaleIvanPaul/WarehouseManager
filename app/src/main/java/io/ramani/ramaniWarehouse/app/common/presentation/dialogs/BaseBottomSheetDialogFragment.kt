package io.ramani.ramaniWarehouse.app.common.presentation.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.showSnackbar
import io.ramani.ramaniWarehouse.app.common.presentation.interfaces.DisposablesHolder
import io.ramani.ramaniWarehouse.app.common.presentation.interfaces.addDisposable
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.entities.ValidationError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


/**
 * Created by Amr on 11/2/17.
 */
abstract class BaseBottomSheetDialogFragment(private val openFullScreen: Boolean = false) :
    BottomSheetDialogFragment(), KodeinAware,
    DisposablesHolder {

    override val kodein by closestKodein()

    override val compositeDisposable = CompositeDisposable()

    abstract val baseViewModel: BaseViewModel?

    val baseActivity: BaseActivity?
        get() = if (isAdded && activity is BaseActivity) activity as BaseActivity else null

    private val snackbars = mutableMapOf<Snackbar, BaseTransientBottomBar.BaseCallback<Snackbar>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener(object : DialogInterface.OnShowListener {
            override fun onShow(dialogInterface: DialogInterface?) {
                if (openFullScreen) {
                    val bottomSheetDialog = dialogInterface as BottomSheetDialog
                    setupFullHeight(bottomSheetDialog)
                }
            }
        })
        return dialog
    }


    override fun onResume() {
        super.onResume()
        baseViewModel?.apply {
            subscribeConfirmLogout(this)
            subscribeConfirmRestart(this)
        }
    }

    protected fun subscribeLoadingVisible(viewModel: BaseViewModel) {
        addDisposable(viewModel.isLoadingVisibleObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (activity != null && isAdded) {
                    setLoadingIndicatorVisible(it)
                }
            })
    }

    protected fun observeLoadingVisible(viewModel: BaseViewModel, owner: LifecycleOwner) {
        viewModel.loadingLiveData.observe(owner, Observer {
            it?.apply(::setLoadingIndicatorVisible)
        })
    }

    protected open fun setLoadingIndicatorVisible(visible: Boolean) {}


    protected fun subscribeLoadingError(viewModel: BaseViewModel) {
        addDisposable(viewModel.loadingErrorObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (activity != null && isAdded) {
                    onLoadingError(it)
                }
            })
    }

    protected open fun onLoadingError(error: String) {}

    protected fun subscribeError(viewModel: BaseViewModel) {
        addDisposable(viewModel.errorObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (activity != null && isAdded) {
                    when (it.type) {
                        PresentationError.ERROR_TEXT -> showError(it.error)
                        PresentationError.ERROR_TEXT_RETRY -> showErrorWithRetry(it.error)
                        PresentationError.ERROR_TEXT_CONFIRM -> showErrorWithConfirm(it.error)

                    }
                }
            })
    }

    protected fun observerError(viewModel: BaseViewModel, owner: LifecycleOwner) {
        viewModel.errorLiveData.observe(owner, Observer {
            it?.let {
                when (it.type) {
                    PresentationError.ERROR_TEXT -> showError(it.error)
                    PresentationError.ERROR_TEXT_RETRY -> showErrorWithRetry(it.error)
                    PresentationError.ERROR_TEXT_CONFIRM -> showErrorWithConfirm(it.error)
                }
            }
        })
    }

    protected open fun subscribeValidationErrors(viewModel: BaseViewModel) {
        addDisposable(viewModel.validationErrorsObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onValidationErrors(it)
            })
    }

    protected open fun subscribeConfirmLogout(viewModel: BaseViewModel) {
        addDisposable(viewModel.confirmLogoutObservable.subscribe {
            errorDialog(it) {
                viewModel.logout(null)
            }
        })
    }

    protected open fun subscribeConfirmRestart(viewModel: BaseViewModel) {
        addDisposable(viewModel.confirmRestartObservable.subscribe {
            errorDialog(it) {
                viewModel.restart()
            }
        })
    }

    protected open fun showSnackbar(message: String, onDismiss: () -> Unit = {}) {
        val callback = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                onDismiss()
            }
        }
        activity?.showSnackbar(message)?.addCallback(callback)?.apply {
            snackbars[this] = callback
        }
    }


    protected open fun showError(error: String) {}

    protected open fun showErrorWithRetry(error: String) {}

    protected open fun showErrorWithConfirm(error: String) {}

    protected open fun onValidationErrors(errors: List<ValidationError>) {}

    protected open fun initView(view: View?) {

    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet: FrameLayout? =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<View>? =
            bottomSheet?.let { BottomSheetBehavior.from<View?>(it) }
        val layoutParams: ViewGroup.LayoutParams? = bottomSheet?.getLayoutParams()
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet?.layoutParams = layoutParams
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)?.getWindowManager()?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}