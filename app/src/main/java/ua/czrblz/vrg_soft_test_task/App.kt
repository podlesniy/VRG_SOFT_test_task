package ua.czrblz.vrg_soft_test_task

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import ua.czrblz.vrg_soft_test_task.di.*

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@App)
            allowOverride(true)
            modules(listOf(appModule, domainModule, dataModule))
        }
    }
}