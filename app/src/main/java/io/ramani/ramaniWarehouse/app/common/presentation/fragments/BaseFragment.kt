package io.ramani.ramaniWarehouse.app.common.presentation.fragments

import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.BaseNavigationViewInterface
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.showSnackbar
import io.ramani.ramaniWarehouse.app.common.presentation.interfaces.DisposablesHolder
import io.ramani.ramaniWarehouse.app.common.presentation.interfaces.addDisposable
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.NavigationEvent
import io.ramani.ramaniWarehouse.app.entities.ValidationError
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

/**
 * Created by Amr on 9/8/17.
 */
abstract class BaseFragment : Fragment(), KodeinAware, AnkoLogger, DisposablesHolder, BaseNavigationViewInterface {
    companion object {
        const val DEFAULT_FULL_SCREEN_DIALOG_HIDE_AFTER_TIME = 1250L
    }

    override val kodein by closestKodein()


    val baseActivity: BaseActivity?
        get() = if (isAdded && activity is BaseActivity) activity as BaseActivity else null

    abstract val baseViewModel: BaseViewModel?

    val navigationManager: NavigationManager = NavigationManager()

    protected val NO_LAYOUT_RES_ID = Int.MIN_VALUE
    protected val NO_MENU_RES_ID = -1

    protected val baseActivty: BaseActivity? by lazy {
        if (activity is BaseActivity) activity as BaseActivity else null
    }

    override val compositeDisposable = CompositeDisposable()

    private val snackbars = mutableMapOf<Snackbar, BaseTransientBottomBar.BaseCallback<Snackbar>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigationManager(navigationManager)
    }

    open fun onUpClick(): Boolean = false

    open fun onBackButtonPressed(): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.requestedOrientation = loadScreenOrientation()
        return if (getLayoutResId() != NO_LAYOUT_RES_ID) {
            inflater.inflate(getLayoutResId(), container, false)
        } else {
            null
        }
    }

    protected open fun loadScreenOrientation() = when (isTablet()) {
        true -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        false -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (getMenuResId() != NO_MENU_RES_ID) {
            inflater.inflate(getMenuResId(), menu)
            onOptionsMenuCreated(menu)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        baseViewModel?.let {
            subscribeConfirmLogout(it)
            subscribeConfirmRestart(it)
        }
    }

    override fun onDestroy() {
        snackbars.forEach { entry ->
            val snackbar = entry.key
            val callback = entry.value
            snackbar.removeCallback(callback)
        }
        super.onDestroy()
    }

    protected open fun pop() {


    }

    protected open fun initNavigationManager(navigationManager: NavigationManager) {}

    protected open fun onOptionsMenuCreated(menu: Menu?) {}

    protected open fun initView(view: View?) {

    }

    protected abstract fun getLayoutResId(): Int

    protected open fun getMenuResId() = NO_MENU_RES_ID

    open fun setToolbarTitle(title: String) {
        baseActivity?.baseViewModel?.toolbarTitleLiveData?.postValue(title)
    }

    open fun setupToolbarShare(isVisible: Boolean, onClick: () -> Unit) {
        baseActivity?.baseViewModel?.toolbarEnableShareLiveData?.postValue(
            Pair(
                isVisible,
                onClick
            )
        )
    }



    open fun setupToolbarBackButton(isBackEnabled: Boolean, isBackButton: Boolean) {
        baseActivity?.baseViewModel?.toolbarEnableBackButtonLiveData?.postValue(
            Pair(
                isBackEnabled,
                isBackButton
            )
        )
    }

    open fun refresh() {}

    protected fun isDualPane() =
        resources.getBoolean(R.bool.two_pane_layout)

    protected fun subscribeLoadingVisible(viewModel: BaseViewModel) {
        addDisposable(
            viewModel.isLoadingVisibleObservable.observeOn(AndroidSchedulers.mainThread())
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



      protected open fun getScreenWidth(marginToSubtract: Int = 0): Int {
        val windowDisplay = activity?.windowManager?.defaultDisplay
        val size = Point()
        windowDisplay?.getSize(size)

        return size.x - marginToSubtract
    }

    protected open fun getScreenHeight(marginToSubtract: Int = 0): Int {
        val windowDisplay = activity?.windowManager?.defaultDisplay
        val size = Point()
        windowDisplay?.getSize(size)

        return size.y - marginToSubtract
    }



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

    protected fun subscribeEmpty(viewModel: BaseViewModel) {
        addDisposable(viewModel.isEmptyObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (activity != null && isAdded) {
                        onEmpty()
                    }
                })
    }

    protected fun observeEmpty(viewModel: BaseViewModel, owner: LifecycleOwner) {
        viewModel.isEmptyLiveData.observe(owner, Observer { it ->
            it?.let {
                onEmpty()
            }
        })
    }


    protected open fun subscribeValidationErrors(viewModel: BaseViewModel) {
        addDisposable(viewModel.validationErrorsObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onValidationErrors(it)
                })
    }

    protected open fun observeValidationErrors(viewModel: BaseViewModel, owner: LifecycleOwner) {
        viewModel.validationErrorsLiveData.observe(owner, Observer {
            onValidationErrors(it)
        })
    }

    protected open fun observeNavigationEvent(viewModel: BaseViewModel, owner: LifecycleOwner) {
        viewModel.navigationEventLiveData.observe(owner, Observer {
            onNavigationEvent(it)
        })
    }

    open fun subscribeConfirmLogout(viewModel: BaseViewModel) {
        addDisposable(viewModel.confirmLogoutObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    errorDialog(it, {
                        viewModel.logout(null)
                    })
                })
    }

    open fun subscribeConfirmRestart(viewModel: BaseViewModel) {
        addDisposable(viewModel.confirmRestartObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    errorDialog(it, {
                        viewModel.restart()
                    })
                })
    }

    protected open fun showSnackbar(message: String, onDismiss: () -> Unit = {}) {
        val callback = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                onDismiss()
            }
        }
        val snackbar = activity?.showSnackbar(message)?.addCallback(callback)
        snackbar?.let {
            snackbars.put(it, callback)
        }
    }

    protected open fun showError(error: String) {}

    protected open fun showErrorWithRetry(error: String) {}

    protected open fun showErrorWithConfirm(error: String) {}

    protected open fun onEmpty() {}

    protected open fun onValidationErrors(errors: List<ValidationError>) {}

    protected open fun onNavigationEvent(event: NavigationEvent) {}

    protected fun isTablet() = resources.getBoolean(R.bool.isTablet)

    open fun onScroll(dx: Int, dy: Int) {

    }

    protected fun getFieldValueByInt(editText: EditText): Int {
        if (editText.text.isNotBlank())
            return editText.text.toString().toInt()
        else
            return -1
    }

    protected fun getFieldValueByDouble(editText: EditText): Double {
        if (editText.text.isNotBlank())
            return editText.text.toString().toDouble()
        else
            return -1.0
    }
}