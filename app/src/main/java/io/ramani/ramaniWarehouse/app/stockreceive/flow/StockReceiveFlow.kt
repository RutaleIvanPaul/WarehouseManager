package io.ramani.ramaniWarehouse.app.auth.flow

import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment

interface StockReceiveFlow {
    fun openReceiveNow()

    fun pop(fragment: BaseFragment)
}