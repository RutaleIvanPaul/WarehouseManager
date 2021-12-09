package io.ramani.ramaniWarehouse.app.auth.flow

import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment

interface StockReceiveFlow {
    fun openReceiveNow()
    fun openSignaturePad(what: String)
    fun pop(fragment: BaseFragment)
}