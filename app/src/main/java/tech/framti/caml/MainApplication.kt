package tech.framti.caml

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import tech.framti.caml.util.ReleaseLogTree
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseLogTree())
        }
    }
}