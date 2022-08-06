package io.ramani.ramaniWarehouse.app.common.presentation.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified T : ViewModel> Fragment.viewModel(factoryBuilder: () -> ViewModelProvider.Factory) =
        ViewModelProviders.of(this, factoryBuilder()).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.viewModel() =
        ViewModelProviders.of(this).get(T::class.java)