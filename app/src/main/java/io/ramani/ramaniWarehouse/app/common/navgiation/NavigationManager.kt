package io.ramani.ramaniWarehouse.app.common.navgiation

import android.app.Activity
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity

/**
 * Created by Amr on 9/18/17.
 */
class NavigationManager {
    enum class OpenMethod {
        ADD, ADD_TEMP, REPLACE
    }

    companion object {
        const val TEMP_FRAGMENT: String = "tempFragment"
    }

    interface NavigationListener {
        fun onBackStackChanged()
    }

    interface OnNavigatedListener {
        fun onNavigated(fragment: Fragment)
    }

    private var activity: Activity? = null
    private var fragmentManager: FragmentManager? = null
    private var fragmentContainerId: Int = -1
    private var navigationListener: NavigationListener? = null

    private val navigationListeners = mutableListOf<OnNavigatedListener>()

    fun init(fragmentManager: FragmentManager, fragmentContainerId: Int) {
        this.fragmentManager = fragmentManager
        this.fragmentManager?.addOnBackStackChangedListener {
            navigationListener?.onBackStackChanged()

            navigationListeners.forEach(::notifyNavigated)
        }
        this.fragmentContainerId = fragmentContainerId
    }

    private fun notifyNavigated(listener: OnNavigatedListener) {
        topFragment()?.apply(listener::onNavigated)
    }

    fun addOnNavigatedListener(action: (Fragment) -> Unit) {
        navigationListeners.add(object : OnNavigatedListener {
            override fun onNavigated(fragment: Fragment) {
                action(fragment)
            }
        })
    }

    fun addOnNavigatedListener(listener: OnNavigatedListener) {
        navigationListeners.add(listener)
    }

    fun removeNavigationListener(navigationListener: OnNavigatedListener) {
        navigationListeners.remove(navigationListener)
    }

    fun setOnBackStackChangedListener(navigationListener: NavigationListener) {
        this.navigationListener = navigationListener
    }

    fun open(
        fragment: Fragment,
        containerId: Int = fragmentContainerId,
        openMethod: OpenMethod = OpenMethod.REPLACE,
        addToBackStack: Boolean = true,
        fragmentTag: String = "",
        @AnimRes enterAnim: Int = R.anim.nav_enter_anim,
        @AnimRes exitAnim: Int = R.anim.nav_exit_anim,
        @AnimRes popEnterAnim: Int = R.anim.nav_pop_enter_anim,
        @AnimRes popExitAnim: Int = R.anim.nav_pop_exit_anim
    ) {
        fragmentManager?.let {
            val transaction = it.beginTransaction()

            transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)

            when (openMethod) {
                OpenMethod.REPLACE -> transaction.replace(containerId, fragment)
                OpenMethod.ADD -> transaction.add(containerId, fragment)
                OpenMethod.ADD_TEMP -> transaction.add(containerId, fragment, TEMP_FRAGMENT)
            }

            if (addToBackStack) {
                transaction.addToBackStack(fragmentTag)
            }

            transaction.commitAllowingStateLoss()
        }
    }

    fun removeTempFragment() {
        fragmentManager?.popBackStackImmediate(
            TEMP_FRAGMENT,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

    }

    fun openAsRoot(fragment: Fragment) {
        popEveryFragment()
        open(
            fragment, enterAnim = R.anim.nav_root_enter_anim, exitAnim = R.anim.nav_root_exit_anim,
            popEnterAnim = R.anim.nav_root_pop_enter_anim, popExitAnim = R.anim.nav_root_exit_anim
        )
    }

    fun remove(fragment: Fragment?) {
        fragment?.let {
            fragmentManager?.beginTransaction()?.remove(it)?.commit()
        }
    }

    fun navigateBackStack(activity: BaseActivity?) {
        if (isRootFragmentVisible()) {
            activity?.finish()
        } else {
            fragmentManager?.popBackStack()
        }
    }

    fun isRootFragmentVisible() =
        (fragmentManager?.backStackEntryCount ?: 0) <= 1

    fun backStackCount(): Int = fragmentManager?.backStackEntryCount ?: 0

    fun topFragment(): Fragment? =
        fragmentManager?.findFragmentById(fragmentContainerId)

    fun getCurrentFragmentByID(fragmentContainerID: Int): Fragment? =
        fragmentManager?.findFragmentById(fragmentContainerID)

    private fun popEveryFragment() {
        fragmentManager?.let {
            val backStackCount = it.backStackEntryCount
            for (i in 0 until backStackCount) {
                val id = it.getBackStackEntryAt(i).id
                it.popBackStack(id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    fun popToRootFragment() {
        fragmentManager?.let {
            val backStackCount = it.backStackEntryCount
            if (backStackCount > 0) {
                for (i in 0 until backStackCount-1) {
                    it.popBackStack()
                }
            }
        }
    }

    fun popToFragment(fragmentTag: String, inclusive: Boolean) {
        fragmentManager?.let {
            while (it.backStackEntryCount > 0) {
                if (fragmentTag != it.getBackStackEntryAt(it.backStackEntryCount - 1).name) {
                    it.popBackStackImmediate()

                } else {
                    if (inclusive) {
                        it.popBackStackImmediate()
                    }
                    break
                }
            }
        }
    }
}