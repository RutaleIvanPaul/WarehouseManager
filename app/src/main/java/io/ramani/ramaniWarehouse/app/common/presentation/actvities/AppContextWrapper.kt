package io.ramani.ramaniWarehouse.app.common.presentation.actvities

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringProvider
import java.util.*


/**
 * Created by zMabrook on 04/04/18.
 */

class AppContextWrapper(context: Context) : ContextWrapper(context) {


    companion object {
        @Suppress("DEPRECATION")
        fun wrap(context: Context, newLocale: Locale): ContextWrapper {
            var newContext = context
            val res = newContext.resources
            val configuration = res.configuration

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(newLocale)
                newContext = newContext.createConfigurationContext(configuration)
            } else {
                configuration.locale = newLocale
                res.updateConfiguration(configuration, res.displayMetrics)
            }

            Locale.setDefault(newLocale)

            StringProvider.init(newContext)

            return ContextWrapper(newContext)
        }
    }

}