package io.ramani.ramaniWarehouse.app.common.presentation.dialogs

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
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
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), KodeinAware, DisposablesHolder {

    override val kodein by closestKodein()

    override val compositeDisposable = CompositeDisposable()

    abstract val baseViewModel: BaseViewModel?



    private val snackbars = mutableMapOf<Snackbar, BaseTransientBottomBar.BaseCallback<Snackbar>>()

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
}