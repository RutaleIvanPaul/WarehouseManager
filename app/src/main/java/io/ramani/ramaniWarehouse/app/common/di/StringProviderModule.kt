package io.ramani.ramaniWarehouse.app.common.di

import io.ramani.ramaniWarehouse.core.domain.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val stringProviderModule = Kodein.Module("stringProviderModule") {
    bind<IStringProvider>() with singleton {
        StringProvider
    }
}