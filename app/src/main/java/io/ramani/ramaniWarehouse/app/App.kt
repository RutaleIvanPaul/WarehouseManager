package io.ramani.ramaniWarehouse.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import io.ramani.ramaniWarehouse.app.common.di.appModule
import io.ramani.ramaniWarehouse.app.common.download.DownloadManager
import io.ramani.ramaniWarehouse.app.common.download.FilesDownloadManager
import io.ramani.ramaniWarehouse.app.common.download.IFilesDownloadManager
import io.ramani.ramaniWarehouse.app.common.download.IMediaDownloadManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivityLifeCycleCallbacks
import io.ramani.ramaniWarehouse.domainCore.prefs.Prefs
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.domainCore.printer.PX400Printer
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 * Created by Amr on 9/7/17.
 */
class App : Application(), KodeinAware {
    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))
        bind<Prefs>() with singleton { PrefsManager(this@App) }
        bind<IMediaDownloadManager>() with singleton { DownloadManager(this@App, instance(), instance()) }
        bind<IFilesDownloadManager>() with singleton { FilesDownloadManager(this@App, instance()) }
        bind<PX400Printer>() with singleton { PX400Printer(this@App)}
        import(appModule)
    }

    override fun onCreate() {
        super.onCreate()
        initPusNotificationsManager()
        registerActivityLifecycleCallbacks(BaseActivityLifeCycleCallbacks())
//            initInstaBug()
    }


    private fun initPusNotificationsManager() {
       //TODO: Setup firebase here
    }


}