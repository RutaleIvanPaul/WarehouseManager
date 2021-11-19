package io.ramani.ramaniWarehouse.app.common.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.ramani.ramaniWarehouse.app.common.debug.DebugHelper
import io.ramani.ramaniWarehouse.app.common.presentation.interfaces.DisposablesHolder
import io.ramani.ramaniWarehouse.app.common.presentation.interfaces.clearDisposables
import io.ramani.ramaniWarehouse.app.common.presentation.interfaces.disposeAll
import io.ramani.ramaniWarehouse.domainCore.log.logInfo

/**
 *  a class that is responsible for handling common operations during Fragment LifeCycle
 *
 * Created by Ahmed Adel Ismail on 2/24/2018.
 */
class BaseFragmentLifeCycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentCreated(fm: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, fragment, savedInstanceState)
        logInfo("${fragment::class.java.simpleName} created")
        DebugHelper.setCurrentScreen(fragment::class.java.simpleName)
    }

    override fun onFragmentPaused(fm: FragmentManager, fragment: Fragment) {
        super.onFragmentPaused(fm, fragment)

        fragment.takeIf { it is DisposablesHolder }
                .let { it as? DisposablesHolder }
                .also { it?.clearDisposables() }
    }


    override fun onFragmentDestroyed(fm: FragmentManager, fragment: Fragment) {
        super.onFragmentDestroyed(fm, fragment)
        logInfo("${fragment::class.java.simpleName} destroyed")

        fragment.takeIf { it is DisposablesHolder }
                .let { it as? DisposablesHolder }
                .also { it?.disposeAll() }

    }
}