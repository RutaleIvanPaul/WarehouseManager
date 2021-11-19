package io.ramani.ramaniWarehouse.app.common.presentation.actvities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.extensions.letIfType
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationTags
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.BaseNavigationViewInterface
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.color
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.interfaces.DisposablesHolder
import io.ramani.ramaniWarehouse.app.common.presentation.interfaces.addDisposable
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.domainCore.lang.getLocaleFromString
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import java.util.*


/**
 * This is the code95 Activity for all the Activities across the application
 *
 * Created by Amr on 9/8/17.
 */
abstract class BaseActivity : AppCompatActivity(), KodeinAware, DisposablesHolder {
    override val kodein by closestKodein()

    abstract val baseViewModel: BaseViewModel?

    var navigationManager: NavigationManager? = null
    var mainFragmentContainerId = R.id.main_fragment_container

    var baseToolBar: RelativeLayout? = null
    var toolbarTitle: TextView? = null
    var backIV: ImageView? = null
    var shareIV: ImageView? = null
    var toolbar_logoIV: ImageView? = null

    protected var isBackEnabled = false
        set(value) {
            field = value
        }

    var isBackButton = true
        set(value) {
            field = value
        }

    protected var hideBackOnRoot = true

    protected var isLogoEnabled = false
        set(value) {
            field = value
        }

    override val compositeDisposable = CompositeDisposable()
//    private val downloadBroadCastReceiver = DownloadFilesBroadCastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = color(R.color.white)

        navigationManager = NavigationManager().apply {
            init(supportFragmentManager, mainFragmentContainerId)
        }

        navigationManager?.setOnBackStackChangedListener(object :
            NavigationManager.NavigationListener {
            override fun onBackStackChanged() {
                if (hideBackOnRoot) {
                    isBackEnabled = checkBackEnabled()
                }
                isLogoEnabled = checkIsLogoEnabled()
            }
        })
//        registerReceiver(downloadBroadCastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }


    override fun onResume() {
        super.onResume()
        baseViewModel?.let {
            subscribeConfirmLogout(it)
            subscribeConfirmRestart(it)
        }
    }

    override fun attachBaseContext(newBase: Context) {
//        val prefsManager = PrefsManager(newBase)
        val languageType = Locale("en")
        super.attachBaseContext(AppContextWrapper.wrap(newBase, languageType))
    }


    override fun onBackPressed() {
        if (navigationManager?.topFragment() is BaseFragment) {
            if (!(navigationManager?.topFragment() as BaseFragment).onBackButtonPressed()) {
                navigationBack()
            }
        } else {
            navigationBack()
        }
    }

    protected fun navigationBack() {
        navigationManager?.navigateBackStack(this)
    }

    protected open fun onUpClick(): Boolean = false

    protected fun subscribeToolbarShareEnable() {
        baseViewModel?.toolbarEnableShareLiveData?.observe(this, Observer {
            shareIV?.visible(it.first)
            shareIV?.setOnClickListener { _ ->
                it.second
            }
        })
    }

    protected fun subscribeBackButtonEnable() {
        baseViewModel?.toolbarEnableBackButtonLiveData?.observe(this, Observer {
            isBackEnabled = it.first
            isBackButton = it.second
        })
    }

    protected fun subscribeError(viewModel: BaseViewModel) {
        addDisposable(viewModel.errorObservable.subscribe {
            when (it.type) {
                PresentationError.ERROR_TEXT -> showError(it.error)
                PresentationError.ERROR_TEXT_RETRY -> showErrorWithRetry(it.error)
                PresentationError.ERROR_TEXT_CONFIRM -> showErrorWithConfirm(it.error)
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

    open fun subscribeConfirmLogout(viewModel: BaseViewModel) {
        addDisposable(viewModel.confirmLogoutObservable.subscribe {
            errorDialog(it) {
                viewModel.logout(null)
            }
        })
    }

    open fun subscribeConfirmRestart(viewModel: BaseViewModel) {
        addDisposable(viewModel.confirmRestartObservable.subscribe {
            errorDialog(it) {
                viewModel.restart()
            }
        })
    }

    protected open fun showError(error: String) {
        // Template method
    }

    protected open fun showErrorWithRetry(error: String) {
        // Template method
    }

    protected open fun showErrorWithConfirm(error: String) {
        // Template method
    }

    private fun checkBackEnabled() =
        navigationManager?.let { manager ->
            manager.topFragment()?.letIfType<BaseNavigationViewInterface, Boolean> {
                when (it.navTag) {
                    NavigationTags.HOME,
                    NavigationTags.FIND_US,
                    NavigationTags.BOOKING -> false
                    else -> true
                }
            } ?: true
        } ?: true

    private fun checkIsLogoEnabled(): Boolean =
        navigationManager?.let {
            it.topFragment()?.letIfType<BaseNavigationViewInterface, Boolean> {
                when (it.navTag) {
                    NavigationTags.HOME,
                    NavigationTags.FIND_US,
                    NavigationTags.BOOKING,
                    NavigationTags.WELCOME -> false
                    else -> true
                }
            } ?: true
        } ?: true


    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(downloadBroadCastReceiver)
    }
}